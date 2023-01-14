package eu.themetacloudservice.network;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.enums.PacketSender;

public class PackageAuthenticByManager extends Packet {

    private String authenticName;
    private String connectionKey;

    public PackageAuthenticByManager(PacketSender packetSender) {
        setSender(packetSender);
        setPacketUUID(9234123);
    }

    public PackageAuthenticByManager() {
        setSender(PacketSender.OTHER);
        setPacketUUID(9234123);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {

            authenticName = buffer.readString();
            connectionKey = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {

        buffer.writeString(connectionKey);
        buffer.writeString(authenticName);

    }

    public String getAuthenticName() {
        return authenticName;
    }

    public void setAuthenticName(String authenticName) {
        this.authenticName = authenticName;
    }

    public String getConnectionKey() {
        return connectionKey;
    }

    public void setConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }
}
