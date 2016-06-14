package com.devtence.will.dev.commons.caches;

import com.devtence.will.dev.models.commons.Configuration;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by plessmann on 14/06/16.
 */
public abstract class BaseCache<K, V> {

	/**
	 * The protected instance
	 */
	protected static BaseCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache cache;

	private boolean useCache;

	public BaseCache() throws Exception {
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

	protected V getCacheElement(K key) throws Exception {
		V element = null;
		useCache = Configuration.getBoolean("use-cache");
		if(useCache) {
			if(cache == null) {
				initCache();
			}
			element = (V) cache.get(key);
		}
		return element;
	}

	protected void putCacheElement(K key, V value) throws Exception{
		useCache = Configuration.getBoolean("use-cache");
		if(useCache) {
			if(cache == null) {
				initCache();
			}
			cache.put(key, value);
		}
	}

	public abstract V getElement(K key) throws Exception;
}
