package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
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
@SuppressWarnings("unchecked")
public abstract class BaseCache<K, V> {

	/**
	 * The protected instance
	 */
	protected static BaseCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache cache;

	public BaseCache() throws Exception {
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

	private V getCacheElement(K key) throws Exception {
		V element;
		if(Constants.USE_CACHE) {
			if(cache == null) {
				initCache();
			}
			element = (V) cache.get(key);
		}
		return element;
	}

	private void putCacheElement(K key, V value) throws Exception{
		if(Constants.USE_CACHE) {
			if(cache == null) {
				initCache();
			}
			cache.put(key, value);
		}
	}

	public abstract V getElement(K key) throws Exception;
}
