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

    public int getID(){
        LiveServiceList list = (LiveServiceList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/general"), LiveServiceList.class);
        LiveServices services = (LiveServices) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudservice/" + getName().replace(list.getCloudServiceSplitter(), "~")), LiveServices.class);
        return services.getUuid();
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
            return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromProxy(name);
        }else {
            return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromService(name);
        }

    }

    public int getPlayercount() {
        if (getGroup().getGroupType().equalsIgnoreCase("PROXY"))
            return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromProxy(this.name).size();
        return AsyncCloudAPI.getInstance().getPlayerPool().getPlayersFromService(this.name).size();
    }

    public String toString(){
        return "name='"+name+"', group='"+group+"', id='"+getID()+"', state='"+getState()+"', address='"+getAddress()+"', port='"+getPort()+"', playerCount='"+getPlayercount()+"'";
    }

    public String getMOTD() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(getAddress(), getPort()));

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            out.write(254);

            StringBuilder sb = new StringBuilder();

            int i;

            while ((i = in.read()) != -1) {
                if ((i != 0) && (i > 16) && (i != 255) && (i != 23) && (i != 24)) {
                    sb.append((char) i);
                }
            }

            String[] data = sb.toString().split("§");

            if (data.length > 0) {
                String motd = data[0];
                return motd;
            }

        } catch (IOException error) {
            error.printStackTrace();
        }
        return "";
    }


}
