package com.devtence.will.dev.commons.mail;

import java.util.List;

/**
 * Abstract class created to serve as the base for all the mail implementations.
 *
 * @author plessmann
 * @since 2016-06-27
 *
 */
public abstract class Mail {

    /**
     * needs to be implemented to Sends an email to an array multiple recipients
     * @param sender	the mail origin
     * @param recipients	a list of targets
     * @param subject	subject of the mail
     * @param message	the body of the mail
     */
    public abstract void sendMail(String sender, String recipients[], String subject, String message);

    /**
     * needs to be implemented to Sends an email to a list of multiple recipients
     * @param sender	the mail origin
     * @param recipients	a list of targets
     * @param subject	subject of the mail
     * @param message	the body of the mail
     */
    public abstract void sendMail(String sender, List<String> recipients, String subject, String message);

    /**
     * needs to be implemented to Sends an email to one recipient
     * @param sender	the mail origin
     * @param recipient	the target of the mail
     * @param subject	subject of the mail
     * @param message	the body of the mail
     */
    public abstract void sendMail(String sender, String recipient, String subject, String message);

}
