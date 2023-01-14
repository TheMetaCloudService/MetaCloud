package eu.themetacloudservice.network.autentic;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageAuthenticByManager extends Packet {

    private String authenticName;
    private String connectionKey;
    private boolean isNode;

    public PackageAuthenticByManager() {
        setPacketUUID(9234123);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        authenticName = buffer.readString();
        connectionKey = buffer.readString();
        isNode = buffer.readBoolean();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(authenticName);
        buffer.writeString(connectionKey);
        buffer.writeBoolean(isNode);
    }

    public boolean isNode() {
        return isNode;
    }

    public void setNode(boolean node) {
        isNode = node;
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
