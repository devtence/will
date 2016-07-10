package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

/**
 * Class that models the Author data and map it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Author.
 *
 * @author sorcerer
 * @since 2015-06-09
 */
@Entity
public class Author extends BaseModel<Author> {

    /**
     * Author full name
     */
    @Index
    private String name;

    /**
     * Author biography
     */
    private String bio;

    /**
     * Author status
     *
     * TODO: change to enum
     */
    @Index
    private Integer status;

    /**
     * List of languages the author uses
     */
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

    /**
     * Default constructor
     */
    public Author() {
    }

    /**
     * Recommended constructor
     * @param id
     * @param name
     * @param bio
     * @param status
     * @param languages
     */
    public Author(Long id, String name, String bio, Integer status, List<Language> languages) {
        this.setId(id);
        this.name = name;
        this.bio = bio;
        this.status = status;
        this.languages = languages;
    }

    /**
     * Stores the object to the database
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        this.save();
    }

    /**
     * Removes the object from the database
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * Compares the object to the new Data, and executes an update if necessary
     * @param data new data to insert
     * @throws Exception
     */
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
        //not required, not needed to implement
    }

    /**
     * Returns an Author object using the get function from parent
     * @param id of the object to get
     * @return
     */
    public static Author get(long id){
        return (Author) get(id, Author.class);
    }
}
