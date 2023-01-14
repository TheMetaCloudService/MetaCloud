package eu.themetacloudservice.network.tasks;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;
import eu.themetacloudservice.networking.packet.enums.PacketSender;

public class PackageLaunchTask extends Packet {

    private String taskProcessName;
    private String jsonGroup;

    public PackageLaunchTask() {
        setPacketUUID(98032980);
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        taskProcessName = buffer.readString();
        jsonGroup = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(taskProcessName);
        buffer.writeString(jsonGroup);

    }

    public String getTaskProcessName() {
        return taskProcessName;
    }

    public void setTaskProcessName(String taskProcessName) {
        this.taskProcessName = taskProcessName;
    }

    public String getJsonGroup() {
        return jsonGroup;
    }

    public void setJsonGroup(String jsonGroup) {
        this.jsonGroup = jsonGroup;
    }
}
