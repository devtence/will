package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;

/**
 * Created by sorcerer on 6/9/16.
 */
public class Category extends BaseModel<Category>{

    private String name;

    private String description;

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

    @Override
    public void validate() throws Exception {
        this.save();
    }

    @Override
    public void destroy() throws Exception {
        this.destroy();
    }

    @Override
    public void update(Category data) throws Exception {

    }

    @Override
    public void load(long id) {

    }
}
