package eu.metacloudservice.webserver.entry;

import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPICreateEvent;
import eu.metacloudservice.networking.packet.packets.out.service.events.PacketOutCloudRestAPIUpdateEvent;
import eu.metacloudservice.webserver.interfaces.IRouteEntry;
import lombok.NonNull;

public class RouteEntry implements IRouteEntry {


    public String route;
    private String json_option;


    public RouteEntry() {}

    public RouteEntry( final String route, final String json_option) {
        this.route = route;
        this.json_option = json_option;
        NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutCloudRestAPICreateEvent(route, json_option));

    }

    @Override
    public String channelRead() {
        return this.json_option;
    }

    @Override
    public void channelWrite(@NonNull final String option) {
        this.json_option = option;
    }

    @Override
    public String readROUTE() {
        return this.route;
    }

    @Override
    public void channelUpdate(@NonNull final String update) {
        this.json_option = update;
        NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutCloudRestAPIUpdateEvent(route, update));
    }
}
