package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by sorcerer on 6/9/16.
 */
@Entity
public class Category extends BaseModel<Category>{

    @Index
    private String name;

    private String description;

    @Index
    private Long idLanguage;

    /*setters and getterts*/

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

    /*constructors*/

    public Category() {
    }

    public Category(Long id, String name, String description, Long idLanguage) {
        setId(id);
        this.name = name;
        this.description = description;
        this.idLanguage = idLanguage;
    }

    /**
     * this method validate fields and save the object into the GDS
     * in this case no validation is made
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        this.save();
    }

    /**
     * this method to delete and object from the GDS
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * this method checks the new object with the old, and if its different does the update.
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

    @Override
    public void load(long id) {
        Category me  = get(id);
        setName(me.getName());
        setDescription(me.getDescription());
        setIdLanguage(me.getIdLanguage());
    }

    /**
     * custom method that returns a language object using the basic get function from parent
     * @param id of the object to get
     * @return
     */
    public static Category get(long id){
        return (Category) get(id, Category.class);
    }
}
