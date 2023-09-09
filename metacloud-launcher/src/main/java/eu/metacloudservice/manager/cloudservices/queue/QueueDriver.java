package eu.metacloudservice.manager.cloudservices.queue;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class QueueDriver {

    private final ConcurrentLinkedDeque<String> queue_startup;
    private final ConcurrentLinkedDeque<String> queue_shutdown;

    public QueueDriver() {
        this.queue_startup = new ConcurrentLinkedDeque<>();
        this.queue_shutdown = new ConcurrentLinkedDeque<>();
    }

    public ConcurrentLinkedDeque<String> getQueue_startup() {
        return queue_startup;
    }

    public ConcurrentLinkedDeque<String> getQueue_shutdown() {
        return queue_shutdown;
    }

    public void addQueuedObjectToStart(String service){
        if (!queue_startup.contains(service))
          this.queue_startup.add(service);
    }

    public void addQueuedObjectToShutdown(String service){
        if (!queue_shutdown.contains(service))
            this.queue_shutdown.add(service);
    }




}


