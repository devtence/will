package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

/**
 * Created by sorcerer on 6/9/16.
 */
@Entity
public class Author extends BaseModel<Author> {

    @Index
    private String name;

    private String bio;

    //TODO: change to enum
    @Index
    private Integer status;

    private List<Language> languages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public Author() {
    }

    public Author(Long id, String name, String bio, Integer status, List<Language> languages) {
        this.setId(id);
        this.name = name;
        this.bio = bio;
        this.status = status;
        this.languages = languages;
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
    public void update(Author data) throws Exception {
        boolean mod = false;

        if (data.getName() != null){
            setName(data.getName());
            mod |= true;
        }

        if (data.getBio() != null){
            setBio(data.getBio());
            mod |= true;
        }

        if (data.getStatus() != null){
            setStatus(data.getStatus());
            mod |= true;
        }

        if (data.getLanguages() != null){
            setLanguages(data.getLanguages());
            mod |= true;
        }

        if (mod){
            this.validate();
        }

    }

    @Override
    public void load(long id) {

    }

    public static Author get(long id){
        return (Author) get(id, Author.class);
    }
}
