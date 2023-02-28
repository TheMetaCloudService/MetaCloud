package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.PacketInServiceConnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.process.TaskedServiceStatus;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketInServiceConnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketInServiceConnect){
            if (CloudManager.serviceDriver.getService(((PacketInServiceConnect) packet).getService()) == null){
                channel.disconnect();
            }else if (NettyDriver.getInstance().nettyServer.isChannelFound(((PacketInServiceConnect) packet).getService())){
                channel.disconnect();
            }else {
                NettyDriver.getInstance().nettyServer.registerChannel(((PacketInServiceConnect) packet).getService(), channel);
                TaskedEntry entry = CloudManager.serviceDriver.getService(((PacketInServiceConnect) packet).getService()).getEntry();
                Group group = Driver.getInstance().getGroupDriver().load(entry.getGroupName());
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Service '§f"+((PacketInServiceConnect) packet).getService()+"/"+entry.getUUID()+"§r' ist nun §ferfolgreich§r verbunden",
                        "the service '§f"+((PacketInServiceConnect) packet).getService()+"/"+entry.getUUID()+"§r' is now §fsuccessfully§r connected");
                CloudManager.serviceDriver.getService(((PacketInServiceConnect) packet).getService()).handelStatusChange(TaskedServiceStatus.LOBBY);
                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);

                if (group.getGroupType().equals("PROXY")){

                    //TODO: SEND PACKET AND SEND EVENT

                }else {
                    String ADDRESS = config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equals(group.getStorage().getRunningNode())).findFirst().get().getAddress();


                    //TODO: SEND PACKET AND SEND EVENT
                }
            }
        }

    }
}
