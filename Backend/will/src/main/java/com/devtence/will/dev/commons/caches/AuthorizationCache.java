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
@SuppressWarnings("unchecked")
public class AuthorizationCache {

	/**
	 * The protected instance
	 */
	protected static AuthorizationCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache cache;

	private AuthorizationCache() throws Exception {
		if(Constants.USE_CACHE) {
			initCache();
		}
	}

	private void initCache() throws Exception {
		CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		Map properties = new HashMap<>();
		properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
		cache = cacheFactory.createCache(properties);
	}

	private CacheAuthWrapper getCacheElement(Long key) throws Exception {
		CacheAuthWrapper element;
		if(Constants.USE_CACHE) {
			if(cache == null) {
				initCache();
			}
			element = (CacheAuthWrapper) cache.get(key);
		}
		return element;
	}

	private void putCacheElement(Long key, CacheAuthWrapper value) throws Exception{
		if(Constants.USE_CACHE) {
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
	public static synchronized AuthorizationCache getInstance() throws Exception {
		if(me == null){
			me = new AuthorizationCache();
		}
		return me;
	}

	public CacheAuthWrapper getElement(Long key) throws Exception {
		CacheAuthWrapper auth = getCacheElement(key);
		if(auth == null) {
			User user = User.getById(key);
			if (user != null) {
				if(user.getJwt() == null || user.getSecret() == null) {
					throw new InvalidValueException(Constants.INVALID_JWT_OR_SECRET);
				}
				auth = new CacheAuthWrapper(user.getId(), user.getJwt(), user.getSecret(), user.getRoles());
				putCacheElement(key, auth);
			} else {
				throw new InvalidValueException(String.format(Constants.INVALID_ID, key));
			}
		}
		return auth;
	}

	public void setAuth(CacheAuthWrapper cacheAuthWrapper) throws Exception {
		putCacheElement(cacheAuthWrapper.getId(), cacheAuthWrapper);
	}

}
