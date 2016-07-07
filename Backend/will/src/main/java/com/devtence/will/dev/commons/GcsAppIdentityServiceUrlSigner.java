package com.devtence.will.dev.commons;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * This class generates a temporary URL to acces elements in an bucket of Google Cloud Storage
 *
 * Created by plessmann on 02/05/16.
 */
public class GcsAppIdentityServiceUrlSigner {

	private static final int EXPIRATION_TIME = 5;
	private static final String BASE_URL = "https://storage.googleapis.com/%s/%s?GoogleAccessId=%s&Expires=%s&Signature=%s";
	private static final String BUCKET = "test-bucket";
	private static final String UTF_8 = "UTF-8";
	private static final long unitMil = 1000L;

	private final AppIdentityService identityService = AppIdentityServiceFactory.getAppIdentityService();

	public String getSignedUrl(final String httpVerb, final String fileName, String bucket) throws Exception {
		final long expiration = expiration();
		final String unsigned = stringToSign(expiration, fileName, httpVerb);
		final String signature = sign(unsigned);
		return String.format(BASE_URL, bucket, fileName, clientId(), String.valueOf(expiration), URLEncoder.encode(signature, UTF_8));
	}

	private long expiration() {
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
		return calendar.getTimeInMillis() / unitMil;
	}

	private String stringToSign(final long expiration, String filename, String httpVerb) {
		final String contentType = "";
		final String contentMD5 = "";
		final String canonicalizedExtensionHeaders = "";
		final String canonicalizedResource = "/" + BUCKET + /*"/" + FOLDER +*/ "/" + filename;
		return httpVerb + "\n" + contentMD5 + "\n" + contentType + "\n"
				+ expiration + "\n" + canonicalizedExtensionHeaders + canonicalizedResource;
	}

	private String sign(final String stringToSign) throws UnsupportedEncodingException {
		final AppIdentityService.SigningResult signingResult = identityService.signForApp(stringToSign.getBytes());
		return new String(Base64.encodeBase64(signingResult.getSignature(), false), UTF_8);
	}

	private String clientId() {
		return identityService.getServiceAccountName();
	}
}
