package com.devtence.will.dev.commons.wrappers;

/**
 * Wrapper Class used to return the resumable upload session data in the FileAPI
 *
 * @author plessmann
 * @since 2016-03-11
 */
public class UploadWrapper {

    private String authToken;

    private String uploadURL;

    private String uniqueFile;

    private String cdnUrl;

    private String type;

    private String origin;

    public UploadWrapper() {
    }

    public UploadWrapper(String authToken, String uploadURL, String uniqueFile, String type, String origin) {
        this.authToken = authToken;
        this.uploadURL = uploadURL;
        this.uniqueFile = uniqueFile;
        this.type = type;
        this.origin = origin;
    }

    public UploadWrapper(String authToken, String uploadURL, String uniqueFile, String cdnUrl, String type, String origin) {
        this.authToken = authToken;
        this.uploadURL = uploadURL;
        this.uniqueFile = uniqueFile;
        this.cdnUrl = cdnUrl;
        this.type = type;
        this.origin = origin;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUploadURL() {
        return uploadURL;
    }

    public void setUploadURL(String uploadURL) {
        this.uploadURL = uploadURL;
    }

    public String getUniqueFile() {
        return uniqueFile;
    }

    public void setUniqueFile(String uniqueFile) {
        this.uniqueFile = uniqueFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }
}
