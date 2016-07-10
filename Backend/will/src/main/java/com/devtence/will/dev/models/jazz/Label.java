package com.devtence.will.dev.models.jazz;

/**
 * Class that models the Label structure, that it's used to categorize Contents
 *
 * @author sorcerer
 * @since 2015-06-09
 * @see Content
 *
 */
public class Label {

    /**
     * Name defined for the label
     */
    private String name;

    /**
     * Recommended constructor
     * @param name
     */
    public Label(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
