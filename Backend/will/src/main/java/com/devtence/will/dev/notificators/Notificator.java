package com.devtence.will.dev.notificators;

import java.util.Map;

/**
 * Generic Notificator
 * Created by plessmann on 11/03/16.
 */
public abstract class Notificator {

	/**
	 * Method to send a notification using the parameters. A parameter must be the Notification to be sent and another parameter could be the targets of the notification
	 * @param parameters
	 */
	public abstract void notify(Map parameters);
}
