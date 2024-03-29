/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInCreateGroup;
import io.netty.channel.Channel;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketInCreateGroup implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInCreateGroup){
            Driver.getInstance().getGroupDriver().create((Group) new ConfigDriver().convert(((PacketInCreateGroup) packet).getGroupConfig(), Group.class));
        }
    }
}
