package com.devtence.will.dev.models.users;

import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.List;

/**
 * base model for clients (apps that are allowed to call the APIs)
 * Created by plessmann on 02/06/16.
 */
@Entity
public class Client extends BaseModel<Client> implements Serializable {

    @Index
    private String name;
    private List<Permission> permissions;

    public Client() {
    }

    public Client(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

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
     * this method validate fields and save the object into the GDS
     * in this case no validation is made
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        this.save();
    }

    /**
     * this method to delete and object from the GDS
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * custom method that returns a language object using the basic get function from parent
     * @param id of the object to get
     * @return
     */
    public static Client getById(Long id) throws Exception {
        return (Client) get(id, Client.class);
    }

    /**
     * this method checks the new object with the old, and if its different does the update.
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

    @Override
    public void load(long id){
        Client me = DbObjectify.ofy().load().type(Client.class).id(id).now();
        this.setId(me.getId());
        this.setName(me.getName());
        this.setPermissions(me.getPermissions());
    }
}
