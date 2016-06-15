package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.commons.Configuration;
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
public class ConfigurationsCache {

	/**
	 * The protected instance
	 */
	protected static ConfigurationsCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache cache;

	private boolean useCache;

	public ConfigurationsCache() throws Exception {
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

	protected Configuration getCacheElement(String key) throws Exception {
		Configuration element = null;
		useCache = Configuration.getBoolean("use-cache");
		if(useCache) {
			if(cache == null) {
				initCache();
			}
			element = (Configuration) cache.get(key);
		}
		return element;
	}

	protected void putCacheElement(String key, Configuration value) throws Exception{
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
	public static synchronized ConfigurationsCache getInstance() throws Exception {
		if(me == null){
			me = new ConfigurationsCache();
		}
		return me;
	}

	public Configuration getElement(String key) throws Exception {
		Configuration configuration = getCacheElement(key);
		if(configuration == null) {
			configuration = Configuration.getConfigByKey(key);
			if (configuration != null) {
				putCacheElement(key, configuration);
			} else {
				throw new InvalidValueException(String.format(Constants.INVALID_ID, key));
			}
		}
		return configuration;
	}
}
