package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.Role;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Cache class that stores the different Roles created in the platform. this roles contains the user permissions
 * so they are needed to get fast access to this information.
 * 
 * this class is used by the UserAuthentication for diferent validations during the permission check
 *
 * if the USE_CACHE flag is set to TRUE the class uses Memcache, otherwise its go directly to the
 * Google Could Datastore
 *
 * important note: this class uses the Singleton design pattern.
 *
 * @author plessmann
 * @since 2016-03-10
 *
 */
@SuppressWarnings("unchecked")
public class RolesCache {

    /**
     * The protected instance
     */
    protected static RolesCache me = null;

    /**
     * The Member Cache element using the JCache library
     */
    private Cache cache;

    private RolesCache() throws Exception {
        if(Constants.USE_CACHE) {
            initCache();
        }
    }

    /**
     * initializes the role cache
     * @throws Exception
     */
    private void initCache() throws Exception {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
        cache = cacheFactory.createCache(properties);
    }

    /**
     * returns the role from the cache if exists
     * @param key
     * @return
     * @throws Exception
     */
    private Role getCacheElement(Long key) throws Exception {
        Role element;
        if(Constants.USE_CACHE) {
            if(cache == null) {
                initCache();
            }
            element = (Role) cache.get(key);
        }
        return element;
    }

    /**
     * puts the role in the cache
     * @param key
     * @param value
     * @throws Exception
     */
    private void putCacheElement(Long key, Role value) throws Exception{
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
    public static synchronized RolesCache getInstance() throws Exception {
        if(me == null){
            me = new RolesCache();
        }
        return me;
    }

    /**
     * return the Role, if the role doesnt exist on the cache it searches for it on the persistance layer
     * @param key
     * @return
     * @throws Exception InvalidValueException if the role doesnt exist
     */
    public Role getElement(Long key) throws Exception {
        Role role = getCacheElement(key);
        if(role == null) {
            role = Role.getById(key);
            if (role != null) {
                putCacheElement(key, role);
            } else {
                throw new InvalidValueException(String.format(Constants.INVALID_ID, key));
            }
        }
        return role;
    }
}
