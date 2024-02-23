/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInSendConsoleFromNode;
import io.netty.channel.Channel;

public class HandlePacketInSendConsoleFromNode implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInSendConsoleFromNode){
            Driver.getInstance().getTerminalDriver().log(Driver.getInstance().getMessageStorage().screenForm, ((PacketInSendConsoleFromNode) packet).getLine());
        }
    }
}
