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
	protected static RolesCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache cache;

	private boolean useCache;

	public RolesCache() throws Exception {
		useCache = Configuration.getBoolean("use-cache");
		if(useCache) {
			initCache();
		}
	}

	protected void initCache() throws Exception {
		CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		Map properties = new HashMap<>();
		properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
		cache = cacheFactory.createCache(properties);
	}

	protected Role getCacheElement(Long key) throws Exception {
		Role element = null;
		useCache = Configuration.getBoolean("use-cache");
		if(useCache) {
			if(cache == null) {
				initCache();
			}
			element = (Role) cache.get(key);
		}
		return element;
	}

	protected void putCacheElement(Long key, Role value) throws Exception{
		useCache = Configuration.getBoolean("use-cache");
		if(useCache) {
			if(cache == null) {
				initCache();
			}
			cache.put(key, value);
		}
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

	public Role getElement(Long key) throws Exception {
		Role role = getCacheElement(key);
		if(role == null) {
			role = Role.getById(key);
			if (role != null) {
				putCacheElement(key, role);
			} else {
				throw new InvalidValueException(String.format(Constants.INVALID_ID, key));
			}
		}
		return role;
	}


}
