/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.command;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.service.command.PacketInCommandMaintenance;
import io.netty.channel.Channel;

public class HandlePacketInCommandMaintenance implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInCommandMaintenance){
            if (Driver.getInstance().getGroupDriver().find(((PacketInCommandMaintenance) packet).getName())){
                Group raw = Driver.getInstance().getGroupDriver().load(((PacketInCommandMaintenance) packet).getName());
                raw.setMaintenance(((PacketInCommandMaintenance) packet).isRemoved());
                Driver.getInstance().getGroupDriver().update(((PacketInCommandMaintenance) packet).getName(), raw);
                Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
            }
        }
    }
}
