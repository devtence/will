package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;

/**
 * Created by sorcerer on 6/9/16.
 */
public class Language extends BaseModel<Language>{

    private String language;

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

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void update(Language data) throws Exception {

    }

    @Override
    public void load(long id) {

    }
}
