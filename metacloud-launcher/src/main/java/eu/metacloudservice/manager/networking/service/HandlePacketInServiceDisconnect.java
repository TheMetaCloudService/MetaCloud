package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.events.listeners.services.CloudProxyDisconnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceDisconnectedEvent;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInServiceDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInServiceDisconnect){
            Group group = Driver.getInstance().getGroupDriver().load(CloudManager.serviceDriver.getService(((PacketInServiceDisconnect) packet).getService()).getEntry().getGroupName());
            if (group.getGroupType().equals("PROXY")){
                Driver.getInstance().getMessageStorage().eventDriver .executeEvent(new CloudProxyDisconnectedEvent(((PacketInServiceDisconnect) packet).getService()));
            }else {
                Driver.getInstance().getMessageStorage().eventDriver .executeEvent(new CloudServiceDisconnectedEvent(((PacketInServiceDisconnect) packet).getService()));

            }
            CloudManager.serviceDriver.unregister(((PacketInServiceDisconnect) packet).getService());
        }
    }
}
