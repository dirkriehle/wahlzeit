package org.wahlzeit.agents;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.wahlzeit.model.Photo;

import java.util.logging.Logger;

import static com.google.appengine.api.taskqueue.RetryOptions.Builder.withTaskRetryLimit;

/**
 * Class to combine all calls for async task to use Task API from Google.
 * <p/>
 * Created by Lukas Hahmann on 12.05.15.
 */
public class AsyncTaskExecutor {

    private static final Logger log = Logger.getLogger(AsyncTaskExecutor.class.getName());

    /**
     * @methodtype command
     * <p/>
     * Starts a task in the default queue to save the photo with the specified ID.
     */
    public static void savePhotoAsync(String photoId) {
        log.info("Calling async push task to persist PhotoId " + photoId);
        Queue queue = QueueFactory.getDefaultQueue();
        RetryOptions retryOptions = withTaskRetryLimit(3);
        queue.add(TaskOptions.Builder.withUrl("/persistPhoto").param(Photo.ID, photoId).retryOptions(retryOptions));
    }
}
