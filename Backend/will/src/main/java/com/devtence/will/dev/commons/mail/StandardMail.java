package com.devtence.will.dev.commons.mail;

import com.devtence.will.Constants;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by plessmann on 27/06/16.
 */
public class StandardMail extends Mail {

	private static final Logger log = Logger.getLogger(StandardMail.class.getName());

	protected static StandardMail me = null;

	public StandardMail() {
	}

	public static synchronized StandardMail getInstance() throws Exception {
		if(me == null){
			me = new StandardMail();
		}
		return me;
	}

	/**
	 * Sends an email
	 * @param sender	the mail origin
	 * @param recipients	a list of targets
	 * @param subject	subject of the mail
	 * @param message	the body of the mail
	 */
	public void sendMail(String sender, String recipients[], String subject, String message) {
		sendMail(sender, Arrays.asList(recipients), subject, message);
	}

	/**
	 * Sends an email
	 * @param sender	the mail origin
	 * @param recipients	a list of targets
	 * @param subject	subject of the mail
	 * @param message	the body of the mail
	 */
	public void sendMail(String sender, List<String> recipients, String subject, String message) {
		log.warning("Enviando " + subject + " a " + (recipients == null ? 0 : recipients.size()) + " desde " + sender);
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			for (String recipient : recipients) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			}
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
		} catch (AddressException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		} catch (MessagingException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		}
	}

	/**
	 * Sends an email
	 * @param sender	the mail origin
	 * @param recipient	the target of the mail
	 * @param subject	subject of the mail
	 * @param message	the body of the mail
	 */
	public void sendMail(String sender, String recipient, String subject, String message) {
		log.warning("Enviando " + subject + " a " + recipient + " desde " + sender);
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
		} catch (AddressException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		} catch (MessagingException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		}
	}
}