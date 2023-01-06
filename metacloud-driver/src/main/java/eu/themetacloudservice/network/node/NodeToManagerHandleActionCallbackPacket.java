package eu.themetacloudservice.network.node;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class NodeToManagerHandleActionCallbackPacket extends Packet {


    private Boolean successful;
    private String reason;

    public NodeToManagerHandleActionCallbackPacket() {
        setPacketUUID(2);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        reason = buffer.readString();
        successful = buffer.readBoolean();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {

        buffer.writeBoolean(successful);
        buffer.writeString(reason);
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
