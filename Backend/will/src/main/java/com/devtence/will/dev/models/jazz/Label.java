package com.devtence.will.dev.models.jazz;

/**
 * model for basic labels
 * Created by sorcerer on 6/9/16.
 */
public class Label {

    private String name;

    /*constructor*/
    public Label(String name) {
        this.name = name;
    }

    /*getter and setter*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
