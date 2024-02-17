package eu.metacloudservice.async.pool.service.entrys;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.pool.player.entrys.AsyncCloudPlayer;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInChangeState;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInDispatchCommand;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServiceList;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServices;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class AsyncCloudService {

    private final String name;
    private final String group;

    public AsyncCloudService(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void performMore(Consumer<AsyncCloudService> cloudServiceConsumer) {
        cloudServiceConsumer.accept(this);
    }

    public String getID(){
        LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/general"), LiveServiceList.class);

        return name.replace(group, "").replace(list.getCloudServiceSplitter(), "");
    }
    public void setState(@NonNull ServiceState state){
        AsyncCloudAPI.getInstance().sendPacketAsynchronous(new PacketInChangeState(this.name, state.toString()));
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

    public List<AsyncCloudPlayer> getPlayers(){
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

    public boolean isStatic(){
        return getGroup().isRunStatic();
    }

    public void connect(String playername){
        try {
            AsyncCloudAPI.getInstance().getPlayerPool().getPlayer(playername).get().connect(this);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        return "name='"+name+"', group='"+group+"', state='"+getState()+"', address='"+getAddress()+"', port='"+getPort()+"', playerCount='"+getPlayercount()+"'";
    }



}
