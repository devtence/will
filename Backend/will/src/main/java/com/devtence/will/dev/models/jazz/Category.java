package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 * Class that models the Category data and map it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Categories.
 *
 * @author sorcerer
 * @since 2015-06-09
 */
@Entity
public class Category extends BaseModel<Category>{

    /**
     * Category name
     */
    @Index
    private String name;

    /**
     * Category description
     */
    private String description;

    /**
     * Languange which the category belongs to
     */
    @Index
    private Long idLanguage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(Long idLanguage) {
        this.idLanguage = idLanguage;
    }

    /**
     * Default constructor
     */
    public Category() {
    }

    /**
     * Recommended constructor
     * @param id
     * @param name
     * @param description
     * @param idLanguage
     */
    public Category(Long id, String name, String description, Long idLanguage) {
        setId(id);
        this.name = name;
        this.description = description;
        this.idLanguage = idLanguage;
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
     * Compares the object to the new data, and executes an update if necessary
     * @param data new data to insert
     * @throws Exception
     */
    @Override
    public void update(Category data) throws Exception {
        boolean mod = false;

        if (data.getName() != null){
            setName(data.getName());
            mod |= true;
        }

        if (data.getDescription() != null){
            setDescription(data.getDescription());
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

    /**
     * Loads the object with the data that matches the id on the Database
     * @param id
     */
    @Override
    public void load(long id) {
        Category me  = get(id);
        setName(me.getName());
        setDescription(me.getDescription());
        setIdLanguage(me.getIdLanguage());
    }

    /**
     * Returns a Category object using the get function from parent
     * @param id of the object to get
     * @return
     */
    public static Category get(long id){
        return (Category) get(id, Category.class);
    }
}
