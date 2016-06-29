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
 * Authenticator for the Endponts using a JWT generated via an auth endpoint
 * Created by plessmann on 09/03/16.
 */
public class UserAuthenticator implements Authenticator {

	private static final Logger log = Logger.getLogger(UserAuthenticator.class.getName());


	/**
	 * The JWT must be set as header using the AUTHORIZATION key and the user id must be set as a header using the AUTHORIZATION_KEY.
	 * The AUTHORIZATION_KEY will be used to check the AuthorizationCache and the stored value is then compared to the received JWT
	 *
	 * @param request	the full received request
	 * @return	null for an invalid access, an instanced user for allowed access
	 */
	@Override
    public User authenticate(HttpServletRequest request) {
		User user;
		if(SystemProperty.environment.value() == SystemProperty.Environment.Value.Development){
			user = new User(Constants.GENERIC_KEY, Constants.GENERIC_USER);
		} else {
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
	 * called. for this it checks that the clienet has the permissiona nd it check if that permission requires a user,
	 * if it does requires it it check that th user role has said permission. after thos tests are passed it checks
	 * for the validity of th JWT to grant a user to be checked.
	 *
	 * @param idClient          id of the client that generates the call
	 * @param pathTranslated    the route being accesed by the web call
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

