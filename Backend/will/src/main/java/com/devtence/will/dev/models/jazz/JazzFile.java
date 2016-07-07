package com.devtence.will.dev.models.jazz;

/**
 * model to map basic files to the google data store and google cloud storage
 * Created by sorcerer on 6/9/16.
 */
public class JazzFile {

    private String name;

    private String originalName;

    private String contentType;

    private String url;

    /*CONSTRUCTORS*/

    public JazzFile() {

    }

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
