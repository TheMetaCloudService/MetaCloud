package eu.themetacloudservice.manager.cloudtasks;

import eu.themetacloudservice.process.enums.ServiceStatus;

public class TaskService {


    public Integer currentPlayers;
    public ServiceStatus status;

    public TaskService() {
        this.currentPlayers = 0;
        this.status = ServiceStatus.STATUS_STARTING;
    }
}
