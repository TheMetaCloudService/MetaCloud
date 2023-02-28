package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.out.node.PacketOutSyncService;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.node.CloudNode;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketOutSyncService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketOutSyncService){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Neue Aufgabe, den Service '"+ ((PacketOutSyncService) packet).getService() + "synchronisieren",
                    "New task, synchronize the '"+ ((PacketOutSyncService) packet).getService() + "service");
            CloudNode.cloudServiceDriver.sync(((PacketOutSyncService) packet).getService());
        }
    }
}
