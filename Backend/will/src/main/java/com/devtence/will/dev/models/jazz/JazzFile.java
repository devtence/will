package com.devtence.will.dev.models.jazz;

/**
 * Class that model Content Files structure, this files will be stored on the Google Cloud Storage, not
 * in the persistence layer defined by the Google Cloud Datastore.
 *
 * @author sorcerer
 * @since 2015-06-09
 *
 */
public class JazzFile {

    /**
     * Normalized name for the file
     */
    private String name;

    /**
     * Received name for the file
     */
    private String originalName;

    /**
     * Metadata that indicates the type of file
     */
    private String contentType;

    /**
     * Default URL where the file is stored
     */
    private String url;

    /**
     * Default Constructor
     */
    public JazzFile() {

    }

    /**
     * Recommended constructor
     * @param name
     * @param originalName
     * @param contentType
     * @param url
     */
    public JazzFile(String name, String originalName, String contentType, String url) {
        this.name = name;
        this.originalName = originalName;
        this.contentType = contentType;
        this.url = url;
    }

    /*GETTERS AND SETTERS*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
