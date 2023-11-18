/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInDeleteGroup;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import io.netty.channel.Channel;

public class HandlePacketInDeleteGroup implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInDeleteGroup){
            Driver.getInstance().getGroupDriver().delete(((PacketInDeleteGroup) packet).getGroup());
            CloudManager.serviceDriver.delete.add(((PacketInDeleteGroup) packet).getGroup());
            CloudManager.serviceDriver.getServices(((PacketInDeleteGroup) packet).getGroup()).forEach(taskedService -> CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName()));
        }
    }
}
