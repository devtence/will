package com.devtence.will.dev.commons;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by plessmann on 16/06/16.
 */
public class Mailgun {

	private static final Logger log = Logger.getLogger(Mailgun.class.getName());

	private static final String MAILGUN_DOMAIN_NAME = System.getenv("MAILGUN_DOMAIN_NAME");
	private static final String MAILGUN_API_KEY = System.getenv("MAILGUN_API_KEY");
	private static final String MAILGUN_URL = "https://api.mailgun.net/v3/%s/messages";
	public static final String TO = "to";
	public static final String FROM = "from";
	public static final String SUBJECT = "subject";
	public static final String TEXT = "text";
	public static final String API = "api";

	public static ClientResponse sendMail(String sender, String recipients[], String subject, String message) {
		return sendMail(sender, Arrays.asList(recipients), subject, message);
	}

	public static ClientResponse sendMail(String sender, List<String> recipients, String subject, String message) {
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
		return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
	}

	public static ClientResponse sendMail(String sender, String recipient, String subject, String message) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(API, MAILGUN_API_KEY));
		WebResource webResource = client.resource(String.format(MAILGUN_URL, MAILGUN_DOMAIN_NAME));
		MultivaluedMapImpl formData = new MultivaluedMapImpl();
		formData.add(FROM, sender);
		formData.add(TO, recipient);
		formData.add(SUBJECT, subject);
		formData.add(TEXT, message);
		return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
	}

}
