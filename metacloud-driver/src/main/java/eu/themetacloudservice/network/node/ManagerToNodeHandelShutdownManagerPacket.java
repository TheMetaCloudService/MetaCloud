package eu.themetacloudservice.network.node;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class ManagerToNodeHandelShutdownManagerPacket extends Packet {

    public ManagerToNodeHandelShutdownManagerPacket() {
        setPacketUUID(3);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {}

    @Override
    public void writePacket(NettyBuffer buffer) {}
}
