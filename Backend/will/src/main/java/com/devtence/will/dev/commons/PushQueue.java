package com.devtence.will.dev.commons;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * Simple wrapper class for accessing the Google AppEngine TaskQueue, in order to create new queue methods it is required to
 * declare the queues in the queue.xml file inside the project. For more info on this please read
 * https://cloud.google.com/appengine/docs/java/taskqueue/push/creating-push-queues
 *
 * @author plessmann
 * @since 2016-04-01
 */
public class PushQueue {

    /**
     * Names for the queues.
     */
    private static final String MAIL_QUEUE = "mail";
    private static final String GCM_QUEUE = "gcm";

    /**
     * Inserts a task on the mail queue.
     * @param taskOptions contains various options for a task following the builder pattern
     */
    public static void enqueueMail(TaskOptions taskOptions) {
        //TODO: check the default configuration so this queue can be activated
//		Queue queue = QueueFactory.getQueue(MAIL_QUEUE);
//		queue.add(taskOptions);
        enqueueDefault(taskOptions);
    }

    /**
     * Inserts a task on the GCM Queue.
     * @param taskOptions contains various options for a task following the builder pattern
     */
    public static void enqueueGCM(TaskOptions taskOptions) {
        //TODO: check the default configuration so this queue can be activated
//		Queue queue = QueueFactory.getQueue(GCM_QUEUE);
//		queue.add(taskOptions);
        enqueueDefault(taskOptions);
    }

    /**
     * Inserts a task on the default task queue
     * @param taskOptions contains various options for a task following the builder pattern
     */
    public static void enqueueDefault(TaskOptions taskOptions) {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(taskOptions);
    }

}
