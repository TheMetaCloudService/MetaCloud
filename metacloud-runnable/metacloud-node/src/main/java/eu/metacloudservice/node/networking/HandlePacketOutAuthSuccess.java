package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketOutAuthSuccess implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-auth"));
    }
}
