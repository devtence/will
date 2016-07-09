package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.wrappers.CacheAuthWrapper;
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
 * Cache class that stores the Clients created in the platform. it is used by the UserAuthenticator
 * to validate the permissions.
 *
 * if the USE_CACHE flag is set to TRUE the class uses Memcache, otherwise its go directly to the
 * Google Could Datastore
 *
 * important note: this class uses the Singleton design pattern.
 *
 * @author plessmann
 * @since 2016-03-10
 * @see com.devtence.will.dev.commons.authenticators.UserAuthenticator
 * @see CacheAuthWrapper
 *
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

    /**
     * initializes the cache.
     * @throws Exception if its not able to create the new cache
     */
    private void initCache() throws Exception {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
        cache = cacheFactory.createCache(properties);
    }

    /**
     * returns cache element with the key that is being search
     * @param key
     * @return
     * @throws Exception
     */
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

    /**
     * sets the client with the key in the cache
     * @param key
     * @param value
     * @throws Exception
     */
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

    /**
     * returns the client that is being search, if the client is not in the cache, it searches the Google Cloud Datastore
     * @param key
     * @return
     * @throws Exception InvalidValueException if the client doesnt exist in the persistance layer
     */
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
