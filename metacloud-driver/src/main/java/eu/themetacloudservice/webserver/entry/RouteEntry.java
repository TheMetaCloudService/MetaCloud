package eu.themetacloudservice.webserver.entry;

import eu.themetacloudservice.webserver.interfaces.IRouteEntry;

public class RouteEntry implements IRouteEntry {


    public String route;
    private String json_option;


    public RouteEntry() {}

    public RouteEntry(String route, String json_option) {
        this.route = route;
        this.json_option = json_option;
    }


    @Override
    public String GET() {
        return this.json_option;
    }

    @Override
    public void PUT(String option) {
        this.json_option = option;
    }

    @Override
    public String GET_ROUTE() {
        return this.route;
    }

    @Override
    public void UPDATE(String update) {
        this.json_option = update;
    }


}
