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
 * Cache class that stores the configurations created in the platform. it is used by the different classes
 * to access global configurations values that can change in real time
 *
 * if the USE_CACHE flag is set to TRUE the class uses Memcached, otherwise its go directly to the
 * Google Could Datastore
 *
 * important note: this class uses the Singleton design pattern.
 *
 * @author plessmann
 * @since 2016-03-10
 * @see Configuration
 *
 */
@SuppressWarnings("unchecked")
public class ConfigurationsCache {

    /**
     * The protected instance for the singleton.
     */
    protected static ConfigurationsCache me = null;

    /**
     * The Member Cache element using the JCache library.
     */
    private Cache cache;

    private ConfigurationsCache() throws Exception {
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
     * Returns the Configuration object of the key queried.
     *
     * @param key key queried
     * @return Configuration object
     * @throws Exception
     */
    private Configuration getCacheElement(String key) throws Exception {
        Configuration element;
        if(Constants.USE_CACHE) {
            if(cache == null) {
                initCache();
            }
            element = (Configuration) cache.get(key);
        }
        return element;
    }

    /**
     * Creates/Updates the Configuration data for the key specified.
     *
     * @param key key to be created or updated
     * @param value value to be placed on the key position
     * @throws Exception
     */
    private void putCacheElement(String key, Configuration value) throws Exception{
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
    public static synchronized ConfigurationsCache getInstance() throws Exception {
        if(me == null){
            me = new ConfigurationsCache();
        }
        return me;
    }

    /**
     * Queries the Configuration that belongs to the key provided, if the client is not in the cache,
     * it searches the Google Cloud Datastore.
     *
     * @param key key queried
     * @return the Configuration object in the key position
     * @throws Exception InvalidValueException if the Config doesn't exist in the persistence layer
     */
    public Configuration getElement(String key) throws Exception {
        Configuration configuration = getCacheElement(key);
        if(configuration == null) {
            configuration = Configuration.getConfigByKey(key);
            if (configuration != null) {
                putCacheElement(key, configuration);
            } else {
                throw new InvalidValueException(String.format(Constants.INVALID_KEY, key));
            }
        }
        return configuration;
    }
}
