package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.wrappers.CacheAuthWrapper;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.User;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Singleton to manage the usage of memcache to store the access to the endpoints using JWT.
 *
 * Created by plessmann on 10/03/16.
 */
public class AuthorizationCache {

	/**
	 * The protected instance
	 */
	private static AuthorizationCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache userCache;

	/**
	 * Contructor for the instance in which the cache timeout is defined
	 * @throws Exception CacheException if the CacheFactory could not be accessed, Exception if the timeout configuration is not set
	 */
	public AuthorizationCache() throws Exception {
		CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		Map properties = new HashMap<>();
		properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
		userCache = cacheFactory.createCache(properties);
	}

	/**
	 * Access control for the singleton
	 * @return the created instance
	 * @throws Exception CacheException if the CacheFactory could not be accessed, Exception if the timeout configuration is not set
	 */
	public static synchronized AuthorizationCache getInstance() throws Exception {
		if(me == null){
			me = new AuthorizationCache();
		}
		return me;
	}

	/**
	 * Access to the cache element
	 * @return cache to be used to manage the authorization
	 */
	public Cache getUserCache() {
		return userCache;
	}

	public CacheAuthWrapper getAuth(long id) throws Exception {
		CacheAuthWrapper auth = (CacheAuthWrapper) userCache.get(id);
		if(auth == null) {
			User user = User.getById(id);
			if (user != null) {
				if(user.getJwt() == null || user.getSecret() == null) {
					throw new InvalidValueException(Constants.INVALID_JWT_OR_SECRET);
				}
				auth = new CacheAuthWrapper(user.getIdUser(), user.getJwt(), user.getSecret());
				userCache.put(id, auth);
			} else {
				throw new InvalidValueException(String.format(Constants.INVALID_ID, id));
			}
		}
		return auth;
	}

	public void setAuth(long id, String jwt, String secret) {
		setAuth(new CacheAuthWrapper(id, jwt, secret));
	}

	public void setAuth(CacheAuthWrapper cacheAuthWrapper){
		userCache.put(cacheAuthWrapper.getId(), cacheAuthWrapper);
	}

}
