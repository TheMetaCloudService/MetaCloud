package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInLaunchService;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketLaunchServiceWithCustomTemplate;
import io.netty.channel.Channel;

public class HandlePacketLaunchServiceWithCustomTemplate implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketLaunchServiceWithCustomTemplate){
            Group gdata = Driver.getInstance().getGroupDriver().load(((PacketLaunchServiceWithCustomTemplate) packet).getGroup());
            String id = "";
            if (CloudManager.config.getUuid().equals("INT")){
                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( ((PacketLaunchServiceWithCustomTemplate) packet).getGroup()));
            }else if (CloudManager.config.getUuid().equals("RANDOM")){
                id = CloudManager.serviceDriver.getFreeUUID();
            }

            String serviceName = gdata.getGroup() + CloudManager.config.getSplitter() + id;
            CloudManager.serviceDriver.register(new TaskedEntry(
                    CloudManager.serviceDriver.getFreePort(gdata.getGroupType().equalsIgnoreCase("PROXY")),
                    gdata.getGroup(),
                    serviceName,
                    gdata.getStorage().getRunningNode(), CloudManager.config.getUseProtocol(), id, true, ((PacketLaunchServiceWithCustomTemplate) packet).getTemplate()));
        }
    }
}
