package com.devtence.will.dev.commons.wrappers;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import java.io.Serializable;
import java.util.List;

/**
 * Wrapper Class used to store user data on the AuthorizationCache it contains the user id a Json Web Token,
 * its secret to decode and the permission roles it has.
 *
 *
 * @author plessmann
 * @since 2016-05-16
 *
 */
public class CacheAuthWrapper implements Serializable {

    private Long id;
    private String jwt;
    private String secret;

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private List<Long> roles;

    public CacheAuthWrapper(Long id, String jwt, String secret, List<Long> roles) {
        this.id = id;
        this.jwt = jwt;
        this.secret = secret;
        this.roles = roles;
    }

    /**
     * gets the id
      * @return id of the object
     */
    public Long getId() {
        return id;
    }

    /**
     * sets the id
     * @param id to set on the object
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * returns the json web token in the object
     * @return
     */
    public String getJwt() {
        return jwt;
    }

    /**
     * sets the json web token
     * @param jwt
     */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    /**
     * returns the secret to decode the json web token
     * @return
     */
    public String getSecret() {
        return secret;
    }

    /**
     * sets the secret to decode the json web token in the object
     * @param secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * returns the user roles
     * @return
     */
    public List<Long> getRoles() {
        return roles;
    }

    /**
     * sets the user roles
     * @param roles
     */
    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }
}
