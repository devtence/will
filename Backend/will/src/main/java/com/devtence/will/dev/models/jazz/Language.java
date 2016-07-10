package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 * Class that models the Language data and map it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Languages.
 *
 * @author sorcerer
 * @since 2015-06-09
 *
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


    /**
     * Default constructor
     */
    public Language() {
    }

    /**
     * Recommended constructor
     * @param id
     * @param language
     * @param shortName
     */
    public Language(Long id, String language, String shortName) {
        setId(id);
        this.language = language;
        this.shortName = shortName;
    }

    /*basic methods*/

    /**
     * Saves the object to the data base.
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        this.save();
    }

    /**
     * Removes the object from the database.
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * Compares the new Data with the object and executes the update if necessary
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

    /**
     * Loads the object with the data obtained from the database
     * @param id
     */
    @Override
    public void load(long id) {
        Language me  = Language.get(id);
        this.setLanguage(me.getLanguage());
        this.setShortName(me.getShortName());
    }

    /**
     * Returns a language object using the get function from parent
     * @param id of the object to get
     * @return
     */
    public static Language get(long id){
        return (Language) get(id, Language.class);
    }
}
