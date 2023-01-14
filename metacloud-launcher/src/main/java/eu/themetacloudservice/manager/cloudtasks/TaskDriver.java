package eu.themetacloudservice.manager.cloudtasks;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.manager.cloudtasks.dummy.TaskGroup;
import eu.themetacloudservice.manager.cloudtasks.dummy.TaskService;
import eu.themetacloudservice.manager.cloudtasks.interfaces.ITaskDriver;
import eu.themetacloudservice.network.tasks.PackageLaunchTask;
import eu.themetacloudservice.network.tasks.PackageStopTask;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.networking.packet.enums.PacketSender;
import eu.themetacloudservice.process.CloudProcess;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TaskDriver implements ITaskDriver {


    private ArrayList<TaskGroup> registered;
    private Integer usedMemory;
    private ArrayList<Integer> usedPort;
    ManagerConfig config;

    public TaskDriver() {
        this.registered = new ArrayList<>();
        this.usedPort = new ArrayList<>();
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
    public Integer getFreePort(boolean proxy) {
        if (proxy){
            for (int i = config.getBungeecordPort(); i !=Integer.MAX_VALUE ; i++) {
                if (!usedPort.contains(i)){
                    return i;
                }
            }
        }else {
            for (int i = config.getSpigotPort(); i !=Integer.MAX_VALUE ; i++) {
                if (!usedPort.contains(i)){
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void unRegisterGroup(String group) {
        if (getRegisteredGroup(group) != null){
            TaskGroup taskGroup = getRegisteredGroup(group);
            taskGroup.getTasks().stream()
                    .filter(taskService -> taskService.getCloudProcess() == null)
                    .forEach(taskService -> {
                        PackageStopTask packet = new PackageStopTask();
                        packet.setTaskProcessName(taskService.getTask());
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
        Group g = Driver.getInstance().getGroupDriver().load(tg.getTaskGroup());
        if (g.getStorage().getRunningNode().equals("InternalNode")){
            int ID = tg.getFreeID();
            String task = g.getGroup() + config.getSplitter() + ID;
            int port = getFreePort(g.getGroupType().equalsIgnoreCase("PROXY"));
            this.usedPort.add(port);
            Driver.getInstance().getQueueDriver().addToQueue(tg.register(new TaskService(task, new CloudProcess(port, task, g.getStorage().getTemplate(), g))).getCloudProcess());
        }else {
            int ID = tg.getFreeID();
            String task = g.getGroup() + config.getSplitter() + ID;
            tg.register(new TaskService(task, g.getStorage().getRunningNode()));
            PackageLaunchTask packet = new PackageLaunchTask();
            packet.setTaskProcessName(task);
            packet.setJsonGroup(new ConfigDriver().convert(g));
            NettyDriver.getInstance().nettyServer.sendPacket(g.getStorage().getRunningNode(), packet);
        }
    }

    @Override
    public void shutdown(String task) {
        if (this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().get(task) == null) return;
        TaskService service = this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().get(task);
        if (service.getNode().equals("InternalNode")){
            service.getCloudProcess().shutdown();
            this.usedPort.removeIf(integer -> integer == service.getCloudProcess().getPort());
            this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().unRegister(task);
        }else {
            PackageStopTask packet  = new PackageStopTask();
            packet.setTaskProcessName(task);
            NettyDriver.getInstance().nettyServer.sendPacket(service.getNode(), packet);
            this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().unRegister(task);
        }


    }

    @Override
    public void changeCurrentPlayer(String task, boolean positive) {
        if (this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().get(task) == null) return;

        TaskService service = this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().get(task);
        if (positive){
            service.setCurrentPlayers(service.getCurrentPlayers()+1);
            this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().setCurrentPlayers(this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().getCurrentPlayers() +1);
        }else {

            this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().setCurrentPlayers(this.registered.stream().filter(taskGroup -> taskGroup.get(task) != null).findFirst().get().getCurrentPlayers() -1);
            service.setCurrentPlayers(service.getCurrentPlayers()-1);
        }

    }
}
