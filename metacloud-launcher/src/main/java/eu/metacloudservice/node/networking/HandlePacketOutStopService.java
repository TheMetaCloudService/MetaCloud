package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.out.node.PacketOutStopService;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.node.CloudNode;
import eu.metacloudservice.node.cloudservices.entry.QueueEntry;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketOutStopService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketOutStopService){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Neue Aufgabe, Stoppen des Service '"+ ((PacketOutStopService) packet).getService() + "",
                    "New task, Stop the '"+ ((PacketOutStopService) packet).getService() + "service");
            CloudNode.cloudServiceDriver.addQueue(new QueueEntry(((PacketOutStopService) packet).getService()));
        }

    }
}
