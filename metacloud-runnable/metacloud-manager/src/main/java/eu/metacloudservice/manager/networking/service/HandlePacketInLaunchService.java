package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInLaunchService;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;

public class HandlePacketInLaunchService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInLaunchService){
            Group gdata = Driver.getInstance().getGroupDriver().load(((PacketInLaunchService) packet).getGroup());
            String id = "";
            if (CloudManager.config.getUuid().equals("INT")){
                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( ((PacketInLaunchService) packet).getGroup()));
            }else if (CloudManager.config.getUuid().equals("RANDOM")){
                id = CloudManager.serviceDriver.getFreeUUID();
            }
            CloudManager.serviceDriver.register(new TaskedEntry(
                    CloudManager.serviceDriver.getFreePort(gdata.getGroupType().equalsIgnoreCase("PROXY")),
                    gdata.getGroup(),
                    gdata.getGroup() + CloudManager.config.getSplitter() + id,
                    gdata.getStorage().getRunningNode(), CloudManager.config.getUseProtocol(), id,false, ""));
        }
    }
}
