package eu.themetacloudservice.network.nodes.from;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageToManagerCallBackServiceExit extends Packet {


    private String service;

    public PackageToManagerCallBackServiceExit() {
        setPacketUUID(89329123);
    }

    public PackageToManagerCallBackServiceExit(String service) {
        setPacketUUID(89329123);
        this.service = service;

    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        service = buffer.readString();

    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(service);

    }

    public String getService() {
        return service;
    }


}
