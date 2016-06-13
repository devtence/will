package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;

import java.util.List;

/**
 * Created by sorcerer on 6/9/16.
 */
public class Content extends BaseModel<Content>{

    private String title;

    private String description;

    private List<Author> authors;

    private List<Long> categories;

    private List<Label> labels;

    private Long idLanguage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Long getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(Long idLanguage) {
        this.idLanguage = idLanguage;
    }

    @Override
    public void validate() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void update(Content data) throws Exception {

    }

    @Override
    public void load(long id) {

    }

    public static Content get(long id){
        return (Content) get(id, Content.class);
    }
}
