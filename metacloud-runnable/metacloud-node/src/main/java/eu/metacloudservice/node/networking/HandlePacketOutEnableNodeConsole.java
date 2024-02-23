/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.node.PacketOutEnableNodeConsole;
import io.netty.channel.Channel;

public class HandlePacketOutEnableNodeConsole implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutEnableNodeConsole){
            Driver.getInstance().getMessageStorage().sendConsoleToManager = true;

        }
    }
}
