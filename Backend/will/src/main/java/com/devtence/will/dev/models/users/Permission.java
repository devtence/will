package com.devtence.will.dev.models.users;

import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by plessmann on 02/06/16.
 */
@Entity
public class Permission extends BaseModel {

    @Id
    private Long id;
    @Index
    private String route;

    public Permission() {
    }

    public Permission(String route) {
        this.route = route;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public void validate() throws Exception {
        this.save();
    }

    public static Permission getById(Long id) throws Exception {
        return DbObjectify.ofy().load().type(Permission.class).id(id).now();
    }

	public void update(Permission toUpdate) throws Exception {
		boolean mod = false;

		if (toUpdate.getRoute() != null) {
			setRoute(toUpdate.getRoute());
			mod |= true;
		}

		if (mod){
			this.validate();
		}
	}

}
