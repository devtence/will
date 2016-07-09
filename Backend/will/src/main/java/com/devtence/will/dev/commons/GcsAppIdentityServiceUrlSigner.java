package com.devtence.will.dev.commons;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Class that implements the functions that creates temporary URLs, that give access to the elements that exists in
 * a Google cloud Storage Bucket this URLs expire after a configured time defined by the constants in the class.
 *
 * this security layer needs to work with the default permissions set for the bucket in the google cloud storage
 * console.
 *
 * @author plessmann
 * @since 2016-05-16
 *
 */
public class GcsAppIdentityServiceUrlSigner {

    private static final int EXPIRATION_TIME = 5;
    private static final String BASE_URL = "https://storage.googleapis.com/%s/%s?GoogleAccessId=%s&Expires=%s&Signature=%s";
    private static final String BUCKET = "test-bucket";
    private static final String UTF_8 = "UTF-8";
    private static final long unitMil = 1000L;

    private final AppIdentityService identityService = AppIdentityServiceFactory.getAppIdentityService();

    /**
     * creates a signed temporal url
     * @param httpVerb
     * @param fileName
     * @param bucket
     * @return
     * @throws Exception
     */
    public String getSignedUrl(final String httpVerb, final String fileName, String bucket) throws Exception {
        final long expiration = expiration();
        final String unsigned = stringToSign(expiration, fileName, httpVerb);
        final String signature = sign(unsigned);
        return String.format(BASE_URL, bucket, fileName, clientId(), String.valueOf(expiration), URLEncoder.encode(signature, UTF_8));
    }

    /**
     * generates and returns the expiration time of the url
     * @return
     */
    private long expiration() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return calendar.getTimeInMillis() / unitMil;
    }

    /**
     * builds the string that will be signed
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
     * builds and returns the string that is being signed encoded in base64
     * @param stringToSign
     * @return
     * @throws UnsupportedEncodingException
     */
    private String sign(final String stringToSign) throws UnsupportedEncodingException {
        final AppIdentityService.SigningResult signingResult = identityService.signForApp(stringToSign.getBytes());
        return new String(Base64.encodeBase64(signingResult.getSignature(), false), UTF_8);
    }

    /**
     * returns the id for the google service account that identifies the app
     * @return
     */
    private String clientId() {
        return identityService.getServiceAccountName();
    }
}
