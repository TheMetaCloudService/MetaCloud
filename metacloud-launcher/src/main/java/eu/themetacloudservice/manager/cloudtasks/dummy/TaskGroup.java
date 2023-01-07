package eu.themetacloudservice.manager.cloudtasks;

import java.util.ArrayList;

public class TaskGroup {


    private String taskGroup;
    private ArrayList<TaskService> tasks;
    private Integer currentPlayers;

    public TaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
        this.tasks = new ArrayList<>();
        this.currentPlayers = 0;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public ArrayList<TaskService> getTasks() {
        return tasks;
    }

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }
}
