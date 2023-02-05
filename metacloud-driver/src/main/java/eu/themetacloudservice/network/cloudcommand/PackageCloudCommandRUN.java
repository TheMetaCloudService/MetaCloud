package eu.themetacloudservice.network.cloudcommand;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudCommandRUN extends Packet {
    private String  group;
    private Integer  amount;

    public PackageCloudCommandRUN() {
        setPacketUUID(980032092);
    }

    public PackageCloudCommandRUN(String group, Integer amount) {
        setPacketUUID(980032092);
        this.group = group;
        this.amount = amount;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        group = buffer.readString();
        amount = buffer.readInt();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {

        buffer.writeString(group);
        buffer.writeInt(amount);
    }

    public String getGroup() {
        return group;
    }

    public Integer getAmount() {
        return amount;
    }
}
