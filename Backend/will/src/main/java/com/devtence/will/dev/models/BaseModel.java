package com.devtence.will.dev.models;


import com.devtence.will.Constants;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Class created to serve as the base for the models that use Google Cloud Datastore for persistence.
 * Defines basic methods every model must have, and implements basic functions like paginated listings.
 *
 * It's very important that every Model must be registered on the DbObjectify Class otherwise it'll fail on execution.
 *
 * @author sorcerer
 * @since 2015-07-18
 *
 * @see DbObjectify
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

    /**
     * Needs to be implemented to validate the data that wants to be persisted.
     */
    public abstract void validate() throws Exception;

    /**
     * Needs to be implemented to delete a record persisted.
     */
    public abstract void destroy() throws Exception;

    /**
     * Needs to implement the update functionality
     * @param data data to be updated on the persistence layer.
     */
    public abstract void update(T data) throws Exception;


    /**
     * Needs to implement the loading functionality from the persistence layer.
     * @param id
     */
    public abstract void load(long id);

    /**
     * this method updates the current object in the Google Cloud Datastore.
     * TODO: Recheck method, we might be able to deprecate it.
     * @deprecated
     */
    public void update() {
        DbObjectify.ofy().save().entity(this).now();
    }

    /***
     *
     * Persists the current object to Google Cloud Datastore, the object is saved in the collection defined
     * by the class object
     */
    protected void save(){
        DbObjectify.ofy().save().entity(this).now();
    }

    /**
     * Deletes the current object from Google Cloud Datastore.
     */
    protected void delete(){
        DbObjectify.ofy().delete().entity(this).now();
    }

    /**
     * Method that returns a basic list using cursors and pagination. Also implements multiple field sort.
     *
     * @param startAt Defines the position of the cursor to use if it exists
     * @param limit Maximum number of items to be returned
     * @param className class name for the elements to search
     * @param sortFields List of fields for sorting the result list
     * @param sortDirs List sort direction for each of the sorting fields specified in sortFields. Must match the
     *                 items on the sortFields argument.
     * @return List of items sorted and paginated
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
     * Returns a list that contains all the elements in the collection from the Google Cloud Datastore.
     *
     * Important note: this method is not recommended for very large collections, it doesn't use pagination
     * or cursors so it'll be very expensive to use.
     * @param className Class of the collection to return
     * @return List of objects
     */
    public static List getAll(Class className){
        return DbObjectify.ofy().load().type(className).list();
    }

    /**
     * Returns an object from the Google Cloud Datastore collection specified by it's ID.
     *
     * @param id unique identifier on the collection
     * @param className class of the model to search
     * @return object that matches the class defined and the id
     */
    public static Object get(long id, Class className){
       return DbObjectify.ofy().load().type(className).id(id).now();
    }
}
