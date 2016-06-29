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
 * Created by plessmann on 27/06/16.
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

	protected static Mailgun me = null;

	public Mailgun() {
	}

	public static synchronized Mailgun getInstance() throws Exception {
		if(me == null){
			me = new Mailgun();
		}
		return me;
	}

	public void sendMail(String sender, String recipients[], String subject, String message) {
		sendMail(sender, Arrays.asList(recipients), subject, message);
	}

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
