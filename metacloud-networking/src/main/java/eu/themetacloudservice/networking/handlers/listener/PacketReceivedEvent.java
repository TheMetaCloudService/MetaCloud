package eu.themetacloudservice.networking.handlers.listener;

import eu.themetacloudservice.networking.channels.Channel;
import eu.themetacloudservice.networking.handlers.bin.IEvent;
import eu.themetacloudservice.networking.protocol.Packet;

public class PacketReceivedEvent implements IEvent {

    private Channel channel;
    private Packet packet;

    public PacketReceivedEvent(Channel channel, Packet packet) {
        this.channel = channel;
        this.packet = packet;
    }

    public Channel getChannel() {
        return channel;
    }

    public Packet getPacket() {
        return packet;
    }
}
