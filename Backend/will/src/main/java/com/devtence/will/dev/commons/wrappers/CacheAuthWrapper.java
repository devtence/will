package com.devtence.will.dev.commons.wrappers;

import java.io.Serializable;

/**
 * Created by plessmann on 06/05/16.
 */
public class CacheAuthWrapper implements Serializable{



	private Long id;
	private String jwt;
	private String secret;

	public CacheAuthWrapper(Long id, String jwt, String secret) {
		this.id = id;
		this.jwt = jwt;
		this.secret = secret;
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
}
