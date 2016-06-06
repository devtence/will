package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.Role;
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
public class RolesCache {

	/**
	 * The protected instance
	 */
	private static RolesCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache rolesCache;

	/**
	 * Contructor for the instance in which the cache timeout is defined
	 * @throws Exception CacheException if the CacheFactory could not be accessed, Exception if the timeout configuration is not set
	 */
	public RolesCache() throws Exception {
		CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		Map properties = new HashMap<>();
		properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
		rolesCache = cacheFactory.createCache(properties);
	}

	/**
	 * Access control for the singleton
	 * @return the created instance
	 * @throws Exception CacheException if the CacheFactory could not be accessed, Exception if the timeout configuration is not set
	 */
	public static synchronized RolesCache getInstance() throws Exception {
		if(me == null){
			me = new RolesCache();
		}
		return me;
	}

	/**
	 * Access to the cache element
	 * @return cache to be used to manage the authorization
	 */
	public Cache getRolesCache() {
		return rolesCache;
	}

	public Role getRole(long id) throws Exception {
		Role role = (Role) rolesCache.get(id);
		if(role == null) {
			role = Role.get(id);
			if (role != null) {
				rolesCache.put(id, role);
			} else {
				throw new InvalidValueException(String.format(Constants.INVALID_ID, id));
			}
		}
		return role;
	}

}
