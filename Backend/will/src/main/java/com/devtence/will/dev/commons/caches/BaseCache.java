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
 * TODO: review this class usage and mark possible for refactor.
 * Abstract Class created to serve as the base for specific Cache implementations that use Memcached.
 * Defines basic methods that every cache must implement.
 *
 * @author plessmann
 * @since 2016-06-14
 * @see ConfigurationsCache
 */
@SuppressWarnings("unchecked")
public abstract class BaseCache<K, V> {

    /**
     * Protected instance for the singleton.
     */
    protected static BaseCache me = null;

    /**
     * The Member Cache element using the JCache library.
     */
    private Cache cache;

    public BaseCache() throws Exception {
        if(Constants.USE_CACHE) {
            initCache();
        }
    }

    /**
     * Cache initialization, sets the expiration time specified in the Configuration file.
     * @throws Exception
     */
    private void initCache() throws Exception {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
        cache = cacheFactory.createCache(properties);
    }

    /**
     * Returns the value of the element with the key searched
     * @param key item to search
     * @return the value that matches the key provided
     * @throws Exception
     */
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

    /**
     * Sets or updates the value at the key position.
     * @param key key to be added or updated
     * @param value value to be added or updated at the key position
     * @throws Exception
     */
    private void putCacheElement(K key, V value) throws Exception{
        if(Constants.USE_CACHE) {
            if(cache == null) {
                initCache();
            }
            cache.put(key, value);
        }
    }

    /**
     * Needs to be implemented to return the value of an object.
     * @param key
     * @return
     * @throws Exception
     */
    public abstract V getElement(K key) throws Exception;
}
