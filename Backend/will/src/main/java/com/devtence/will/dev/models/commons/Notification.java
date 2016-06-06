package com.devtence.will.dev.models.commons;


import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.devtence.will.dev.models.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Notifications to be sent to different targets. Mostly used via the Notificators
 * Created by plessmann on 11/03/16.
 */
@Entity
public class Notification extends BaseModel<Notification> implements Serializable{

	private String sender;
	private String recipients;
	private String subject;
	private String message;
	@Index
	private String mnemonic;

	public Notification() {
	}

	public Notification(String sender, String recipients, String subject, String message) {
		this.sender = sender;
		this.recipients = recipients;
		this.subject = subject;
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	@Override
	public void validate() throws Exception {
		if(subject == null || subject.isEmpty()){
			throw new MissingFieldException("invalid subject");
		}
		if(message == null || message.isEmpty()){
			throw new MissingFieldException("invalid message");
		}
		if(mnemonic == null || mnemonic.isEmpty()){
			throw new MissingFieldException("invalid mnemonic");
		}
		this.save();
	}

	@Override
	public void destroy() throws Exception {

	}

	@Override
	public void update(Notification data) throws Exception {

	}

	public static Notification getById(Long id) throws Exception {
		return DbObjectify.ofy().load().type(Notification.class).id(id).now();
	}

	/**
	 * Query the database using the mnemonic as parameter
	 * @param mnemonic	mnemonic to search
	 * @return	the associated DigitalContentStatus data
	 * @throws Exception	an error ocurred
	 */
	public static Notification getByMnemonic(String mnemonic) throws Exception {
		return DbObjectify.ofy().load().type(Notification.class).filter("mnemonic", mnemonic).first().now();
	}
}
