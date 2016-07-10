package com.devtence.will.dev.commons;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * This class exposes objects stored on the Google Cloud Storage to the public by creating temporary URLs. The URL
 * will expire after a configured time defined by the constants in the class.
 * <p>
 * This security layer needs to work with the default permissions set for the bucket in the Google Cloud Storage
 * console.
 *
 * @author plessmann
 * @since 2016-05-16
 *
 */
public class GcsAppIdentityServiceUrlSigner {

    /**
     * Expiration time in minutes. Default: 5 minutes.
     */
    private static final int EXPIRATION_TIME = 5;

    /**
     * Base URL for the Storage API.
     */
    private static final String BASE_URL = "https://storage.googleapis.com/%s/%s?GoogleAccessId=%s&Expires=%s&Signature=%s";

    /**
     * Bucket name.
     */
    private static final String BUCKET = "test-bucket";

    private static final String UTF_8 = "UTF-8";

    /**
     * Number of milliseconds in a second.
     */
    private static final long unitMil = 1000L;

    private final AppIdentityService identityService = AppIdentityServiceFactory.getAppIdentityService();

    /**
     * Creates a signed temporary URL.
     * @param httpVerb String that specifies the HTTP method to use
     * @param fileName name of the file that's going to be signed
     * @param bucket the bucket where the file resides
     * @return signed URL
     * @throws Exception
     */
    public String getSignedUrl(final String httpVerb, final String fileName, String bucket) throws Exception {
        final long expiration = expiration();
        final String unsigned = stringToSign(expiration, fileName, httpVerb);
        final String signature = sign(unsigned);
        return String.format(BASE_URL, bucket, fileName, clientId(), String.valueOf(expiration), URLEncoder.encode(signature, UTF_8));
    }

    /**
     * Generates the Epoch date when the URL is going to be expired, represented in seconds.
     *
     * @return Epoch date in seconds
     */
    private long expiration() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return calendar.getTimeInMillis() / unitMil;
    }

    /**
     * Builds the string that will be signed.
     *
     * @param expiration time for the url to exists
     * @param filename that is going to be accessed
     * @param httpVerb
     * @return
     */
    private String stringToSign(final long expiration, String filename, String httpVerb) {
        final String contentType = "";
        final String contentMD5 = "";
        final String canonicalizedExtensionHeaders = "";
        final String canonicalizedResource = "/" + BUCKET + /*"/" + FOLDER +*/ "/" + filename;
        return httpVerb + "\n" + contentMD5 + "\n" + contentType + "\n"
                + expiration + "\n" + canonicalizedExtensionHeaders + canonicalizedResource;
    }

    /**
     * Builds and returns the string that is being signed encoded in base64
     *
     * @param stringToSign string URL to be signed
     * @return URL signed and encoded in base64
     * @throws UnsupportedEncodingException
     */
    private String sign(final String stringToSign) throws UnsupportedEncodingException {
        final AppIdentityService.SigningResult signingResult = identityService.signForApp(stringToSign.getBytes());
        return new String(Base64.encodeBase64(signingResult.getSignature(), false), UTF_8);
    }

    /**
     * Returns the ID for the Google Service Account that identifies the app
     * @return service account name
     */
    private String clientId() {
        return identityService.getServiceAccountName();
    }
}
