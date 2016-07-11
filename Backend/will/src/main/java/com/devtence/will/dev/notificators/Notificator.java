package com.devtence.will.dev.notificators;

import java.util.Map;

/**
 * Very simple Abstract Class for processing notification Tasks out of the Google Cloud Task Queue.
 *
 * @author plessmann
 * @since 2016-03-11
 */
public abstract class Notificator {

	/**
	 * Method for sending a notification using the parameters. A parameter must be the Notification to be sent
	 * and another parameter could be the targets of the notification.
	 */
	public abstract void notify(Map parameters);
}
