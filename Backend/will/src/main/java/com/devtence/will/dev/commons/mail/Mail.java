package com.devtence.will.dev.commons.mail;

import java.util.List;

/**
 * Created by plessmann on 27/06/16.
 */
public abstract class Mail {

	/**
	 * Sends an email
	 * @param sender	the mail origin
	 * @param recipients	a list of targets
	 * @param subject	subject of the mail
	 * @param message	the body of the mail
	 */
	public abstract void sendMail(String sender, String recipients[], String subject, String message);

	/**
	 * Sends an email
	 * @param sender	the mail origin
	 * @param recipients	a list of targets
	 * @param subject	subject of the mail
	 * @param message	the body of the mail
	 */
	public abstract void sendMail(String sender, List<String> recipients, String subject, String message);

	/**
	 * Sends an email
	 * @param sender	the mail origin
	 * @param recipient	the target of the mail
	 * @param subject	subject of the mail
	 * @param message	the body of the mail
	 */
	public abstract void sendMail(String sender, String recipient, String subject, String message);

}
