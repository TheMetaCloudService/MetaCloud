package eu.themetacloudservice.manager.cloudtasks.interfaces;

import eu.themetacloudservice.manager.cloudtasks.dummy.TaskGroup;

import java.util.ArrayList;

public interface ITaskDriver{

    void registerGroup(String group);
    ArrayList<TaskGroup> getRegisteredGroups();
    TaskGroup getRegisteredGroup(String group);
    Integer getFreePort(boolean proxy);
    void unRegisterGroup(String group);
    void launch(String group);
    void shutdown(String task);
    void changeCurrentPlayer(String task, boolean positive);

}