package com.devtence.will.dev.commons;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * Simple wrapper class for accessing the Google AppEngine TaskQueue, in order to create new queue methods it is required to
 * declare the queues in the queue.xml for more info on this please see
 * https://cloud.google.com/appengine/docs/java/taskqueue/push/creating-push-queues
 *
 * @author plessmann
 * @since 2016-04-01
 */
public class PushQueue {

    private static final String MAIL_QUEUE = "mail";
    private static final String GCM_QUEUE = "gcm";

    /**
     * insert task on the mail queue
     * @param taskOptions
     */
    public static void enqueueMail(TaskOptions taskOptions) {
        //TODO: check the default configuration so this queue can be activated
//		Queue queue = QueueFactory.getQueue(MAIL_QUEUE);
//		queue.add(taskOptions);
        enqueueDefault(taskOptions);
    }

    /**
     * inserts task on the GCM Queue
     * @param taskOptions
     */
    public static void enqueueGCM(TaskOptions taskOptions) {
        //TODO: check the default configuration so this queue can be activated
//		Queue queue = QueueFactory.getQueue(GCM_QUEUE);
//		queue.add(taskOptions);
        enqueueDefault(taskOptions);
    }

    /**
     * inserts task on the default task queue
     * @param taskOptions task to insert on the default queue
     */
    public static void enqueueDefault(TaskOptions taskOptions) {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(taskOptions);
    }

}
