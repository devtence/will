package com.devtence.will.dev.commons.authenticators;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.caches.AuthorizationCache;
import com.devtence.will.dev.commons.caches.ClientsCache;
import com.devtence.will.dev.commons.caches.RolesCache;
import com.devtence.will.dev.commons.wrappers.CacheAuthWrapper;
import com.devtence.will.dev.models.users.Client;
import com.devtence.will.dev.models.users.Permission;
import com.devtence.will.dev.models.users.Role;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.appengine.api.utils.SystemProperty;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authenticator class that implements the logic and methods to authenticate and validate Authorization keys and
 * Permissions that are received via http headers. Json Web Tokens (JWT) are used for the Auth keys and are generated
 * via the Authentication Web Service.
 *
 * All the classes that extends from BaseController will use this Authenticator as default unless is explicitly
 * changed.
 *
 * This class uses AuthorizationCache and ClientsCache to check the permissions and roles of the user or client
 * making the call.
 *
 * @author plessmann
 * @since 2016-03-09
 *
 * @see Authenticator
 * @see AuthorizationCache
 * @see ClientsCache
 *
 */
public class UserAuthenticator implements Authenticator {

    private static final Logger log = Logger.getLogger(UserAuthenticator.class.getName());


    /**
     * The JWT must be set as header using the AUTHORIZATION key and the user id must be set as a header using the
     * AUTHORIZATION_KEY. The AUTHORIZATION_KEY will be used to check the AuthorizationCache and the stored value is
     * then compared to the received JWT.
     *
     * @param request	the full received request
     * @return	null for an invalid access, an instanced user for allowed access
     */
    @Override
    public User authenticate(HttpServletRequest request) {
        User user;
        //checks the running environment
        if(SystemProperty.environment.value() == SystemProperty.Environment.Value.Development){
            //running on development uses generic value so you can test
            user = new User(Constants.GENERIC_KEY, Constants.GENERIC_USER);
        } else {
            //running on production (must contain actual headers)
            String idClient = request.getHeader(Constants.AUTHORIZATION_CLIENT);
            String pathTranslated = request.getPathTranslated();
            String token = request.getHeader(Constants.AUTHORIZATION);
            String key = request.getHeader(Constants.AUTHORIZATION_KEY);
            user = authProduction(idClient, pathTranslated, token, key);
        }
        return user;
    }

    /**
     * This method checks that the client and the user has the correct permission to access the route that is being
     * called. It checks that the client has the right permission and it checks if that permission requires a user,
     * if it does, it check that the user role has the permission. After those tests are passed it checks
     * for the validity of the JWT.
     *
     * @param idClient          id of the client that generates the call
     * @param pathTranslated    the route being accessed by the web call
     * @param token             the JWT that allows access to the platform
     * @param key               the user id to get the roles of the user and the secret to decrypt the JWT
     * @return                  if the tests passes, an instatianted user. null if the call is illegal or out of time.
     */
    public User authProduction(String idClient, String pathTranslated, String token, String key){
        User user = null;
        if (idClient != null && !idClient.isEmpty()) {
            try {
                Client client = ClientsCache.getInstance().getElement(Long.parseLong(idClient));
                if (client != null) {
                    Permission permission = new Permission(pathTranslated);
                    if (client.getPermissions().contains(permission)) {
                        int permissionIndex = client.getPermissions().indexOf(permission);
                        permission = client.getPermissions().get(permissionIndex);
                        if(permission.getUserRequired()) {
                            if (token != null && !token.isEmpty() && key != null && !key.isEmpty()) {
                                try {
                                    CacheAuthWrapper value = AuthorizationCache.getInstance().getElement(Long.parseLong(key));
                                    boolean valid = false;
                                    Role role ;
                                    for (Long roleKey : value.getRoles()) {
                                        role = RolesCache.getInstance().getElement(roleKey);
                                        if (role.getPermissions().contains(permission)) {
                                            valid = true;
                                            break;
                                        }
                                    }
                                    if (valid) {
                                        Jwts.parser().setSigningKey(value.getSecret()).parseClaimsJws(token);
                                        user =  new User(key, Constants.GENERIC_USER);
                                    }
                                } catch (Exception e) {
                                    log.log(Level.WARNING, Constants.ERROR, e);
                                }
                            }
                        } else {
                            user = new User(Constants.GENERIC_KEY, Constants.GENERIC_USER);
                        }
                    }
                }
            } catch (Exception e) {
                log.log(Level.WARNING, Constants.ERROR, e);
            }
        }
        return user;
    }
}

