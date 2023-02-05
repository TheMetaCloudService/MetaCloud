package eu.themetacloudservice.network.cloudcommand;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudCommandWITELIST extends Packet {


    private boolean addTO;
    private String name;

    public PackageCloudCommandWITELIST() {
        setPacketUUID(7482812);
    }

    public PackageCloudCommandWITELIST(boolean addTO, String name) {
        setPacketUUID(7482812);
        this.addTO = addTO;
        this.name = name;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        addTO = buffer.readBoolean();
        name = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeBoolean(addTO);
        buffer.writeString(name);
    }

    public boolean isAddTO() {
        return addTO;
    }

    public String getName() {
        return name;
    }
}
