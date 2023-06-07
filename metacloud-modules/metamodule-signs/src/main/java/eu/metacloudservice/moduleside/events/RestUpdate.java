package eu.metacloudservice.moduleside.events;

import eu.metacloudservice.config.Locations;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Priority;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIPutEvent;

public class RestUpdate implements ICloudListener {


    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudRestAPIPutEvent event){
        if (event.getPath().equals("/module/signs/locations")){
            new ConfigDriver("./modules/signs/locations.json").save(new ConfigDriver().convert(event.getContent(), Locations.class));
        }
    }
}
