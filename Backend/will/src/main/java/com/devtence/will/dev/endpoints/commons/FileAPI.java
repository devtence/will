package com.devtence.will.dev.endpoints.commons;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.GcsAppIdentityServiceUrlSigner;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.commons.wrappers.BooleanWrapper;
import com.devtence.will.dev.commons.wrappers.UploadWrapper;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.models.BaseModel;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.services.storage.StorageScopes;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by plessmann on 06/06/16.
 */
@Api(
	name = Constants.FILES_API_NAME,
	version = Constants.API_MASTER_VERSION,
	scopes = {Constants.EMAIL_SCOPE},
	clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
	audiences = {Constants.ANDROID_AUDIENCE},
	authenticators = {UserAuthenticator.class}
)
public class FileAPI {

	/** E-mail address of the service account. */
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";
	private static final String X_UPLOAD_CONTENT_TYPE = "X-Upload-Content-Type";
	private static final String VIDEO_MP4 = "video/mp4";
	private static final String AUTHORIZATION = "Authorization";
	private static final String ORIGIN = "Origin";
	private static final String ORIGIN_VALUE = "https://%s";
	private static final String HOST = "Host";
	private static final String LOCATION = "location";
	private static final String POST = "POST";
	private static final String BEARER = "Bearer %s";
	private static final String HARC = "user_%s/harc_%s";
	private static final String ROUTE_HARC = "user_%s/%s/harc_%s";
	private static final String FAILED_HTTP_ERROR = "Failed : HTTP error code : %s :: %s";
	private static final String GET = "GET";
	private static final String DEFAULT_BUCKET = "default_bucket";

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();


	private static final String SERVICE_ACCOUNT_EMAIL = "";
	private static final String CERTIFICATE_ROUTE = "WEB-INF/";
	private static final String BUCKET = "";
	private static final String CDN_URL_BASE = "/%s";



	private static final Logger log = Logger.getLogger(FileAPI.class.getName());

	private static final String input = "{\"name\":\"%s\"}";
	private static final String UPLOAD_URL = "https://www.googleapis.com/upload/storage/v1/b/%s/o?uploadType=resumable";


