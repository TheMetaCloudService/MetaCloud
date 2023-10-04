package eu.metacloudservice.async.pool.service.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.async.pool.player.entrys.CloudPlayer;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.in.service.cloudapi.PacketInChangeState;
import eu.metacloudservice.networking.in.service.cloudapi.PacketInDispatchCommand;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServiceList;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServices;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class CloudService {

    private final String name;
    private final String group;

    public CloudService(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void performMore(Consumer<CloudService> cloudServiceConsumer) {
        cloudServiceConsumer.accept(this);
    }


    public void setState(@NonNull ServiceState state){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInChangeState(this.name, state.toString()));
    }

    public void sync(){
        AsyncCloudAPI.getInstance().dispatchCommand("service sync " + name);
    }

    public void shutdown(){
        AsyncCloudAPI.getInstance().stopService(name);
    }

    public boolean isTypeProxy(){
        return getGroup().getGroupType().equalsIgnoreCase("PROXY");
    }

    public boolean isTypeLobby(){
        return getGroup().getGroupType().equalsIgnoreCase("LOBBY");
    }

    public boolean isTypeGame(){
        return getGroup().getGroupType().equalsIgnoreCase("GAME");
    }

    public Group getGroup(){
    return (Group) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/" + group), Group.class);
    }

    public void dispatchCommand(@NonNull String command){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInDispatchCommand(this.name, command));
    }


    public ServiceState getState(){
        LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/general"), LiveServiceList.class);
        LiveServices services = (LiveServices) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/" + getName().replace(list.getCloudServiceSplitter(), "~")), LiveServices.class);
        return services.getState();
    }

    public String getAddress(){
        LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/general"), LiveServiceList.class);
        LiveServices services = (LiveServices) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/" + getName().replace(list.getCloudServiceSplitter(), "~")), LiveServices.class);
        return services.getHost();
    }

    public Integer getPort(){
        LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/general"), LiveServiceList.class);
        LiveServices services = (LiveServices) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/" + getName().replace(list.getCloudServiceSplitter(), "~")), LiveServices.class);
        return services.getPort();
    }

    public List<CloudPlayer> getPlayers(){
        if (getGroup().getGroupType().equalsIgnoreCase("PROXY")){
            try {
                return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromProxy(name).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromService(name).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public int getPlayercount() {
        if (getGroup().getGroupType().equalsIgnoreCase("PROXY")) {
            try {
                return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromProxy(this.name).get().size();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromService(this.name).get().size();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        return "name='"+name+"', group='"+group+"', state='"+getState()+"', address='"+getAddress()+"', port='"+getPort()+"', playerCount='"+getPlayercount()+"'";
    }



}
