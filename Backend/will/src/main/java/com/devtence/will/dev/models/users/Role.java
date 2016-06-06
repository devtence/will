package com.devtence.will.dev.models.users;

import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.List;

/**
 * Created by plessmann on 02/06/16.
 */
@Entity
public class Role extends BaseModel<Role> implements Serializable {

    @Index
    private String name;
    private List<Permission> permissions;

    public Role() {
    }

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

    @Override
    public void validate() throws Exception {
        this.save();
    }

    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    public static Role getById(Long id) throws Exception {
        return DbObjectify.ofy().load().type(Role.class).id(id).now();
    }

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

}
