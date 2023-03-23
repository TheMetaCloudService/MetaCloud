package eu.metacloudservice.manager.cloudservices.queue;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.LinkedList;
import java.util.TimerTask;

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
        this.queue_startup.add(service);
    }

    public void addQueuedObjectToShutdown(String service){
        this.queue_shutdown.add(service);
    }




}


