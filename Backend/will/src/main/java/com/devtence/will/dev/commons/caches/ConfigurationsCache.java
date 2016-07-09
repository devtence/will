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
 * if the USE_CACHE flag is set to TRUE the class uses Memcache, otherwise its go directly to the
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
     * The protected instance
     */
    protected static ConfigurationsCache me = null;

    /**
     * The Member Cache element using the JCache library
     */
    private Cache cache;

    private ConfigurationsCache() throws Exception {
        if(Constants.USE_CACHE) {
            initCache();
        }
    }

    /**
     * initializes the cache
     * @throws Exception CacheException if its not able to create the cache
     */
    private void initCache() throws Exception {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
        cache = cacheFactory.createCache(properties);
    }

    /**
     * returns configuration from cache if it doesnt exist return null value
     * @param key
     * @return
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
     * sets the confing in the cache using the key
     * @param key search parameter to find the configuration
     * @param value
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

    /**
     * returns the config, if it doesnt exist on the cache, it search for it on the Google Cloud Datastore
     * @param key
     * @return
     * @throws Exception InvalidValueException if the key doesnt exist on the persistance layer.
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
