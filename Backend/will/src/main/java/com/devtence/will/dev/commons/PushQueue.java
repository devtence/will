package com.devtence.will.dev.commons;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * This is a Simple wrpper for accessing the TaskQueue, in order to create new queue methods it is required to
 * declare the queues in the queue.xml (https://cloud.google.com/appengine/docs/java/taskqueue/push/creating-push-queues)
 *
 * Created by plessmann on 01/04/16.
 */
public class PushQueue {

	private static final String MAIL_QUEUE = "mail";
	private static final String GCM_QUEUE = "gcm";

	public static void enqueueMail(TaskOptions taskOptions) {
//		Queue queue = QueueFactory.getQueue(MAIL_QUEUE);
//		queue.add(taskOptions);
		enqueueDefault(taskOptions);
	}

	public static void enqueueGCM(TaskOptions taskOptions) {
//		Queue queue = QueueFactory.getQueue(GCM_QUEUE);
//		queue.add(taskOptions);
		enqueueDefault(taskOptions);
	}

	public static void enqueueDefault(TaskOptions taskOptions) {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(taskOptions);
	}

}
