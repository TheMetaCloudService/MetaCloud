/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.command;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.in.service.command.PacketInCommandMaintenance;
import eu.metacloudservice.networking.in.service.command.PacketInCommandMaxPlayers;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInCommandMaxPlayers implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInCommandMaxPlayers){
            if (Driver.getInstance().getGroupDriver().find(((PacketInCommandMaxPlayers) packet).getGroup())){
                Group raw = Driver.getInstance().getGroupDriver().load(((PacketInCommandMaxPlayers) packet).getGroup());
                raw.setMaxPlayers(((PacketInCommandMaxPlayers) packet).getAmount());
                Driver.getInstance().getGroupDriver().update(((PacketInCommandMaxPlayers) packet).getGroup(), raw);
                Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
            }

        }
    }
}
