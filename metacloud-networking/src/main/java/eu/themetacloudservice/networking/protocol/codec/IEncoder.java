package eu.themetacloudservice.networking.protocol.codec;

import eu.themetacloudservice.networking.worker.Utils;
import eu.themetacloudservice.networking.protocol.IBuffer;
import eu.themetacloudservice.networking.protocol.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IEncoder {

    void encode(DataOutputStream output, Packet packet, IBuffer buffer) throws IOException;

    class NetPacketEncoder implements IEncoder {
        @Override
        public void encode(DataOutputStream output, Packet packet, IBuffer buffer) throws IOException {
            buffer.write("clazz", packet.getClass().getName());
            packet.write(buffer);
            output.write(Utils.nullChar(buffer.array()));
            output.flush();
        }
    }
}
