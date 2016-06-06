package com.devtence.will.dev.models;


import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * base class for all the models of LP
 * Created by sorcerer on 7/18/15.
 */
public abstract class BaseModel<T extends BaseModel> {

    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract void validate() throws Exception;

    public abstract void destroy() throws Exception;

    public abstract void update(T data) throws Exception;

    public void update() {
        DbObjectify.ofy().save().entity(this).now();
    }

    protected void save(){
        DbObjectify.ofy().save().entity(this).now();
    }

    protected void delete(){
        DbObjectify.ofy().delete().entity(this).now();
    }

    public static ListItem getList(String startAt, int limit, Class fromDb, String sortField, String sortDir) {
        ListItem toReturn = new ListItem();

        Query query = null;
        if (sortField != null) {
            if (sortDir.equalsIgnoreCase("DESC")){
                sortField = "-" + sortField;
            }
            query = DbObjectify.ofy().load().type(fromDb).order(sortField);
        } else {
            query = DbObjectify.ofy().load().type(fromDb);
        }

        if (startAt != null) {
            query = query.startAt(Cursor.fromWebSafeString(startAt));
        }

        int totalCount = query.count();

        boolean cont = false;

        // List for inserting the result items
        ArrayList result = new ArrayList();
        QueryResultIterator iterator = query.limit(limit).iterator();

        while (iterator.hasNext()) {
            result.add(iterator.next());
            cont = true;
        }

        if (cont) {
            Cursor cursor = iterator.getCursor();
            toReturn.setItems(result);
            toReturn.setCursor(cursor.toWebSafeString());
        }

        toReturn.setTotalCount(totalCount);

        return toReturn;
    }

    public static List getAll(Class objClass){
        return DbObjectify.ofy().load().type(objClass).list();
    }

    public static Object get(long id, Class objClass){
       return DbObjectify.ofy().load().type(objClass).id(id).now();
    }

    public abstract void load(long id);


}
