package eu.themetacloudservice.manager.cloudtasks;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.manager.cloudtasks.dummy.TaskGroup;
import eu.themetacloudservice.manager.cloudtasks.dummy.TaskService;
import eu.themetacloudservice.manager.cloudtasks.interfaces.ITaskDriver;
import eu.themetacloudservice.network.node.ManagerToNodeHandelTaskShutdownPacket;
import eu.themetacloudservice.networking.NettyDriver;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TaskDriver implements ITaskDriver {


    private ArrayList<TaskGroup> registered;
    private Integer usedMemory;
    ManagerConfig config;

    public TaskDriver() {
        this.registered = new ArrayList<>();
        this.config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        this.usedMemory = config.getCanUsedMemory();
    }

    @Override
    public void registerGroup(String group) {
        this.registered.stream()
                .filter(taskGroup -> taskGroup.getTaskGroup().equalsIgnoreCase(group))
                .findAny()
                .orElse(new TaskGroup(group));
    }

    @Override
    public ArrayList<TaskGroup> getRegisteredGroups() {
        return registered;
    }

    @Override
    public TaskGroup getRegisteredGroup(String group) {
        return this.registered.stream().filter(taskGroup -> taskGroup.getTaskGroup().equals(group)).findAny().get();
    }

    @Override
    public void unRegisterGroup(String group) {
        if (getRegisteredGroup(group) != null){
            TaskGroup taskGroup = getRegisteredGroup(group);
            taskGroup.getTasks().stream()
                    .filter(taskService -> taskService.getCloudProcess() == null)
                    .forEach(taskService -> {
                        ManagerToNodeHandelTaskShutdownPacket packet = new ManagerToNodeHandelTaskShutdownPacket();
                        packet.setTask(taskService.getTask());
                        NettyDriver.getInstance().nettyServer.sendPacket(taskService.getNode(), packet);
                    });
            taskGroup.getTasks().stream()
                    .filter(taskService -> taskService.getCloudProcess() != null)
                    .forEach(taskService -> {
                        taskService.getCloudProcess().shutdown();
                    });

            this.registered.removeIf(tg ->tg.getTaskGroup().equals(group));
        }
    }

    @Override
    public void launch(String group) {
        TaskGroup tg = this.registered.stream().filter(taskGroup -> taskGroup.getTaskGroup().equals(group)).findFirst().orElse(new TaskGroup(group));



    }

    @Override
    public void shutdown(String task) {

    }

    @Override
    public void changeCurrentPlayer(String task, boolean positive) {

    }
}
