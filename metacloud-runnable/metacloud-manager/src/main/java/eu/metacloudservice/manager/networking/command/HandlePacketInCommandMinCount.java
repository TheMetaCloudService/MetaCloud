/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.command;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.service.command.PacketInCommandMinCount;
import io.netty.channel.Channel;

public class HandlePacketInCommandMinCount implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInCommandMinCount){
            if (Driver.getInstance().getGroupDriver().find(((PacketInCommandMinCount) packet).getGroup())){
                Group raw = Driver.getInstance().getGroupDriver().load(((PacketInCommandMinCount) packet).getGroup());
                raw.setMinimalOnline(((PacketInCommandMinCount) packet).getAmount());
                Driver.getInstance().getGroupDriver().update(((PacketInCommandMinCount) packet).getGroup(), raw);
                Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
            }
        }
    }
}
