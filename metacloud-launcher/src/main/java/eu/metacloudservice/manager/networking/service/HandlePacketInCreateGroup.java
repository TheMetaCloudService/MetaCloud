/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.in.service.cloudapi.PacketInCreateGroup;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInCreateGroup implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInCreateGroup){
            Driver.getInstance().getGroupDriver().create((Group) new ConfigDriver().convert(((PacketInCreateGroup) packet).getGroupConfig(), Group.class));
        }
    }
}
