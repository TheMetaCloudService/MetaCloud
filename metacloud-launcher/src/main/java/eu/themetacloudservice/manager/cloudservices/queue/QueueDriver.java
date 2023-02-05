package eu.themetacloudservice.manager.cloudservices.queue;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.events.dummys.processbased.ServiceDisconnectedEvent;
import eu.themetacloudservice.events.dummys.processbased.ServiceLaunchEvent;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.manager.cloudservices.enums.TaskedServiceStatus;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.timebaser.TimerBase;
import eu.themetacloudservice.timebaser.utils.TimeUtil;

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
                        CloudManager.serviceDriver.getService(service).handelStatusChange(TaskedServiceStatus.STARTED);
                        CloudManager.serviceDriver.getService(service).handelLaunch();
                        Driver.getInstance().getEventDriver().executeEvent(new ServiceLaunchEvent(service,   CloudManager.serviceDriver.getService(service).getEntry().getNode()));
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


