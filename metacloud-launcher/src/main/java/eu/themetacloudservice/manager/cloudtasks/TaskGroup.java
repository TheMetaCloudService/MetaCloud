package eu.themetacloudservice.manager.cloudtasks;

import java.util.ArrayList;

public class TaskGroup {


    public ArrayList<TaskService> tasks;
    public Integer currentPlayers;

    public TaskGroup() {
        this.tasks = new ArrayList<>();
        this.currentPlayers = 0;
    }
}
