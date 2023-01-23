package eu.themetacloudservice.network.nodes.from;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageToManagerCallBackServiceLaunch extends Packet {

    private String service;
    private  String node;
    private  Integer port;

    public PackageToManagerCallBackServiceLaunch() {
        setPacketUUID(3825221);
    }


    public PackageToManagerCallBackServiceLaunch(String service, String node, Integer port) {
        setPacketUUID(3825221);
        this.service = service;
        this.node = node;
        this.port = port;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        service = buffer.readString();
        node = buffer.readString();
        port = buffer.readInt();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(service);
        buffer.writeString(node);
        buffer.writeInt(port);
    }

    public String getService() {
        return service;
    }

    public String getNode() {
        return node;
    }

    public Integer getPort() {
        return port;
    }
}
