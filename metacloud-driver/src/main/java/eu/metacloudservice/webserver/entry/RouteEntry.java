package eu.metacloudservice.webserver.entry;

import eu.metacloudservice.webserver.interfaces.IRouteEntry;

public class RouteEntry implements IRouteEntry {


    public String route;
    private String json_option;


    public RouteEntry() {}

    public RouteEntry(String route, String json_option) {
        this.route = route;
        this.json_option = json_option;
    }

    @Override
    public String channelRead() {
        return this.json_option;
    }

    @Override
    public void channelWrite(String option) {
        this.json_option = option;
    }

    @Override
    public String readROUTE() {
        return this.route;
    }

    @Override
    public void channelUpdate(String update) {
        this.json_option = update;
    }
}
