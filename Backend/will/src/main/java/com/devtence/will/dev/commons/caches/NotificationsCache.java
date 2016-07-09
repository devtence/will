package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.commons.Notification;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Cache class that stores the different Notifications created in the platform.
 *
 * if the USE_CACHE flag is set to TRUE the class uses Memcache, otherwise its go directly to the
 * Google Could Datastore
 *
 * important note: this class uses the Singleton design pattern.
 *
 * @author plessmann
 * @since 2016-03-10
 * @see Notification
 *
 */
@SuppressWarnings("unchecked")
public class NotificationsCache {

    /**
     * The protected instance
     */
    protected static NotificationsCache me = null;

    /**
     * The Member Cache element using the JCache library
     */
    private Cache cache;

    private NotificationsCache() throws Exception {
        if(Constants.USE_CACHE) {
            initCache();
        }
    }

    /**
     * initializes the cache.
     * @throws Exception
     */
    private void initCache() throws Exception {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
        cache = cacheFactory.createCache(properties);
    }

    /**
     * returns the element from the cache.
     * @param key
     * @return
     * @throws Exception
     */
    private Notification getCacheElement(String key) throws Exception {
        Notification element;
        if(Constants.USE_CACHE) {
            if(cache == null) {
                initCache();
            }
            element = (Notification) cache.get(key);
        }
        return element;
    }

    /**
     * sets the notification on the cache
     * @param key
     * @param value
     * @throws Exception
     */
    private void putCacheElement(String key, Notification value) throws Exception{
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
    public static synchronized NotificationsCache getInstance() throws Exception {
        if(me == null){
            me = new NotificationsCache();
        }
        return me;
    }

    /**
     * returns the element thats being searched, if its not on the cache it searches for it on the persistance layer
     * if its not found an Exception is thrown
     * @param key
     * @return
     * @throws Exception
     */
    public Notification getElement(String key) throws Exception {
        Notification notification = getCacheElement(key);
        if(notification == null) {
            notification = Notification.getByMnemonic(key);
            if (notification != null) {
                putCacheElement(key, notification);
            } else {
                throw new InvalidValueException(String.format(Constants.INVALID_KEY, key));
            }
        }
        return notification;
    }

}