	private static final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
			.initialRetryDelayMillis(10)
			.retryMaxAttempts(10)
			.totalRetryPeriodMillis(15000)
			.build());

	@ApiMethod( httpMethod = ApiMethod.HttpMethod.GET,  name = "files.auth", path = "files/auth")
	public UploadWrapper singleFileAuthentication(@Named("type") String type, @Named("id_user") @Nullable @DefaultValue("0") Long idUser, @Named("route") String route, HttpServletRequest req, User user) throws BadRequestException, UnauthorizedException {
		BaseController.validateUser(user);
		try {

			UploadWrapper tr = new UploadWrapper();
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
					.setServiceAccountScopes(Collections.singleton(StorageScopes.CLOUD_PLATFORM))
					.setServiceAccountPrivateKeyFromP12File(new File(CERTIFICATE_ROUTE))
					.build();
			credential.refreshToken();
			tr.setAuthToken(String.format(BEARER, credential.getAccessToken()));
			HttpURLConnection conn = null;
			try {
				//create uploadURL
				String file = null;
				if(route != null && !route.isEmpty()) {
					file = String.format(ROUTE_HARC, idUser, route, UUID.randomUUID().toString());
				} else {
					file = String.format(HARC, idUser, UUID.randomUUID().toString());
				}

				String initURL =  String.format(UPLOAD_URL, BUCKET);

				//call url
				URL url = new URL(initURL);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod(POST);

				//setting headers and metadata
				conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
				conn.setRequestProperty(X_UPLOAD_CONTENT_TYPE, type == null ? VIDEO_MP4 : type );
				conn.setRequestProperty(AUTHORIZATION, tr.getAuthToken());
				conn.setRequestProperty(ORIGIN, String.format(ORIGIN_VALUE, req.getHeader(HOST)));

				OutputStream os = conn.getOutputStream();
				os.write(String.format(input, file).getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException(String.format(FAILED_HTTP_ERROR, conn.getResponseCode(), conn.getResponseMessage())) ;
				}
				tr.setUploadURL(conn.getHeaderField(LOCATION));
				tr.setUniqueFile(file);
				tr.setCdnUrl(String.format(CDN_URL_BASE, file));
				tr.setOrigin(req.getHeader(HOST));
			}finally {
				if (conn != null){
					conn.disconnect();
				}
			}
			return tr;
		}catch (Exception e){
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(Constants.INVALID_PARAMS);
		}
	}

	@ApiMethod( httpMethod = ApiMethod.HttpMethod.GET,  name = "files.auth.multiple", path = "files/authmultiple")
	public List<UploadWrapper> multipleFilesAuthentication(@Named("type") String type, @Named("id_user") @Nullable @DefaultValue("0") Long idUser, @Named("route") String route, @Named("files_quantity") Integer filesQuantity, HttpServletRequest req, User user) throws BadRequestException, UnauthorizedException {
		BaseController.validateUser(user);
		try {
			List<UploadWrapper> wrapperList = new ArrayList<>(filesQuantity);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
					.setServiceAccountScopes(Collections.singleton(StorageScopes.CLOUD_PLATFORM))
					.setServiceAccountPrivateKeyFromP12File(new File(CERTIFICATE_ROUTE))
					.build();
			credential.refreshToken();
			UploadWrapper tr = null;
			HttpURLConnection conn = null;
			String initURL =  String.format(UPLOAD_URL, BUCKET);
			String file = null;
			OutputStream os = null;
			URL url = null;
			for (int i = 0; i < filesQuantity; i++) {
				tr = new UploadWrapper();
				tr.setAuthToken(String.format(BEARER, credential.getAccessToken()));
				try {
					if(route != null && !route.isEmpty()) {
						file = String.format(ROUTE_HARC, idUser, route, UUID.randomUUID().toString());
					} else {
						file = String.format(HARC, idUser, UUID.randomUUID().toString());
					}
					url = new URL(initURL);
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod(POST);
					conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
					conn.setRequestProperty(X_UPLOAD_CONTENT_TYPE, type == null ? VIDEO_MP4 : type );
					conn.setRequestProperty(AUTHORIZATION, tr.getAuthToken());
					conn.setRequestProperty(ORIGIN, String.format(ORIGIN_VALUE, req.getHeader(HOST)));
					os = conn.getOutputStream();
					os.write(String.format(input, file).getBytes());
					os.flush();
					if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
						throw new RuntimeException(String.format(FAILED_HTTP_ERROR, conn.getResponseCode(), conn.getResponseMessage())) ;
					}
					tr.setUploadURL(conn.getHeaderField(LOCATION));
					tr.setUniqueFile(file);
					tr.setCdnUrl(String.format(CDN_URL_BASE, file));
					tr.setOrigin(req.getHeader(HOST));
					wrapperList.add(tr);
				}finally {
					if (conn != null){
						conn.disconnect();
					}
				}
			}
			return wrapperList;
		}catch (Exception e){
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(Constants.INVALID_PARAMS);
		}
	}

	@ApiMethod( httpMethod = ApiMethod.HttpMethod.GET,  name = "files.url", path = "files/url")
	public UploadWrapper fileUrlGenerator(@Named("fileRoute") String fileRoute, User user) throws BadRequestException, UnauthorizedException {
		BaseController.validateUser(user);
		try {
			GcsAppIdentityServiceUrlSigner gcsAppIdentityServiceUrlSigner = new GcsAppIdentityServiceUrlSigner();
			String auth = gcsAppIdentityServiceUrlSigner.getSignedUrl(GET, fileRoute, BUCKET);
			System.out.println(auth);
			return new UploadWrapper(auth, null, null, null, null);
		}catch (Exception e){
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(Constants.INVALID_PARAMS);
		}
	}

	@ApiMethod( httpMethod = ApiMethod.HttpMethod.GET,  name = "files.delete", path = "files/delete")
	public BooleanWrapper deleteFileFromBucket(@Named("filename") String filename, User user) throws BadRequestException, UnauthorizedException {
		BaseController.validateUser(user);
		try {
			if(filename != null && !filename.isEmpty()) {
				GcsFilename gcsfileName = new GcsFilename(BUCKET, filename);
				if (gcsService.delete(gcsfileName)) {
					return new BooleanWrapper(true);
				} else {
					return new BooleanWrapper(false);
				}
			} else {
				return new BooleanWrapper(false);
			}
		}catch (Exception e){
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(Constants.INVALID_PARAMS);
		}
	}

}
