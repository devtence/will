package com.devtence.will.dev.models.users;

import java.io.Serializable;

/**
 * Created by plessmann on 02/06/16.
 */
public class Permission implements Serializable {

    private String route;

	private Boolean userRequired;

    public Permission() {
    }

    public Permission(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

	public Boolean getUserRequired() {
		return userRequired;
	}

	public void setUserRequired(Boolean userRequired) {
		this.userRequired = userRequired;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Permission that = (Permission) o;
		return route != null ? route.endsWith(that.route) : that.route == null;
	}

	@Override
	public int hashCode() {
		return route != null ? route.hashCode() : 0;
	}
}
