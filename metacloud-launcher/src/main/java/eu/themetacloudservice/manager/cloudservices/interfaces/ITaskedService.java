package eu.themetacloudservice.manager.cloudtasks.interfaces;

import eu.themetacloudservice.manager.cloudtasks.enums.TaskedServiceStatus;

public abstract class ITaskedService {

    public abstract void handelExecute(String line);
    public abstract void handelLaunch();
    public abstract void handelQuit();
    public abstract void handelPlayers();
    public abstract void handelStatusChange(TaskedServiceStatus status);
    public abstract void handelCloudPlayerConnection(boolean connect);

}
