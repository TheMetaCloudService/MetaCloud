/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIReloadEvent;
import eu.metacloudservice.networking.packet.packets.out.service.PacketOutResAPItReload;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketOutResAPItReload implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutResAPItReload){
            CloudAPI.getInstance().getEventDriver().executeEvent(new CloudRestAPIReloadEvent());
        }
    }
}
