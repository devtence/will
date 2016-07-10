package com.devtence.will.dev.commons.mail;

import com.devtence.will.Constants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class that extends from Mail to implement the integration with the Mailgun Email Service API.
 *
 * @author plessmann
 * @since 2016-06-27
 *
 * @see Mail
 *
 */
public class Mailgun extends Mail {

    private static final String MAILGUN_DOMAIN_NAME = System.getenv("MAILGUN_DOMAIN_NAME");
    private static final String MAILGUN_API_KEY = System.getenv("MAILGUN_API_KEY");
    private static final String MAILGUN_URL = "https://api.mailgun.net/v3/%s/messages";
    private static final String TO = "to";
    private static final String FROM = "from";
    private static final String SUBJECT = "subject";
    private static final String TEXT = "text";
    private static final String API = "api";
    private static final Logger log = Logger.getLogger(Mailgun.class.getName());

    /**
     * Protected singleton instance.
     */
    protected static Mailgun me = null;

    /**
     * Constructor.
     */
    public Mailgun() {
    }

    /**
     * Access control for the singleton.
     * @return the singleton instance object
     * @throws Exception
     */
    public static synchronized Mailgun getInstance() throws Exception {
        if(me == null){
            me = new Mailgun();
        }
        return me;
    }

    /**
     * Sends an email to an array of recipients.
     * @param sender    the mail's origin
     * @param recipients    an array of recipients
     * @param subject    subject of the mail
     * @param message    the body of the mail
     */
    public void sendMail(String sender, String recipients[], String subject, String message) {
        sendMail(sender, Arrays.asList(recipients), subject, message);
    }

    /**
     * Sends an email to a list of recipients.
     * @param sender    the mail's origin
     * @param recipients    a list of recipients
     * @param subject    subject of the mail
     * @param message    the body of the mail
     */
    public void sendMail(String sender, List<String> recipients, String subject, String message) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(API, MAILGUN_API_KEY));
        WebResource webResource = client.resource(String.format(MAILGUN_URL, MAILGUN_DOMAIN_NAME));
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add(FROM, sender);
        for (String recipient : recipients) {
            formData.add(TO, recipient);
        }
        formData.add(SUBJECT, subject);
        formData.add(TEXT, message);
        ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
        if(clientResponse.getStatus() != 200){
            log.log(Level.WARNING, Constants.ERROR, clientResponse.getStatus());
        }
    }

    /**
     * Sends an email to a single recipient.
     * @param sender    the mail's origin
     * @param recipient    the recipient of the mail
     * @param subject    subject of the mail
     * @param message    the body of the mail
     */
    public void sendMail(String sender, String recipient, String subject, String message) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(API, MAILGUN_API_KEY));
        WebResource webResource = client.resource(String.format(MAILGUN_URL, MAILGUN_DOMAIN_NAME));
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add(FROM, sender);
        formData.add(TO, recipient);
        formData.add(SUBJECT, subject);
        formData.add(TEXT, message);
        ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
        if(clientResponse.getStatus() != 200){
            log.log(Level.WARNING, Constants.ERROR, clientResponse.getStatus());
        }
    }

}
