package com.devtence.will.dev.models.users;

import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.List;

/**
 * Class that models the Clients  data and map it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Clients.
 * <p>
 *     According to our implementation a Client is an application that is allowed to call on the APIs
 * </p>
 *
 * @author plessmann
 * @since 2015-06-16
 * @see Permission
 *
 */
@Entity
public class Client extends BaseModel<Client> implements Serializable {

    /**
     * Client name
     */
    @Index
    private String name;

    /**
     * list of permissions
     */
    private List<Permission> permissions;

    /**
     * Default constructor
     */
    public Client() {
    }

    /**
     * Recommended constructor
     * @param name
     * @param permissions
     */
    public Client(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    /**
     * Constructor to copy object
     * @param me
     */
    public Client(Client me){
        this.setId(me.getId());
        this.name = me.name;
        this.permissions = me.permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Stores the object to the database
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        this.save();
    }

    /**
     * Removes the object from the database
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * Returns a Client object using the get function from parent
     * @param id of the object to get
     * @return
     */
    public static Client getById(Long id) throws Exception {
        return (Client) get(id, Client.class);
    }

    /**
     * Compares the object with the new data and executes the update if necessary
     * @param data new data to insert
     * @throws Exception
     */
    @Override
    public void update(Client data) throws Exception {
        boolean mod = false;

        if (data.getName() != null){
            setName(data.getName());
            mod |= true;
        }

        if (data.getPermissions() != null){
            setPermissions(data.getPermissions());
            mod |=true;
        }

        if (mod){
            this.validate();
        }
    }

    /**
     * Loads the Object with the data from the database
     * @param id
     */
    @Override
    public void load(long id){
        Client me = DbObjectify.ofy().load().type(Client.class).id(id).now();
        this.setId(me.getId());
        this.setName(me.getName());
        this.setPermissions(me.getPermissions());
    }
}
