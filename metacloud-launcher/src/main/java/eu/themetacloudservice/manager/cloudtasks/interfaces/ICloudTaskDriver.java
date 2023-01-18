package eu.themetacloudservice.manager.cloudtasks.interfaces;

import eu.themetacloudservice.manager.cloudtasks.entry.TaskedEntry;
import eu.themetacloudservice.manager.cloudtasks.entry.TaskedService;

import java.util.ArrayList;
import java.util.List;

public interface ICloudTaskDriver {

    TaskedService register(TaskedEntry entry);
    void unregister(String service);
    Integer getFreeUUID(String group);
    Integer getActiveServices(String group);
    Integer getFreePort(boolean proxy);
    void shutdown(ArrayList<String> tasks);
    void handelServices();
    void updatePlayers(String service, boolean connect);
    TaskedService getService(String service);
    ArrayList<TaskedService> getServices();
    List<TaskedService> getServices(String group);
    List<TaskedService> getServicesFromNode(String node);
}
