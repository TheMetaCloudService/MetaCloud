package eu.metacloudservice.manager.cloudservices.queue;

import java.util.LinkedList;

public class QueueDriver {

    private final LinkedList<String> queue_startup;
    private final LinkedList<String> queue_shutdown;

    public QueueDriver() {
        this.queue_startup = new LinkedList<>();
        this.queue_shutdown = new LinkedList<>();
    }

    public LinkedList<String> getQueue_startup() {
        return queue_startup;
    }

    public LinkedList<String> getQueue_shutdown() {
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


