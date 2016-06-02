package com.devtence.will.dev.models.users;

import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

/**
 * Created by plessmann on 02/06/16.
 */
@Entity
public class Role extends BaseModel {

	@Id
	private Long idRole;
	@Index
	private String name;
	private List<Permission> permissions;

	public Role(String name, List<Permission> permissions) {
		this.name = name;
		this.permissions = permissions;
	}

	public Long getIdRole() {
		return idRole;
	}

	public void setIdRole(Long idRole) {
		this.idRole = idRole;
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

	public static Role getById(Long id) throws Exception {
		return DbObjectify.ofy().load().type(Role.class).id(id).now();
	}

}
