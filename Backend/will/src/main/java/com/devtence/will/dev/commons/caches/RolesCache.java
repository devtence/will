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
 * Class that caches the permission Roles. These roles specify the user's permissions on the platform
 * so they are needed to get fast access to this information.
 * <p>
 * This class is used by the UserAuthenticator for different validations during the authentication's permission check.
 * <p>
 * if the USE_CACHE flag is set to TRUE on the Constants class, Memcached is used, otherwise it'll query the
 * Google Could Datastore.
 *
 * @author plessmann
 * @since 2016-03-10
 *
 * @see com.devtence.will.dev.commons.authenticators.UserAuthenticator
 *
 */
@SuppressWarnings("unchecked")
public class RolesCache {

    /**
     * The protected instance for the singleton.
     */
    protected static RolesCache me = null;

    /**
     * The Member Cache element using the JCache library.
     */
    private Cache cache;

    private RolesCache() throws Exception {
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
     * @return Role object
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
     * Creates/Updates the Role data for the key specified.
     *
     * @param key key to be created or updated
     * @param value value to be placed on the key position
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
     * Access control for the singleton.
     * @return the singleton instance object
     * @throws Exception CacheException if the CacheFactory could not be accessed, Exception if the timeout configuration is not set
     */
    public static synchronized RolesCache getInstance() throws Exception {
        if(me == null){
            me = new RolesCache();
        }
        return me;
    }

    /**
     * Queries the Role that belongs to the key provided, if the Role is not on the cache,
     * it searches the Google Cloud Datastore.
     *
     * @param key key queried
     * @return the Role object in the key position
     * @throws Exception InvalidValueException if the client doesn't exist in the persistence layer
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
