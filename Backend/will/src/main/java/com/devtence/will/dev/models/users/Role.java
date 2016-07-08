package com.devtence.will.dev.models.users;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.List;

/**
 * user role class that contains the permitions that an user has
 *
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

    public static Role get(Long id) throws Exception {
        return (Role) get(id, Role.class);
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

    @Override
    public void load(long id) {
        Role me = (Role) get(id, Role.class);
        setId(me.getId());
        setName(me.getName());
        setPermissions(me.getPermissions());
    }

    public static Role getById(Long id) throws Exception {
        return (Role) get(id, Role.class);
    }

}
