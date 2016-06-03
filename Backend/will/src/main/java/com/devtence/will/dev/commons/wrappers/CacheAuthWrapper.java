package com.devtence.will.dev.commons.wrappers;

import com.devtence.will.dev.models.users.Role;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created by plessmann on 06/05/16.
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}
}
