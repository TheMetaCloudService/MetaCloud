package eu.themetacloudservice.network.tasks;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageLaunchService extends Packet {

    private String taskProcessName;
    private String jsonGroup;

    public PackageLaunchService() {
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


    public String getJsonGroup() {
        return jsonGroup;
    }

}
