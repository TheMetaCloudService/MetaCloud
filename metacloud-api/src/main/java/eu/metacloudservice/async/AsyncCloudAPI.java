package eu.metacloudservice.async;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.pool.player.PlayerPool;
import eu.metacloudservice.async.pool.service.ServicePool;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.events.EventDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.cloudapi.*;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.GroupList;
import eu.metacloudservice.webserver.dummys.WhiteList;
import lombok.NonNull;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class AsyncCloudAPI {

    private static AsyncCloudAPI instance;

    private PlayerPool playerPool;
    private ServicePool servicePool;

    public AsyncCloudAPI() {
        instance = this;
        this.playerPool = new PlayerPool();
        this.servicePool = new ServicePool();
    }

    public void launchService(String group){
        sendPacketAsynchronous(new PacketInLaunchService(group));
    }

    public void dispatchCommand(String command){
        sendPacketAsynchronous(new PacketInDispatchMainCommand(command));
    }

    public void stopService(String service){
        sendPacketAsynchronous(new PacketInStopService(service));
    }

    public void stopGroup(String group){
        sendPacketAsynchronous(new PacketInStopGroup(group));
    }

    public void launchServices(String group, int count){
        for (int i = 0; i != count-1; i++) {
            launchService(group);
        }
    }


    public double getUsedMemory(){
        return  (double) ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576;

    }

    public double getMaxMemory(){
        return  (double) ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / 1048576;

    }

    public ArrayList<Group> getGroups(){
        ArrayList<Group> groups = new ArrayList<>();
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        cech.getGroups().forEach(s -> {
            Group g = (Group) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/" + s), Group.class);
            groups.add(g);
        });
        return groups;

    }

    public LiveService getCurrentService(){
        return CloudAPI.getInstance().getCurrentService();
    }

    public List<String> getGroupsName(){
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        return cech.getGroups();
    }

    public List<String> getWhitelist(){
        WhiteList cech = (WhiteList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/default/whitelist"), WhiteList.class);
        return cech.getWhitelist();
    }

    public Messages getMessages(){
        return (Messages) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/message/default"), Messages.class);
    }

    public EventDriver getEventDriver() {
        return CloudAPI.getInstance().getEventDriver();
    }

    public boolean addWhiteList(String username){
        if (getWhitelist().stream().noneMatch(s -> s.equals(username))){
            dispatchCommand("service whitelist add " + username);
            return true;
        }
        return false;
    }

    public boolean removeWhiteList(String username){
        if (getWhitelist().stream().anyMatch(s -> s.equals(username))){
            dispatchCommand("service whitelist remove " + username);
            return true;
        }

        return false;
    }

    public void registerListener(ICloudListener eventListener){
        CloudAPI.getInstance().getEventDriver().registerListener(eventListener);
    }

    public void setState(@NonNull ServiceState state, String name){
        sendPacketAsynchronous(new PacketInChangeState(name, state.toString()));
    }

    public void sendPacketAsynchronous(Packet packet){
        NettyDriver.getInstance().nettyClient.sendPacketsAsynchronous(packet);
    }

    public CloudAPI getSyncAPI(){
        return CloudAPI.getInstance();
    }

    public void setState(ServiceState state){
        setState(state, getCurrentService().getService());
    }

    public RestDriver getRestDriver() {
        return CloudAPI.getInstance().getRestDriver();
    }

    public PlayerPool getPlayerPool() {
        return playerPool;
    }

    public ServicePool getServicePool() {
        return servicePool;
    }

    public static AsyncCloudAPI getInstance() {
        return instance;
    }
}
