package com.devtence.will.dev.models.users;

import java.io.Serializable;

/**
 * Class that models the Permission structure to the persistence layer.
 *
 * @author plessmann
 * @since 2015-06-02
 *
 */
public class Permission implements Serializable {

    /**
     * url route the permission uses
     */
    private String route;

    /**
     * indicates if an user is required
     */
    private Boolean userRequired;

    public Permission() {
    }

    public Permission(String route) {
        this.route = route;
        this.userRequired = false;
    }

    public Permission(String route, Boolean userRequired) {
        this.route = route;
        this.userRequired = userRequired;
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
