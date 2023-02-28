package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.in.node.PacketInShutdownNode;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketInShutdownNode implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketInShutdownNode){
            CloudManager.serviceDriver.getServicesFromNode(((PacketInShutdownNode) packet).getNode()).forEach(taskedService -> {
                CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName());
            });
            NettyDriver.getInstance().nettyServer.removeChannel(((PacketInShutdownNode) packet).getNode());
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Node '§f"+((PacketInShutdownNode) packet).getNode()+"§r' ist jetzt offline, alle Dienste wurden gelöscht",
                    "the node '§f"+((PacketInShutdownNode) packet).getNode()+"§r' is now offline, all services have been deleted");

        }

    }
}
