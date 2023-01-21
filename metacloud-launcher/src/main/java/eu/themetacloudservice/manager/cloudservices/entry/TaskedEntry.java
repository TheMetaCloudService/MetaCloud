package eu.themetacloudservice.manager.cloudservices.entry;

import eu.themetacloudservice.manager.cloudservices.enums.TaskedServiceStatus;

public class TaskedEntry {

    private int current_players, check_interval, check_interval_players, used_port;
    private String group_name, service_name, task_node;
    private TaskedServiceStatus status;

    public TaskedEntry(int used_port, String group_name, String service_name, String task_node) {
        this.current_players = 0;
        this.check_interval = 0;
        this.check_interval_players = 0;
        this.used_port = used_port;
        this.group_name = group_name;
        this.service_name = service_name;
        this.task_node = task_node;
        this.status = TaskedServiceStatus.QUEUED;
    }

    public int getCheckInterval() {
        return check_interval;
    }

    public void setCheckInterval(int check_interval) {
        this.check_interval = check_interval;
    }

    public void setCurrentPlayers(int current_players) {
        this.current_players = current_players;
    }

    public int getCurrentPlayers() {
        return current_players;
    }

    public String getGroupName() {
        return group_name;
    }

    public String getServiceName() {
        return service_name;
    }

    public String getNode() {
        return task_node;
    }

    public TaskedServiceStatus getStatus() {
        return status;
    }

    public void setStatus(TaskedServiceStatus status) {
        this.status = status;
    }

    public int getUsedPort() {
        return used_port;
    }

    public int getCheckIntervalPlayers() {
        return check_interval_players;
    }

    public void setCheckIntervalPlayers(int check_interval_players) {
        this.check_interval_players = check_interval_players;
    }
}
