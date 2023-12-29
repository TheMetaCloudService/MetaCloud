package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.events.listeners.services.CloudProxyDisconnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceDisconnectedEvent;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutServiceDisconnected;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutServiceDisconnected implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutServiceDisconnected){
            if (((PacketOutServiceDisconnected) packet).isProxy()){
                CloudAPI.getInstance().getEventDriver().executeEvent(new CloudProxyDisconnectedEvent(((PacketOutServiceDisconnected) packet).getName()));
            }else {
                CloudAPI.getInstance().getEventDriver().executeEvent(new CloudServiceDisconnectedEvent(((PacketOutServiceDisconnected) packet).getName()));
            }


            CloudAPI.getInstance().getServicePool().unregisterService(((PacketOutServiceDisconnected) packet).getName());
            AsyncCloudAPI.getInstance().getServicePool().unregisterService(((PacketOutServiceDisconnected) packet).getName());
        }
    }
}
