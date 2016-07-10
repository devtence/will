package com.devtence.will.dev.models.users;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.List;

/**
 * Class that models the Role data and maps it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Roles.
 * <p>
 * It contains the permission that a User Object is allow to execute on the platform
 *
 * @author plessmann
 * @since 2015-06-02
 *
 */
@Entity
public class Role extends BaseModel<Role> implements Serializable {

    /**
     * Name used for the role
     */
    @Index
    private String name;

    /**
     * List of permissions
     */
    private List<Permission> permissions;

    /**
     * Default constructor.
     */
    public Role() {
    }

    /**
     * Recommended constructor
     * @param name
     * @param permissions
     */
    public Role(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
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
     * Stores the object in the database
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
     * Returns the role object queried by id
     * @param id
     * @return
     * @throws Exception
     */
    public static Role get(Long id) throws Exception {
        return (Role) get(id, Role.class);
    }

    /**
     * Compares the object to the new data, and executes an update if necessary
     * @param data data to be updated on the persistence layer.
     * @throws Exception
     */
    @Override
    public void update(Role data) throws Exception {
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
     * Loads the object with the data from the database
     * @param id
     */
    @Override
    public void load(long id) {
        Role me = (Role) get(id, Role.class);
        setId(me.getId());
        setName(me.getName());
        setPermissions(me.getPermissions());
    }

    /**
     * Returns the Role queried by id
     * @param id
     * @return
     * @throws Exception
     */
    public static Role getById(Long id) throws Exception {
        return (Role) get(id, Role.class);
    }

}
