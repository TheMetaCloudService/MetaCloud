package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.services.CloudProxyLaunchEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceLaunchEvent;
import eu.metacloudservice.networking.out.service.PacketOutServiceLaunch;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutServiceLaunch implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutServiceLaunch){
            if (((PacketOutServiceLaunch) packet).isProxy()){

                CloudAPI.getInstance().getEventDriver().executeEvent(new CloudProxyLaunchEvent(((PacketOutServiceLaunch) packet).getName(), ((PacketOutServiceLaunch) packet).getGroup(), ((PacketOutServiceLaunch) packet).getNode()));
            }else {
                CloudAPI.getInstance().getEventDriver().executeEvent(new CloudServiceLaunchEvent(((PacketOutServiceLaunch) packet).getName(), ((PacketOutServiceLaunch) packet).getGroup(), ((PacketOutServiceLaunch) packet).getNode()));
            }
        }
    }
}
