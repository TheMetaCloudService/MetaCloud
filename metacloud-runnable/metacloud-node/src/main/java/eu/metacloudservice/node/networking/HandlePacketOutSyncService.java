/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.node.PacketOutSyncService;
import eu.metacloudservice.node.CloudNode;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketOutSyncService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutSyncService){
            Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-service-sync")
                    .replace("%service%", ((PacketOutSyncService) packet).getService()));
            CloudNode.cloudServiceDriver.sync(((PacketOutSyncService) packet).getService());
        }
    }
}
