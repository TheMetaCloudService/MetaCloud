/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.node;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;

public class PacketOutPushTemplate extends Packet {


    private String url;
    private String template;

    public PacketOutPushTemplate() {
        setPacketUUID(87388742);
    }

    public PacketOutPushTemplate(String url, String template) {
        setPacketUUID(87388742);
        this.url = url;
        this.template = template;
    }

    @Override
    public void readPacket(NettyBuffer buffer) {
        url = buffer.readString();
        template = buffer.readString();
    }

    @Override
    public void writePacket(NettyBuffer buffer) {
        buffer.writeString(url);
        buffer.writeString(template);
    }

    public String getTemplate() {
        return template;
    }

    public String getUrl() {
        return url;
    }
}
