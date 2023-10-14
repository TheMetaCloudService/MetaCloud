package eu.metacloudservice.manager.cloudservices.interfaces;

import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public interface ICloudServiceDriver {

    TaskedService register(TaskedEntry entry);
    void unregister(String service);
    void unregistered(String service);
    Integer getFreeUUID(String group);
    String getFreeUUID();
    Integer getActiveServices(String group);
    Integer getLobbiedServices(String group);
    Integer getFreePort(boolean proxy);
    void shutdown(ArrayList<String> tasks);
    void shutdownNode(String node);
    void handelServices();
    void updatePlayers(String service, boolean connect);
    TaskedService getService(String service);
    ArrayDeque<TaskedService> getServices();
    List<TaskedService> getServices(String group);
    List<TaskedService> getServicesFromNode(String node);
}
