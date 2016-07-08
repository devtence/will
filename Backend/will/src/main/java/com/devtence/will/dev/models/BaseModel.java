package com.devtence.will.dev.models;


import com.devtence.will.Constants;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * base class for the Will Models that use Google Cloud Data Store as DB(GDS).
 *
 * this base also defines the basic functions every model must have, and implements basic functions, like saving,
 * deleting, and basic search.
 *
 * every model must be registered on the DbObjectify class so it can be used, otherwise it will fail executing.
 *
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

    /**basic abstract methods that every model must implement**/

    public abstract void validate() throws Exception;

    public abstract void destroy() throws Exception;

    public abstract void update(T data) throws Exception;

    public abstract void load(long id);

    /**
     * this method updates the current object in the GDS
     * TODO check this method and possible delete
     * @deprecated
     */
    public void update() {
        DbObjectify.ofy().save().entity(this).now();
    }

    /***
     * this method saves the current object to GDS, the object is saved in the collection defined by the class object
     */
    protected void save(){
        DbObjectify.ofy().save().entity(this).now();
    }

    /**
     * this method deteles the current object from GDS
     */
    protected void delete(){
        DbObjectify.ofy().delete().entity(this).now();
    }

    /**
     * this method returns a basic list using cursors and pagination
     * @param startAt string that defines the position of the cursor to use if exist
     * @param limit integer for the max amount
     * @param className class name for the elements to search
     * @param sortFields list of fields to sort
     * @param sortDirs list of directions for the sorting the directions must match sortFields
     * @return list of items
     */
    public static ListItem getList(String startAt, int limit, Class className,
                                   List<String> sortFields,List<Boolean> sortDirs) {
        ListItem toReturn = new ListItem();

        Query query = null;
        if (sortFields != null && !sortFields.isEmpty()) {
            query = DbObjectify.ofy().load().type(className);
            StringBuilder sort = new StringBuilder();
			for (int i = 0; i < sortFields.size(); i++) {
                if(sortDirs != null && !sortDirs.isEmpty() && i < sortDirs.size() && sortDirs.get(i)) {
                    sort.append(Constants.DESC_SORTER);
                }
                sort.append(sortFields.get(i));
                query = query.order(sort.toString());
                sort.delete(0, sort.length());
            }
        } else {
            query = DbObjectify.ofy().load().type(className);
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

    /**
     * this method returns a list with all the elements of the collection.
     *
     * important note: this method is not recommended for collections with big amounts of data it doesnt use pagination
     * or cursors so its really expensive.     *
     * @param className Class of the collection to return
     * @return List of objects
     */
    public static List getAll(Class className){
        return DbObjectify.ofy().load().type(className).list();
    }

    /**
     * this method returns an object from the collection that uses the id provided
     * @param id unique identifier on the collection
     * @param className class of the model to search
     * @return object that matches the class defined and the id
     */
    public static Object get(long id, Class className){
       return DbObjectify.ofy().load().type(className).id(id).now();
    }
}
