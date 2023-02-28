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
        //Todo: fix the howl QueueSystem because they crashed the system

    }

    public void addQueuedObjectToStart(String service){
        this.queue_startup.add(service);
    }

    public void addQueuedObjectToShutdown(String service){
        this.queue_shutdown.add(service);
    }


    public void handler(){
        TimerBase base = new TimerBase();
         base.schedule(new TimerTask() {
             @Override
             public void run() {
                try {

                    if (!queue_startup.isEmpty()){
                        String service = queue_startup.removeFirst();
                        CloudManager.serviceDriver.getService(service).handelStatusChange(ServiceState.STARTED);
                        CloudManager.serviceDriver.getService(service).handelLaunch();
                    }else if (!queue_shutdown.isEmpty()){
                        String service = queue_shutdown.removeFirst();
                        CloudManager.serviceDriver.getService(service).handelQuit();
                        CloudManager.serviceDriver.unregistered(service);
                    }
                }catch (Exception ignored){}

             }
         }, 0, 1, TimeUtil.MILLISECONDS);
    }

}


