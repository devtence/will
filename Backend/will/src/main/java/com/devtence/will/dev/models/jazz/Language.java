package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 * basic model for languages
 * Created by sorcerer on 6/9/16.
 */
@Entity
public class Language extends BaseModel<Language> {

    @Index
    private String language;

    @Index
    private String shortName;

    /*getters and setters*/

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /*Constructors*/

    public Language() {
    }

    public Language(Long id, String language, String shortName) {
        setId(id);
        this.language = language;
        this.shortName = shortName;
    }

    /*basic methods*/

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
    public void update(Language data) throws Exception {
        boolean mod = false;

        if (data.getLanguage() != null){
            setLanguage(data.getLanguage());
            mod |= true;
        }

        if (data.getShortName() != null){
            setShortName(data.getShortName());
            mod |= true;
        }

        if (mod){
            this.validate();
        }
    }

    @Override
    public void load(long id) {
        Language me  = Language.get(id);
        this.setLanguage(me.getLanguage());
        this.setShortName(me.getShortName());
    }

    /**
     * custom method that returns a language object using the basic get function from parent
     * @param id of the object to get
     * @return
     */
    public static Language get(long id){
        return (Language) get(id, Language.class);
    }
}
