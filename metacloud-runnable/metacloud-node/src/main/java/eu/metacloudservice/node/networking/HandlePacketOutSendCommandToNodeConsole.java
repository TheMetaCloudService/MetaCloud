/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.out.node.PacketOutSendCommandToNodeConsole;
import io.netty.channel.Channel;

public class HandlePacketOutSendCommandToNodeConsole implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketOutSendCommandToNodeConsole){
            Driver.getInstance().getTerminalDriver().getCommandDriver().executeCommand(((PacketOutSendCommandToNodeConsole) packet).getCommand());
        }
    }
}
