package eu.themetacloudservice.network.tasks;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageLaunchService extends Packet {

    private String taskProcessName;
    private String jsonGroup;

    public PackageLaunchService() {
        setPacketUUID(98032980);
    }

    public PackageLaunchService(String taskProcessName, String jsonGroup) {
        setPacketUUID(98032980);
        this.taskProcessName = taskProcessName;
        this.jsonGroup = jsonGroup;
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


    public String getJsonGroup() {
        return jsonGroup;
    }

}
