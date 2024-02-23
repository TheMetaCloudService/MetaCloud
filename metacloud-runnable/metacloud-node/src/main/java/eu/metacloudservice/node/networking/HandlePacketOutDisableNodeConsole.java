/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.node.PacketOutDisableNodeConsole;
import io.netty.channel.Channel;

public class HandlePacketOutDisableNodeConsole implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutDisableNodeConsole){
            Driver.getInstance().getMessageStorage().sendConsoleToManager = false;
        }
    }
}
