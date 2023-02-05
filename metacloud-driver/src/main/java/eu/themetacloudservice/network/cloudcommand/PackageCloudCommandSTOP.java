package eu.themetacloudservice.network.cloudcommand;

import eu.themetacloudservice.networking.packet.NettyBuffer;
import eu.themetacloudservice.networking.packet.Packet;

public class PackageCloudCommandSTOP extends Packet {
        private String  service;

        public PackageCloudCommandSTOP() {
                setPacketUUID(2812391);
        }

        public PackageCloudCommandSTOP(String service) {
                setPacketUUID(2812391);
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
