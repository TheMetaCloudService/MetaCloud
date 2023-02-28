package eu.metacloudservice.manager.cloudservices.entry;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.events.listeners.CloudProxyPreparedEvent;
import eu.metacloudservice.events.listeners.CloudServicePreparedEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.interfaces.ITaskedService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.out.node.PacketOutLaunchService;
import eu.metacloudservice.networking.out.node.PacketOutSendCommand;
import eu.metacloudservice.networking.out.node.PacketOutStopService;
import eu.metacloudservice.networking.out.node.PacketOutSyncService;
import eu.metacloudservice.networking.out.service.PacketOutServicePrepared;
import eu.metacloudservice.process.ServiceProcess;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServiceList;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServices;
import eu.metacloudservice.webserver.entry.RouteEntry;
import lombok.SneakyThrows;

public class TaskedService implements ITaskedService {

    private final TaskedEntry entry;
    private ServiceProcess process;
    public boolean hasStartedNew;

    public TaskedService(TaskedEntry entry) {
        this.entry = entry;
        hasStartedNew = false;
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        LiveServices liveServices = new LiveServices();
        liveServices.setGroup(entry.getGroupName());
        liveServices.setName(entry.getServiceName());
        liveServices.setPlayers(0);
        liveServices.setHost(config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equals(entry.getNode())).toList().get(0).getAddress());
        liveServices.setNode(entry.getNode());
        liveServices.setPort(-1);
        liveServices.setState(ServiceState.QUEUED);

        LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudManager.restDriver.get("/general/services"), LiveServiceList.class);
        list.getServices().add(entry.getServiceName());
        Driver.getInstance().getWebServer().updateRoute("/general/services", new ConfigDriver().convert(list));
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/services/" + entry.getServiceName().replace(config.getSplitter(), "~"), new ConfigDriver().convert(liveServices)));

        if (Driver.getInstance().getGroupDriver().load(entry.getGroupName()).getGroupType().equals("PROXY")){
            NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServicePrepared(entry.getServiceName(), true, entry.getGroupName(), entry.getNode()));
            CloudManager.eventDriver.executeEvent(new CloudProxyPreparedEvent(entry.getServiceName(), entry.getGroupName(), entry.getNode()));
        }else {
            NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServicePrepared(entry.getServiceName(), false, entry.getGroupName(), entry.getNode()));
            CloudManager.eventDriver.executeEvent(new CloudServicePreparedEvent(entry.getServiceName(), entry.getGroupName(), entry.getNode()));
        }

    }



    @SneakyThrows
    @Override
    public void handelExecute(String line) {
       if (entry.getNode().equalsIgnoreCase("InternalNode")){
           process.getProcess().getOutputStream().write((line + "\n").getBytes());
           process.getProcess().getOutputStream().flush();
       }else {
             NettyDriver.getInstance().nettyServer.sendPacketSynchronized(entry.getNode(), new PacketOutSendCommand(line, entry.getServiceName()));
       }

    }

    @Override
    public void handelSync() {
        if (this.getEntry().getNode().equals("InternalNode")){
            this.process.sync();
           }else {
            PacketOutSyncService sync = new PacketOutSyncService(getEntry().getServiceName());
            NettyDriver.getInstance().nettyServer.sendPacketSynchronized(getEntry().getNode(), sync);
        }
    }

    @Override
    public void handelLaunch() {
        if (this.getEntry().getNode().equals("InternalNode")){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+getEntry().getServiceName()+"§r' wird gestartet 'node: §f"+entry.getNode()+"§r, port: §f"+entry.getUsedPort()+"§r'",
                    "The service '§f"+getEntry().getServiceName()+"§r' is starting 'node: §f"+entry.getNode()+"§r, port: §f"+entry.getUsedPort()+"§r'");
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            LiveServices liveServices = (LiveServices) new ConfigDriver().convert(CloudManager.restDriver.get("/services/" + entry.getServiceName().replace(config.getSplitter(), "~")), LiveServices.class);
            liveServices.setPort(entry.getUsedPort());
            Driver.getInstance().getWebServer().updateRoute("/services/" + entry.getServiceName().replace(config.getSplitter(), "~"), new ConfigDriver().convert(liveServices));

            process = new ServiceProcess(Driver.getInstance().getGroupDriver().load(getEntry().getGroupName()), getEntry().getServiceName(), entry.getUsedPort(), entry.isUseProtocol(), config.getSpigotVersion().equals("MINESTOM"));
            process.handelLaunch();
        }else {
            PacketOutLaunchService launch = new PacketOutLaunchService(entry.getServiceName(), new ConfigDriver().convert(Driver.getInstance().getGroupDriver().load(getEntry().getGroupName())), entry.isUseProtocol());
            NettyDriver.getInstance().nettyServer.sendPacketSynchronized(getEntry().getNode(), launch);
        }
    }

    @Override
    public void handelScreen() {

    }

    @Override
    public void handelQuit() {
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        Driver.getInstance().getWebServer().removeRoute("/services/" + entry.getServiceName().replace(config.getSplitter(), "~"));
        LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudManager.restDriver.get("/general/services"), LiveServiceList.class);
        list.getServices().remove(entry.getServiceName());
        Driver.getInstance().getWebServer().updateRoute("/general/services", new ConfigDriver().convert(list));
        if (this.getEntry().getNode().equals("InternalNode")){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+getEntry().getServiceName()+"/"+getEntry().getUUID()+"§r' wird angehalten",
                    "The service '§f"+getEntry().getServiceName()+"§r' is stopping");
            process.handelShutdown();
        }else {
            PacketOutStopService exit = new PacketOutStopService(entry.getServiceName());
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+getEntry().getServiceName()+"/"+getEntry().getUUID()+"§r' wird angehalten",
                    "The service '§f"+getEntry().getServiceName()+"§r' is stopping");
            if (  NettyDriver.getInstance().nettyServer.isChannelFound(entry.getNode())){

                NettyDriver.getInstance().nettyServer.sendPacketSynchronized(getEntry().getNode(), exit);
            }
        }
    }


    @Override
    public void handelStatusChange(ServiceState status) {
        this.entry.setStatus(status);
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        LiveServices liveServices = (LiveServices) new ConfigDriver().convert(CloudManager.restDriver.get("/services/" + entry.getServiceName().replace(config.getSplitter(), "~")), LiveServices.class);
        liveServices.setState(status);
        Driver.getInstance().getWebServer().updateRoute("/services/" + entry.getServiceName().replace(config.getSplitter(), "~"), new ConfigDriver().convert(liveServices));
        if (status == ServiceState.IN_GAME){
             hasStartedNew = true;
            if (entry.getNode().equals("InternalNode")){
                int freeMemory = Driver.getInstance().getMessageStorage().canUseMemory;
                int memoryAfter = freeMemory- Driver.getInstance().getGroupDriver().load(entry.getGroupName()).getUsedMemory();

                if (memoryAfter >= 0){
                    TaskedService taskedService = CloudManager.serviceDriver.register(new TaskedEntry(
                            CloudManager.serviceDriver.getFreePort(Driver.getInstance().getGroupDriver().load(entry.getGroupName()).getGroupType().equalsIgnoreCase("PROXY")),
                            getEntry().getGroupName(),
                            getEntry().getGroupName() + config.getSplitter() + CloudManager.serviceDriver.getFreeUUID(entry.getGroupName()),
                            "InternalNode", getEntry().isUseProtocol()));
                    Driver.getInstance().getMessageStorage().canUseMemory  =Driver.getInstance().getMessageStorage().canUseMemory -  Driver.getInstance().getGroupDriver().load(entry.getGroupName()).getUsedMemory();
                    taskedService.handelLaunch();
                }
            }
        }
    }

    @Override
    public void handelCloudPlayerConnection(boolean connect) {
        if (connect){
            entry.setCurrentPlayers(entry.getCurrentPlayers() + 1);
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            LiveServices liveServices = (LiveServices) new ConfigDriver().convert(CloudManager.restDriver.get("/services/" + entry.getServiceName().replace(config.getSplitter(), "~")), LiveServices.class);
            liveServices.setPlayers(entry.getCurrentPlayers());
            Driver.getInstance().getWebServer().updateRoute("/services/" + entry.getServiceName().replace(config.getSplitter(), "~"), new ConfigDriver().convert(liveServices));

        }else {
            entry.setCurrentPlayers(entry.getCurrentPlayers()-1);
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            LiveServices liveServices = (LiveServices) new ConfigDriver().convert(CloudManager.restDriver.get("/services/" + entry.getServiceName().replace(config.getSplitter(), "~")), LiveServices.class);
            liveServices.setPlayers(entry.getCurrentPlayers());
            Driver.getInstance().getWebServer().updateRoute("/services/" + entry.getServiceName().replace(config.getSplitter(), "~"), new ConfigDriver().convert(liveServices));

        }

    }

    public TaskedEntry getEntry() {
        return entry;
    }
}
