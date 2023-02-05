package eu.themetacloudservice.network.service;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageRunCommand extends Packet {

    private String command;
    private String service;

    public PackageRunCommand() {
        setPacketUUID(59488922);
    }

    public PackageRunCommand(String command, String service) {
        this.command = command;
        this.service = service;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        command = buffer.readString();
        service = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(command);
        buffer.writeString(service);
    }

    public String getCommand() {
        return command;
    }

    public String getService() {
        return service;
    }
}
