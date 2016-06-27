package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.Client;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Singleton to manage the usage of memcache to store the clients created in the platform.
 *
 * Created by plessmann on 10/03/16.
 */
@SuppressWarnings("unchecked")
public class ClientsCache {

	/**
	 * The protected instance
	 */
	protected static ClientsCache me = null;

	/**
	 * The Member Cache element using the JCache library
	 */
	private Cache cache;

	private ClientsCache() throws Exception {
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

	private Client getCacheElement(Long key) throws Exception {
		Client element;
		if(Constants.USE_CACHE) {
			if(cache == null) {
				initCache();
			}
			element = (Client) cache.get(key);
		}
		return element;
	}

	private void putCacheElement(Long key, Client value) throws Exception{
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
	public static synchronized ClientsCache getInstance() throws Exception {
		if(me == null){
			me = new ClientsCache();
		}
		return me;
	}

	public Client getElement(Long key) throws Exception {
		Client client = getCacheElement(key);
		if(client == null) {
			client = Client.getById(key);
			if (client != null) {
				putCacheElement(key, client);
			} else {
				throw new InvalidValueException(String.format(Constants.INVALID_ID, key));
			}
		}
		return client;
	}

}
