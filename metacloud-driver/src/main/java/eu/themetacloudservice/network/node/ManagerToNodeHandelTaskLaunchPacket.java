package eu.themetacloudservice.network.node;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class ManagerToNodeHandelTaskLaunchPacket extends Packet {

    public ManagerToNodeHandelTaskLaunchPacket(){
        setPacketUUID(0);
    }


    @Override
    public void readPacket(NettyBuffer buffer) {

    }

    @Override
    public void writePacket(NettyBuffer buffer) {

    }
}
