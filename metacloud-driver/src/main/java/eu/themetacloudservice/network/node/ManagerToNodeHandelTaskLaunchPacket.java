package eu.themetacloudservice.network.node;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class ManagerToNodeHandelTaskLaunchPacket extends Packet {


    private String groupJson;
    private String task;

    public ManagerToNodeHandelTaskLaunchPacket(){
        setPacketUUID(0);
    }


    @Override
    public void readPacket(NettyBuffer buffer) {
        groupJson = buffer.readString();
        task = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(task);
        buffer.writeString(groupJson);
    }

    public String getGroupJson() {
        return groupJson;
    }

    public void setGroupJson(String groupJson) {
        this.groupJson = groupJson;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
