package eu.themetacloudservice.manager.cloudtasks.entry;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.manager.cloudtasks.enums.TaskedServiceStatus;
import eu.themetacloudservice.manager.cloudtasks.interfaces.ITaskedService;
import eu.themetacloudservice.network.tasks.PackageHandelCommand;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.process.ServiceProcess;
import eu.themetacloudservice.timebaser.TimerBase;
import eu.themetacloudservice.timebaser.utils.TimeUtil;

import java.util.List;
import java.util.TimerTask;

public class TaskedService extends ITaskedService {


    private TaskedEntry entry;
    private ServiceProcess process;
    private boolean interrupted;
    private boolean hasStartedNew;

    public TaskedService(TaskedEntry entry) {
        this.entry = entry;
        hasStartedNew = false;
        handelPlayers();
    }

    @Override
    public void handelExecute(String line) {
        if (interrupted && entry.getStatus() == TaskedServiceStatus.QUEUED) return;
        NettyDriver.getInstance().nettyServer.sendPacket(entry.getServiceName(), new PackageHandelCommand(line));
    }

    @Override
    public void handelLaunch() {
        interrupted = false;

        if (this.getEntry().getNode().equals("InternalNode")){

        }else {

        }

    }

    @Override
    public void handelQuit() {
        this.interrupted = true;
    }

    @Override
    public void handelPlayers() {
        Group group = Driver.getInstance().getGroupDriver().load(entry.getGroupName());
        final Integer need_players = (group.getMaxPlayers()/100) * group.getStartNewPercent();
        TimerBase timerBase = new TimerBase();
        timerBase.schedule(new TimerTask() {
            @Override
            public void run() {
                if (interrupted){
                    timerBase.cancel();
                    cancel();
                }

                if (entry.getCurrentPlayers() <= need_players){
                    if (entry.getCheckInterval() == 6){
                        if (hasStartedNew) {
                           return;
                        }
                        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                        hasStartedNew = true;
                        if (entry.getNode().equals("InternalNode")){

                            TaskedService taskedService = CloudManager.taskDriver.register(new TaskedEntry(
                                    CloudManager.taskDriver.getFreePort(Driver.getInstance().getGroupDriver().load(entry.getGroupName()).getGroupType().equalsIgnoreCase("PROXY")),
                                    getEntry().getGroupName(),
                                    getEntry().getGroupName() + config.getSplitter() + CloudManager.taskDriver.getFreeUUID(entry.getGroupName()),
                                    "InternalNode"));

                            taskedService.handelLaunch();
                        }else {
                            TaskedService taskedService = CloudManager.taskDriver.register(new TaskedEntry(
                                   -1,
                                    getEntry().getGroupName(),
                                    getEntry().getGroupName() + config.getSplitter() + CloudManager.taskDriver.getFreeUUID(entry.getGroupName()),
                                    getEntry().getNode()));

                            taskedService.handelLaunch();
                        }
                    }else {
                        entry.setCheckInterval(entry.getCheckInterval() +1);
                    }
                }if (entry.getCurrentPlayers() == 0 && entry.getStatus() == TaskedServiceStatus.LOBBY){

                    if (entry.getCheckIntervalPlayers() != 12){
                        entry.setCheckIntervalPlayers(entry.getCheckIntervalPlayers()+1);
                    }else {
                        int players = 0;
                        List<TaskedService> services = CloudManager.taskDriver.getServices(group.getGroup());
                        for (int j = 0; j != services.size() ; j++) {
                            TaskedService taskedService = services.get(j);
                            players = players + taskedService.getEntry().getCurrentPlayers();
                        }

                        if (CloudManager.taskDriver.entry.global_players < 100){
                            Integer minimal = group.getOver100AtNetwork() * (CloudManager.taskDriver.entry.global_player_potency +1);

                            if (minimal > CloudManager.taskDriver.getActiveServices(group.getGroup())){
                                CloudManager.taskDriver.unregister(entry.getServiceName());
                            }
                        }else if (players < 100){

                            Integer minimal = group.getOver100AtGroup() * (CloudManager.taskDriver.entry.group_player_potency.get(entry.getGroupName()) +1);

                            if (minimal > CloudManager.taskDriver.getActiveServices(group.getGroup())){
                                CloudManager.taskDriver.unregister(entry.getServiceName());
                            }
                        }else if (group.getMinimalOnline() > CloudManager.taskDriver.getActiveServices(group.getGroup())){
                            CloudManager.taskDriver.unregister(entry.getServiceName());
                        }
                    }

                }
            }
        }, 10, 10, TimeUtil.SECONDS);
    }

    @Override
    public void handelStatusChange(TaskedServiceStatus status) {
        this.entry.setStatus(status);
        if (status == TaskedServiceStatus.IN_GAME){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            hasStartedNew = true;
            if (entry.getNode().equals("InternalNode")){

                TaskedService taskedService = CloudManager.taskDriver.register(new TaskedEntry(
                        CloudManager.taskDriver.getFreePort(Driver.getInstance().getGroupDriver().load(entry.getGroupName()).getGroupType().equalsIgnoreCase("PROXY")),
                        getEntry().getGroupName(),
                        getEntry().getGroupName() + config.getSplitter() + CloudManager.taskDriver.getFreeUUID(entry.getGroupName()),
                        "InternalNode"));

                taskedService.handelLaunch();
            }else {
                TaskedService taskedService = CloudManager.taskDriver.register(new TaskedEntry(
                        -1,
                        getEntry().getGroupName(),
                        getEntry().getGroupName() + config.getSplitter() + CloudManager.taskDriver.getFreeUUID(entry.getGroupName()),
                        getEntry().getNode()));

                taskedService.handelLaunch();
            }
        }
    }

    @Override
    public void handelCloudPlayerConnection(boolean connect) {
        if (connect){
            entry.setCurrentPlayers(entry.getCurrentPlayers() + 1);
        }else {
            entry.setCurrentPlayers(entry.getCurrentPlayers()-1);
        }
    }

    public TaskedEntry getEntry() {
        return entry;
    }
}
