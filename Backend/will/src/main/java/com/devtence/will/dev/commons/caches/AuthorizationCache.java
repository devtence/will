package com.devtence.will.dev.commons.caches;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.wrappers.CacheAuthWrapper;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.User;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

import javax.cache.Cache;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Singleton Cache class that stores the users permissions access and roles. It's used by the UserAuthenticator
 * to validate the permissions.
 *
 * if the USE_CACHE flag is set to TRUE in the Constants class, Memcached will be used, otherwise goes directly to the
 * Google Cloud Datastore.
 *
 * @author plessmann
 * @since 2016-03-10
 * @see com.devtence.will.dev.commons.authenticators.UserAuthenticator
 * @see CacheAuthWrapper
 *
 *
 */
@SuppressWarnings("unchecked")
public class AuthorizationCache {

    /**
     * The protected instance.
     */
    protected static AuthorizationCache me = null;

    /**
     * The Member Cache element using the JCache library.
     */
    private Cache cache;

    /**
     * Private constructor used to ensure there's only one instance of the object.
     * @throws Exception
     */
    private AuthorizationCache() throws Exception {
        if(Constants.USE_CACHE) {
            initCache();
        }
    }

    /**
     * Initializes the cache.
     * @throws Exception if there was an error initialising the cache.
     */
    private void initCache() throws Exception {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.HOURS.toSeconds(Configuration.getInt("cache-timeout")));
        cache = cacheFactory.createCache(properties);
    }

    /**
     * Gets a CacheAuthWrapper element by its key.
     * @param key id of the element
     * @return the cache object
     * @throws Exception in case an error occurs
     */
    private CacheAuthWrapper getCacheElement(Long key) throws Exception {
        CacheAuthWrapper element;
        if(Constants.USE_CACHE) {
            if(cache == null) {
                initCache();
            }
            element = (CacheAuthWrapper) cache.get(key);
        }
        return element;
    }

    /**
     * Sets a CacheAuthWrapper on the cache.
     * @param key to find the object
     * @param value CacheAuthWrapper that contains user information
     * @throws Exception in case it cant set access the cache
     */
    private void putCacheElement(Long key, CacheAuthWrapper value) throws Exception{
        if(Constants.USE_CACHE) {
            if(cache == null) {
                initCache();
            }
            cache.put(key, value);
        }
    }

    /**
     * Public access for the singleton.
     * @return The created/instantiated instance
     * @throws Exception CacheException if the CacheFactory could not be accessed, Exception if the timeout configuration is not set
     */
    public static synchronized AuthorizationCache getInstance() throws Exception {
        if(me == null){
            me = new AuthorizationCache();
        }
        return me;
    }

    /**
     * Checks the cache for the value that its being searched, if it doesn't exist it queries the
     * Google Cloud Datastore to try and find it, if it's found on the DB it'll be stored in the cache..
     * @param key to be found
     * @return
     * @throws Exception
     */
    public CacheAuthWrapper getElement(Long key) throws Exception {
        CacheAuthWrapper auth = getCacheElement(key);
        if(auth == null) {
            User user = User.getById(key);
            if (user != null) {
                if(user.getJwt() == null || user.getSecret() == null) {
                    throw new InvalidValueException(Constants.INVALID_JWT_OR_SECRET);
                }
                auth = new CacheAuthWrapper(user.getId(), user.getJwt(), user.getSecret(), user.getRoles());
                putCacheElement(key, auth);
            } else {
                throw new InvalidValueException(String.format(Constants.INVALID_ID, key));
            }
        }
        return auth;
    }

    public void setAuth(CacheAuthWrapper cacheAuthWrapper) throws Exception {
        putCacheElement(cacheAuthWrapper.getId(), cacheAuthWrapper);
    }

}
