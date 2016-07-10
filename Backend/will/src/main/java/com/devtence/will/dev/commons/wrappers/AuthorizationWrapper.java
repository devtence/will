package com.devtence.will.dev.commons.wrappers;

/**
 * A wrapper for the Authorization data.
 *
 * @author plessmann
 * @since 2016-03-10
 */
public class AuthorizationWrapper {

	/**
	 * The JWT used to grant access.
	 */
	private String authorization;
	/**
	 * The user id in the database, used as a key in AuthorizationCache
	 */
	private Long authorizationKey;
	/**
	 * The user type in the database.
	 */
	private Integer type;

	/**
	 * Constructor.
	 * @param authorization	The JWT used to grant access
	 * @param authorizationKey The user id int the database, used as a key used in AuthorizationCache
	 */
	public AuthorizationWrapper(String authorization, Long authorizationKey) {
		this.authorization = authorization;
		this.authorizationKey = authorizationKey;
	}

	/**
	 * Constructor.
	 * @param authorization	The JWT used to grant access
	 * @param authorizationKey The user id int the database, used as a key used in AuthorizationCache
	 * @param type The user type in the DB
	 */
	public AuthorizationWrapper(String authorization, Long authorizationKey, Integer type) {
		this.authorization = authorization;
		this.authorizationKey = authorizationKey;
		this.type = type;
	}

	/**
	 * Get the JWT used to grant access.
	 */
	public String getAuthorization() {
		return authorization;
	}

	/**
	 * Set the JWT to be used to grant access.
	 */
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	/**
	 * Get the user id in the database, used as a key in @see com.devtence.will.dev.commons.caches.AuthorizationCache#getCache .
	 */
	public Long getAuthorizationKey() {
		return authorizationKey;
	}

	/**
	 * Set the user id int the database, used as a key used in AuthorizationCache.
	 */
	public void setAuthorizationKey(Long authorizationKey) {
		this.authorizationKey = authorizationKey;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
