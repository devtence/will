package com.devtence.will.dev.models;


import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * base class for all the models of LP
 * Created by sorcerer on 7/18/15.
 */
public abstract class BaseModel {

    public abstract void validate() throws Exception;

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

    public static List getAll(Class actual){
        return DbObjectify.ofy().load().type(actual).list();
    }

}
