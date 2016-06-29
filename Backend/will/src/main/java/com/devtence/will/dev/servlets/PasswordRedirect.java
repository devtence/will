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
 * This is used as a filter for Reset password requests, it simply receives a JWT that was generated when te Password Reset Process began
 * and when this is called it checks for the validity of the JWT to decide where to redirect the user
 * Created by plessmann on 23/03/16.
 */
public class PasswordRedirect extends HttpServlet {

	private static final Logger log = Logger.getLogger(PasswordRedirect.class.getName());
	public static final String PASSWORD_ERROR = "password_error";
	public static final String PASSWORD_VALID = "password_valid";

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

	public boolean processRequest(String token) {
		boolean error = true;
		UserPasswordReset passwordReset = null;
		User user;
		try {
			passwordReset = UserPasswordReset.getByToken(token);
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
