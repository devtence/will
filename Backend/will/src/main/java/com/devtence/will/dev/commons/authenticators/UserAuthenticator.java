package com.devtence.will.dev.commons.authenticators;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.caches.AuthorizationCache;
import com.devtence.will.dev.commons.wrappers.CacheAuthWrapper;
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
		if(SystemProperty.environment.value() == SystemProperty.Environment.Value.Development){
			return new User("OK", "user@devtence.com");
		}
		String token = request.getHeader(Constants.AUTHORIZATION);
		String key = request.getHeader(Constants.AUTHORIZATION_KEY);
		if(token != null && !token.isEmpty() && key != null && !key.isEmpty()) {
			try {
				CacheAuthWrapper value = AuthorizationCache.getInstance().getAuth(Integer.parseInt(key));
				Jwts.parser().setSigningKey(value.getSecret()).parseClaimsJws(token);
				return new User("OK", "user@devtence.com");
			} catch (Exception e) {
				log.log(Level.WARNING, Constants.ERROR, e);
			}
		}
		return null;
	}

}

