package com.devtence.will.dev.notificators;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.caches.ConfigurationsCache;
import com.devtence.will.dev.commons.caches.NotificationsCache;
import com.devtence.will.dev.commons.mail.StandardMail;
import com.devtence.will.dev.models.commons.Notification;
import com.devtence.will.dev.models.users.User;
import com.devtence.will.dev.models.users.UserPasswordReset;
import com.google.appengine.api.utils.SystemProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class processes the notifications in the Task Queue that intend to help the user with the Password Rest process
 *
 * @author plessmann
 * @since 2016-03-22
 */
public class UserPasswordRecovery extends Notificator {

	public static final String URL = "%URL%";
	private static final Logger log = Logger.getLogger(UserPasswordRecovery.class.getName());
	public static final String PASSWORD_REDIRECT_SERVLET = "password_redirect_servlet";
	public static final int TOKEN_VALID_OFFSET = 60 * 60000;

	/**
	 * Method that receives the parameters of the Task pulled out of the Task Queue
     */
	@Override
	public void notify(Map parameters) {
		try {
			long id = Long.parseLong(((String[])parameters.get(Constants.ID))[0]);
			String notificationMnemonic = ((String[])parameters.get(Constants.NOTIFICATION_MNEMONIC))[0];
			User user = User.getById(id);
			if(user != null) {
				if(user.getPasswordRecoveryStatus() == 1) {
					Notification notification = NotificationsCache.getInstance().getElement(notificationMnemonic);
					if (notification != null) {
						// Generate key
						Key key = MacProvider.generateKey();
						String secret = Base64.encodeBase64String(key.getEncoded());
						// Generate JWT
						String webToken = Jwts.builder().setSubject(user.getUser()).setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_OFFSET)).signWith(SignatureAlgorithm.HS512, key).compact();
						UserPasswordReset passwordReset = new UserPasswordReset(id, webToken, secret);
						// Store password on Google Cloud Datastore
						passwordReset.validate();
						// Set user registry status
						user.setPasswordRecoveryStatus(2);
						user.update();
						String passwordRedirectServlet = ConfigurationsCache.getInstance().getElement(PASSWORD_REDIRECT_SERVLET).getValue();
						if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
							System.out.println(String.format(passwordRedirectServlet, webToken));
						}
						//TODO: adjust message
						String message = notification.getMessage().replaceAll(URL, String.format(passwordRedirectServlet, webToken));
						StandardMail.getInstance().sendMail(notification.getSender(), user.getEmail(), notification.getSubject(), message);
					}
				}
			}
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		}
	}
}
