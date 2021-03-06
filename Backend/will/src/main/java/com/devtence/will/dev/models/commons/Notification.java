package com.devtence.will.dev.models.commons;


import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.devtence.will.dev.models.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Class that models the Notifications data and map it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Notifications.
 * <p>
 * This objects are to be sent to different targets and will be used via the Notificators classes
 *
 * @author plessmann
 * @since 2015-03-11
 * @see com.devtence.will.dev.commons.caches.ConfigurationsCache
 *
 */
@Entity
public class Notification extends BaseModel<Notification> implements Serializable{

    private String sender;

    private String recipients;

    private String subject;

    private String message;

    /**
     * String identifier for the notification type
     */
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

    public Notification(String sender, String recipients, String subject, String message, String mnemonic) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.message = message;
        this.mnemonic = mnemonic;
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

    /**
     * Validates and Adds a new Notification to the Database
     * @throws Exception
     */
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

    /**
     * Removes the notification from the database
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * Updates the Notification withe the new data received, only performs the operation if the data is
     * different from the old data.
     * @param data data to be updated on the persistence layer.
     * @throws Exception
     */
    @Override
    public void update(Notification data) throws Exception {
        boolean mod = false;

        if (data.getSender() != null){
            setSender(data.getSender());
            mod |= true;
        }

        if (data.getSubject() != null){
            setSubject(data.getSubject());
            mod |= true;
        }

        if (data.getRecipients() != null){
            setRecipients(data.getRecipients());
            mod |= true;
        }

        if (data.getMnemonic() != null){
            setMnemonic(data.getMnemonic());
            mod |= true;
        }

        if (data.getMessage() != null){
            setMessage(data.getMessage());
            mod |= true;
        }

        if (mod){
            this.validate();
        }
    }

    /**
     * Loads the object with the Data that matches the id.
     * @param id
     */
    @Override
    public void load(long id) {
        Notification me = DbObjectify.ofy().load().type(Notification.class).id(id).now();
        this.setId(me.getId());
        this.setSender(me.getSender());
        this.setRecipients(me.getRecipients());
        this.setSubject(me.getSubject());
        this.setMessage(me.getMessage());
    }

    /**
     * Returns the notification object queried by id
     * @param id
     * @return
     * @throws Exception
     */
    public static Notification getById(Long id) throws Exception {
        return DbObjectify.ofy().load().type(Notification.class).id(id).now();
    }

    /**
     * Queries the database using the mnemonic as parameter
     * @param mnemonic	mnemonic to search
     * @return	the associated DigitalContentStatus data
     * @throws Exception	an error ocurred
     */
    public static Notification getByMnemonic(String mnemonic) throws Exception {
        return DbObjectify.ofy().load().type(Notification.class).filter("mnemonic", mnemonic).first().now();
    }

    /**
     * Returns a String representation of the object
     * @return
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{\"id\":").append(getId())
                .append(",\"sender\":\"").append(sender)
                .append("\",\"recipients\":\"").append(recipients)
                .append("\",\"subject\":\"").append(subject)
                .append("\",\"message\":\"").append(message).append("\"}");
        return result.toString();
    }
}
