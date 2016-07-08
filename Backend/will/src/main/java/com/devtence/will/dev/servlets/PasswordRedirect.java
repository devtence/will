package com.devtence.will.dev.servlets;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.caches.ConfigurationsCache;
import com.devtence.will.dev.models.users.User;
import com.devtence.will.dev.models.users.UserPasswordReset;
import io.jsonwebtoken.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class implements the validation process for the Reset password requests, it simply receives a JWT that was
 * generated when the Password Reset Process began and implements the necessary methods to decide where to redirect the user.
 *
 * @author  plessmann
 * @since 2016-03-23
 *
 * @see com.devtence.will.dev.commons.wrappers.AuthorizationWrapper
 * @see UserPasswordReset
 * @see com.devtence.will.dev.notificators.UserPasswordRecovery
 */
public class PasswordRedirect extends HttpServlet {

	private static final Logger log = Logger.getLogger(PasswordRedirect.class.getName());
	public static final String PASSWORD_ERROR = "password_error";
	public static final String PASSWORD_VALID = "password_valid";

	/**
	 * Receives a request and get the JWT token from the HTTP Request parameters and validates if it's valid.
	 * Returns a String message.
     */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			String token = request.getParameter(Constants.TOKEN);
			boolean error = processRequest(token);
			response.sendRedirect(error ? ConfigurationsCache.getInstance().getElement(PASSWORD_ERROR).getValue() : ConfigurationsCache.getInstance().getElement(PASSWORD_VALID).getValue());
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		}
	}

	/**
	 * Receives a token and validates if the JWT token belongs to the same user.
	 *
	 * @param token JWT token received by the Servlet
	 * @return true if the token is not valid and false if it passed the validity test
     */
	public boolean processRequest(String token) {
		boolean error = true;
		UserPasswordReset passwordReset = null;
		User user;
		try {
			// Search the DB for the user authentication data
			passwordReset = UserPasswordReset.getByToken(token);

			// Verify the validity of the token
			if (passwordReset != null) {
				Jws<Claims> claimsJws = Jwts.parser().setSigningKey(passwordReset.getSecret()).parseClaimsJws(token);
				Claims body = claimsJws.getBody();
				user = User.getById(passwordReset.getIdUser());
				if (user != null) {
					if (body.getSubject().equals(user.getUser())) {
						user.setPasswordRecoveryStatus(3);
						user.update();
						passwordReset.destroy();
						error = false;
					}
				}
			}
		} catch (SignatureException e) {
			try {passwordReset.destroy();} catch (Exception ex){}
			log.log(Level.WARNING, Constants.ERROR, e);
		} catch (ExpiredJwtException e) {
			try {passwordReset.destroy();} catch (Exception ex){}
			log.log(Level.WARNING, Constants.ERROR, e);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		}
		return error;
	}

}
