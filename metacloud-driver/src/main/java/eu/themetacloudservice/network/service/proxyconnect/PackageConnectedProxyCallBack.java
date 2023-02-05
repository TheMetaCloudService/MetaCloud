package eu.themetacloudservice.network.service.proxyconnect;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageConnectedProxyCallBack extends Packet {


    private PackageConnectedProxyCallBackData data;


    public PackageConnectedProxyCallBack() {

        setPacketUUID(943599293);
    }

    public PackageConnectedProxyCallBack(PackageConnectedProxyCallBackData data) {
        setPacketUUID(943599293);
        this.data = data;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        data = (PackageConnectedProxyCallBackData) buffer.readClass(PackageConnectedProxyCallBackData.class);
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeClass(data);
    }

    public PackageConnectedProxyCallBackData getData() {
        return data;
    }
}
