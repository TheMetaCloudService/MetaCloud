package eu.themetacloudservice.manager.cloudtasks.dummy;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.groups.dummy.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TaskGroup {


    private String taskGroup;
    private ArrayList<TaskService> tasks;
    private Integer currentPlayers;

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public TaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
        this.tasks = new ArrayList<>();
        this.currentPlayers = 0;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public ArrayList<TaskService> getTasks() {
        return tasks;
    }

    public TaskService register(TaskService taskService) {
        return this.tasks.stream().filter(ts -> ts.getTask().equalsIgnoreCase(taskService.getTask())).findAny().orElse(taskService);
    }

    public void unRegister(String task) {
        this.tasks.stream()
                .filter(taskService -> taskService.getTask().equals(task) && taskService.getNode().equalsIgnoreCase("InternalNode"))
                .findFirst().get().getCloudProcess().shutdown();
        this.tasks.removeIf(taskService -> taskService.getTask().equals(taskService));
    }

    public TaskService get(String task) {
        return this.tasks.stream().filter(taskService -> taskService.getTask().equals(task)).findFirst().get();
    }



    public Integer getFreeID() {
        ArrayList<TaskService> services = getTasks();
        ArrayList<Integer> usedID = new ArrayList<>();
        Group group = Driver.getInstance().getGroupDriver().load(taskGroup);

        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        services.forEach(taskService -> {
            usedID.add(Integer.parseInt(taskService.getTask().replace(taskGroup, "").replace(config.getSplitter(), "")));
        });
        int maximalTasks = 0;
        if (group.getMaximalOnline() == -1){maximalTasks = Integer.MAX_VALUE;}else {maximalTasks = group.getMaximalOnline();}

        for (int i = 1; i !=maximalTasks ; i++) {
            if (!usedID.contains(i)){
                return i;
            }
        }

        return null;
    }



}
