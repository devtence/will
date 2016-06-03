package com.devtence.will.dev.models.users;

/**
 * Created by plessmann on 02/06/16.
 */
public class Permission {

    private String route;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Permission that = (Permission) o;

		return route != null ? route.equals(that.route) : that.route == null;

	}

	@Override
	public int hashCode() {
		return route != null ? route.hashCode() : 0;
	}
}
