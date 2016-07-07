package com.devtence.will.dev.models;

import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;

/**
 * interface that every new user model must implement
 * Created by plessmann on 03/06/16.
 */
public interface AuthenticableEntity {

	public AuthorizationWrapper authorize() throws Exception;

	public boolean goodLogin(String inputPassword) throws Exception;

}
