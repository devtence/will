package com.devtence.will.dev.models;

import java.util.List;

/**
 * Created by sorcerer on 12/10/15.
 */
public class ListItem {

    private Integer totalCount;
    private String cursor;
    private List items;

    public ListItem() {
        this.totalCount = 0;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
