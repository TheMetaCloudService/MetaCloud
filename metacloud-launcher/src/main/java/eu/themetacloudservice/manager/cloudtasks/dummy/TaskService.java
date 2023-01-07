package eu.themetacloudservice.manager.cloudtasks;

import eu.themetacloudservice.process.CloudProcess;
import eu.themetacloudservice.process.enums.ServiceStatus;

public class TaskService {

    private String task;

    private String node;
    private Integer currentPlayers;
    private CloudProcess cloudProcess;
    private ServiceStatus status;

    public TaskService(String task, String node) {
        this.task = task;
        this.node = node;
        this.currentPlayers = 0;
        this.status = ServiceStatus.STATUS_STARTING;
    }

    public TaskService(String task, String node, CloudProcess cloudProcess) {
        this.task = task;
        this.node = node;
        this.cloudProcess = cloudProcess;
        this.currentPlayers = 0;
        this.status = ServiceStatus.STATUS_STARTING;
    }


    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public String getNode() {
        return node;
    }

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }

    public CloudProcess getCloudProcess() {
        return cloudProcess;
    }

    public ServiceStatus getStatus() {
        return status;
    }
}
