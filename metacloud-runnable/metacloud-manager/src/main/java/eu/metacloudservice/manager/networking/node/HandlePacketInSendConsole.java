package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInSendConsole;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInSendConsole implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInSendConsole){
            Driver.getInstance().getTerminalDriver().log(((PacketInSendConsole) packet).getService(), ((PacketInSendConsole) packet).getLine());
        }
    }
}
