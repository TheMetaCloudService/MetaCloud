/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.events;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.Locations;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIReloadEvent;
import eu.metacloudservice.serverside.bukkit.SignBootstrap;

public class CloudEventHandler implements ICloudListener {

    @Subscribe
    public void handle(CloudRestAPIReloadEvent e){
        SignBootstrap.signsAPI.configuration = ((Configuration) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/signs/configuration"), Configuration.class));
        SignBootstrap.signsAPI.Locations = ((Locations) new ConfigDriver().convert( CloudAPI.getInstance().getRestDriver().get("/module/signs/locations"), Locations.class));

    }
}
