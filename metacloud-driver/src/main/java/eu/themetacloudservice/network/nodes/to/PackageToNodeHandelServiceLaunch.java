package eu.themetacloudservice.network.nodes.to;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageToNodeHandelServiceLaunch extends Packet {

    private String service;
    private String group;
    private boolean useProtocol;

    public PackageToNodeHandelServiceLaunch() {
        setPacketUUID(7843215);
    }

    public PackageToNodeHandelServiceLaunch(String service, String group, boolean useProtocol) {
        setPacketUUID(7843215);
        this.service = service;
        this.group = group;
        this.useProtocol = useProtocol;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        service = buffer.readString();
        group = buffer.readString();
        useProtocol = buffer.readBoolean();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(service);
        buffer.writeString(group);
        buffer.writeBoolean(useProtocol);
    }

    public String getService() {
        return service;
    }

    public String getGroup() {
        return group;
    }

    public boolean isUseProtocol() {
        return useProtocol;
    }
}
