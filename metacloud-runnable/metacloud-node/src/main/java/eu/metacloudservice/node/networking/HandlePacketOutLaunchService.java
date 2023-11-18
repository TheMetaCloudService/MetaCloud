package eu.metacloudservice.node.networking;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.packet.packets.out.node.PacketOutLaunchService;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.node.CloudNode;
import eu.metacloudservice.node.cloudservices.entry.QueueEntry;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketOutLaunchService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {

        if (packet instanceof PacketOutLaunchService){
            Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-node-service-launch")
                    .replace("%service%", ((PacketOutLaunchService) packet).getService()));
            CloudNode.cloudServiceDriver.addQueue(new QueueEntry(((PacketOutLaunchService) packet).getService(),
                    ((PacketOutLaunchService) packet).getGroup(),
                    ((PacketOutLaunchService) packet).isUseProtocol()));
        }


    }
}
