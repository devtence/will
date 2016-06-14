package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

/**
 * Created by sorcerer on 6/9/16.
 */
@Entity
public class Content extends BaseModel<Content>{

    @Index
    private String title;

    private String description;

    private List<Author> authors;

    private List<Long> categories;

    private List<Label> labels;

    private List<JazzFile> files;

    @Index
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

    public List<JazzFile> getFiles() {
        return files;
    }

    public void setFiles(List<JazzFile> files) {
        this.files = files;
    }

    public Long getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(Long idLanguage) {
        this.idLanguage = idLanguage;
    }

    @Override
    public void validate() throws Exception {
        this.save();
    }

    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    @Override
    public void update(Content data) throws Exception {
        boolean mod = false;

        if (data.getTitle() != null){
            setTitle(data.getTitle());
            mod |= true;
        }

        if (data.getDescription() != null){
            setDescription(data.getDescription());
            mod |= true;
        }

        if (data.getAuthors() != null){
            setAuthors(data.getAuthors());
            mod |= true;
        }

        if (data.getCategories() != null){
            setCategories(data.getCategories());
            mod |= true;
        }

        if (data.getLabels() != null){
            setLabels(data.getLabels());
            mod |= true;
        }

        if (data.getFiles() != null){
            setFiles(data.getFiles());
            mod |= true;
        }

        if (data.getIdLanguage() != null){
            setIdLanguage(data.getIdLanguage());
            mod |= true;
        }


        if (mod){
            this.validate();
        }

    }

    @Override
    public void load(long id) {
        Content me  = get(id);
        setTitle(me.getTitle());
        setDescription(me.getDescription());
        setAuthors(me.getAuthors());
        setCategories(me.getCategories());
        setLabels(me.getLabels());
        setFiles(me.getFiles());
        setIdLanguage(me.getIdLanguage());
    }

    public static Content get(long id){
        return (Content) get(id, Content.class);
    }
}
