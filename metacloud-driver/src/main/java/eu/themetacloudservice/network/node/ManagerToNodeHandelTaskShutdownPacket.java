package eu.themetacloudservice.network.node;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class ManagerToNodeHandelTaskShutdownPacket extends Packet {


    private String task;

    public ManagerToNodeHandelTaskShutdownPacket() {
        setPacketUUID(1);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        task = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(task);
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
