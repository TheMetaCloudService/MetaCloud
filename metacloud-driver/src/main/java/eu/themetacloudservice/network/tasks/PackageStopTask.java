package eu.themetacloudservice.network;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.enums.PacketSender;

public class PackageStopTask extends Packet {

    private String taskProcessName;


    public PackageStopTask(PacketSender sender) {
        setSender(sender);
        setPacketUUID(8921234);
    }

    public PackageStopTask() {
        setSender(PacketSender.OTHER);
        setPacketUUID(8921234);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        taskProcessName = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(taskProcessName);
    }

    public String getTaskProcessName() {
        return taskProcessName;
    }

    public void setTaskProcessName(String taskProcessName) {
        this.taskProcessName = taskProcessName;
    }
}
