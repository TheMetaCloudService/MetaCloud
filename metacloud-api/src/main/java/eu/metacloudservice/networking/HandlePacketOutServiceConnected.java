package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.events.listeners.CloudProxyConnectedEvent;
import eu.metacloudservice.events.listeners.CloudServiceConnectedEvent;
import eu.metacloudservice.networking.out.service.PacketOutServiceConnected;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.pool.service.entrys.CloudService;
import io.netty.channel.Channel;

public class HandlePacketOutServiceConnected implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutServiceConnected){
            CloudAPI.getInstance().getServicePool().registerService(new CloudService(((PacketOutServiceConnected) packet).getName(), ((PacketOutServiceConnected) packet).getGroup()));
            CloudService cloudService = CloudAPI.getInstance().getServicePool().getService(((PacketOutServiceConnected) packet).getName());
            if (cloudService.getGroup().getGroupType().equals("PROXY")){
                try {
                    CloudAPI.getInstance().getEventDriver().executeEvent(new CloudProxyConnectedEvent(cloudService.getName(), cloudService.getGroup().getStorage().getRunningNode(), cloudService.getPort(), cloudService.getHost(), cloudService.getGroup().getGroup()));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                try {
                    CloudAPI.getInstance().getEventDriver().executeEvent(new CloudServiceConnectedEvent(cloudService.getName(), cloudService.getGroup().getStorage().getRunningNode(), cloudService.getPort(), cloudService.getHost(), cloudService.getGroup().getGroup()));

                }catch (Exception e){
                    e.printStackTrace();
                }
                  }
        }
    }
}
