package eu.themetacloudservice.network.tasks;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageHandelCommand extends Packet {

    private String command;

    public PackageHandelCommand() {
        setPacketUUID(32419043);
    }

    public PackageHandelCommand(String command) {
        setPacketUUID(32419043);
        this.command = command;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        command = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(command);
    }

    public String getCommand() {
        return command;
    }
}
