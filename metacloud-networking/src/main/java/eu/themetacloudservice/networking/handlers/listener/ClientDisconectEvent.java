package eu.themetacloudservice.networking.handlers.listener;

import eu.themetacloudservice.networking.channels.Channel;
import eu.themetacloudservice.networking.handlers.bin.IEvent;

public class ClientDisconectEvent implements IEvent {
    private Channel channel;

    public ClientDisconectEvent(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
