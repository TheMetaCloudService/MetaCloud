package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInShutdownNode;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketInShutdownNode implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketInShutdownNode){
            CloudManager.serviceDriver.getServicesFromNode(((PacketInShutdownNode) packet).getNode()).forEach(taskedService -> {
                CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
            });
            if (Driver.getInstance().getMessageStorage().screenForm.equalsIgnoreCase(((PacketInShutdownNode) packet).getNode())){
                CloudManager.screenNode(((PacketInShutdownNode) packet).getNode());
            }
            NettyDriver.getInstance().nettyServer.removeChannel(((PacketInShutdownNode) packet).getNode());
            Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-stop")
                    .replace("%node%", ((PacketInShutdownNode) packet).getNode()));

        }

    }
}
