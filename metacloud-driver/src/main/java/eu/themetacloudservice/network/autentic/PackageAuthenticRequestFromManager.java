package eu.themetacloudservice.network.autentic;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.enums.PacketSender;

public class PackageAuthenticRequestFromManager extends Packet {

    public PackageAuthenticRequestFromManager() {
        setPacketUUID(83281);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

    }

    @Override
    public void writePacket(NettyBuffer buffer) {

    }
}
