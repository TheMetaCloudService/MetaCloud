package eu.themetacloudservice.manager.queue;

import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.timebaser.TimerBase;
import eu.themetacloudservice.timebaser.utils.TimeUtil;

import java.util.*;
import java.util.stream.Collectors;

public class QueueDriver {

    private Queue<String> queue_startup;
    private Queue<String> queue_shutdown;

    public QueueDriver() {
        this.queue_startup = new LinkedList<>();
        this.queue_shutdown = new LinkedList<>();

    }

    public void addQueuedObjectToStart(String service){
        this.queue_startup.add(service);
    }

    public void addQueuedObjectToShutdown(String service){
        this.queue_shutdown.add(service);
    }

    public List<String> getServicesInStartQueue(){
        return  queue_startup.stream().collect(Collectors.toList());
    }


    public List<String> getServicesInStartQueue(String group){
        return  queue_startup.stream().filter(s -> s.startsWith(group)).collect(Collectors.toList());
    }


    public List<String> getServicesInStopQueue(){
        return  queue_shutdown.stream().collect(Collectors.toList());
    }


    public List<String> getServicesInStopQueue(String group){
        return  queue_shutdown.stream().filter(s -> s.startsWith(group)).collect(Collectors.toList());
    }



    public void handler(){
        TimerBase base = new TimerBase();
         base.schedule(new TimerTask() {
             @Override
             public void run() {

                 if (!queue_startup.isEmpty()){
                     String service = queue_startup.poll();
                     CloudManager.serviceDriver.getService(service).handelLaunch();
                 }
                 if (!queue_shutdown.isEmpty()){
                     String service = queue_shutdown.poll();
                     CloudManager.serviceDriver.getService(service).handelQuit();
                     CloudManager.serviceDriver.unregister(service);
                 }

             }
         }, 0, 10, TimeUtil.SECONDS);
    }

}


