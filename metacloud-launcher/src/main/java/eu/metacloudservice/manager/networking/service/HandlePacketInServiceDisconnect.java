package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.events.listeners.CloudProxyDisconnectedEvent;
import eu.metacloudservice.events.listeners.CloudServiceDisconnectedEvent;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.PacketInServiceDisconnect;
import eu.metacloudservice.networking.out.service.PacketOutServiceDisconnected;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInServiceDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInServiceDisconnect){
            Group group = Driver.getInstance().getGroupDriver().load(CloudManager.serviceDriver.getService(((PacketInServiceDisconnect) packet).getService()).getEntry().getGroupName());
            if (group.getGroupType().equals("PROXY")){
                CloudManager.eventDriver.executeEvent(new CloudProxyDisconnectedEvent(((PacketInServiceDisconnect) packet).getService()));
                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServiceDisconnected(((PacketInServiceDisconnect) packet).getService(), true));
            }else {
                CloudManager.eventDriver.executeEvent(new CloudServiceDisconnectedEvent(((PacketInServiceDisconnect) packet).getService()));
                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServiceDisconnected(((PacketInServiceDisconnect) packet).getService(), false));

            }
            CloudManager.serviceDriver.unregister(((PacketInServiceDisconnect) packet).getService());
        }
    }
}
