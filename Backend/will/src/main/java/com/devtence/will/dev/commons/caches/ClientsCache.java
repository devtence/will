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
 * Singleton Class that caches the Client's data. It's used by the UserAuthenticator
 * to validate the permissions.
 * <p>
 * If the USE_CACHE flag is set to TRUE in the Constants Class, Memcached will be used, otherwise its go directly to the
 * Google Could Datastore.
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
     * The protected instance for the singleton.
     */
    protected static ClientsCache me = null;

    /**
     * The Member Cache element using the JCache library.
     */
    private Cache cache;

    private ClientsCache() throws Exception {
        if(Constants.USE_CACHE) {
            initCache();
        }
    }

    /**
     * Cache initialization.
     *
     * @throws Exception if its not able to create the new cache
     */
    private void initCache() throws Exception {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
        cache = cacheFactory.createCache(properties);
    }

    /**
     * Returns the value of the key queried.
     *
     * @param key key queried
     * @return Client object
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
     * Creates/Updates the Client data for the key specified.
     *
     * @param key key to be created or updated
     * @param value value to be placed on the key position
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
     * Access control for the singleton.
     * @return the singleton instance object
     * @throws Exception CacheException if the CacheFactory could not be accessed, Exception if the timeout configuration is not set
     */
    public static synchronized ClientsCache getInstance() throws Exception {
        if(me == null){
            me = new ClientsCache();
        }
        return me;
    }

    /**
     * Queries the Client that belongs to the key provided, if the client is not in the cache,
     * it searches the Google Cloud Datastore.
     *
     * @param key key queried
     * @return the Client object in the key position
     * @throws Exception InvalidValueException if the client doesn't exist in the persistence layer
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
