package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.async.pool.service.entrys.AsyncCloudService;
import eu.metacloudservice.events.listeners.services.CloudProxyConnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceConnectedEvent;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutServiceConnected;
import eu.metacloudservice.pool.service.entrys.CloudService;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketOutServiceConnected implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutServiceConnected){
            if (!CloudAPI.getInstance().getServicePool().serviceNotNull(((PacketOutServiceConnected) packet).getName())){
                CloudAPI.getInstance().getServicePool().registerService(new CloudService(((PacketOutServiceConnected) packet).getName(), ((PacketOutServiceConnected) packet).getGroup()));
                CloudAPI.getInstance().getAsyncServicePool().registerService(new AsyncCloudService(((PacketOutServiceConnected) packet).getName(), ((PacketOutServiceConnected) packet).getGroup()));
                CloudService cloudService = CloudAPI.getInstance().getServicePool().getService(((PacketOutServiceConnected) packet).getName());
                if (cloudService.getGroup().getGroupType().equals("PROXY")){
                    try {
                        CloudAPI.getInstance().getEventDriver().executeEvent(new CloudProxyConnectedEvent(cloudService.getName(), cloudService.getGroup().getStorage().getRunningNode(), cloudService.getPort(), cloudService.getAddress(), cloudService.getGroup().getGroup()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    try {
                        CloudAPI.getInstance().getEventDriver().executeEvent(new CloudServiceConnectedEvent(cloudService.getName(), cloudService.getGroup().getStorage().getRunningNode(), cloudService.getPort(), cloudService.getAddress(), cloudService.getGroup().getGroup()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
