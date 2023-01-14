package eu.themetacloudservice.network;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.enums.PacketSender;

public class PackageCallBackAuthenticByManager extends Packet {

    private boolean accepted;
    private String reason;

    public PackageCallBackAuthenticByManager(PacketSender packetSender) {
        setSender(packetSender);
        setPacketUUID(4992390);
    }

    public PackageCallBackAuthenticByManager() {
        setSender(PacketSender.OTHER);
        setPacketUUID(4992390);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        reason = buffer.readString();
        accepted = buffer.readBoolean();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(reason);
        buffer.writeBoolean(accepted);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
