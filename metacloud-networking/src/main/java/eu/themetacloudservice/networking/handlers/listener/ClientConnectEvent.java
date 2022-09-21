package eu.themetacloudservice.networking.handlers.listener;

import eu.themetacloudservice.networking.channels.Channel;
import eu.themetacloudservice.networking.handlers.bin.IEvent;

public class ClientConnectEvent implements IEvent {

    private Channel channel;

    public ClientConnectEvent(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
