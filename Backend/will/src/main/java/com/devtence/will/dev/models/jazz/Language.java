package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by sorcerer on 6/9/16.
 */
@Entity
public class Language extends BaseModel<Language>{

    @Index
    private String language;

    @Index
    private String shortName;

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

    @Override
    public void validate() throws Exception {
        this.save();
    }

    @Override
    public void destroy() throws Exception {
        this.destroy();
    }

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

    public static Language get(long id){
        return (Language) get(id, Language.class);
    }
}
