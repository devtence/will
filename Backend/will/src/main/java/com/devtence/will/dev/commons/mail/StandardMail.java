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
 * Class that extends from Mail that implements the basic functionality to send emails via the Google AppEngine API
 * this class is the default class to send emails, and it doesnt require any special configurations.
 *
 * important note: this class uses the singleton design pattern
 *
 * @author plessmann
 * @since 2016-06-27
 * @see Mail
 */
public class StandardMail extends Mail {

    private static final Logger log = Logger.getLogger(StandardMail.class.getName());

    /**
     * protected instance
     */
    protected static StandardMail me = null;

    public StandardMail() {
    }

    /**
     * returns the unique instance of the class, if it doesnt exist calls the constructor so it can be created
     * @return
     * @throws Exception
     */
    public static synchronized StandardMail getInstance() throws Exception {
        if(me == null){
            me = new StandardMail();
        }
        return me;
    }

    /**
     * Sends an email to an array of recipients
     * @param sender	the mail origin
     * @param recipients	a list of targets
     * @param subject	subject of the mail
     * @param message	the body of the mail
     */
    public void sendMail(String sender, String recipients[], String subject, String message) {
        sendMail(sender, Arrays.asList(recipients), subject, message);
    }

    /**
     * Sends an email to a list of recipients
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
     * Sends an email to a recipient
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
