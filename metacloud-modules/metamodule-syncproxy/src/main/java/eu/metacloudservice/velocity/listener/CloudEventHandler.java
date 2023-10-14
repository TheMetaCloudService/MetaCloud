/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velocity.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIReloadEvent;
import eu.metacloudservice.velocity.VeloCityBootstrap;

public class CloudEventHandler implements ICloudListener {

    @Subscribe
    public void handle(CloudRestAPIReloadEvent event){
        VeloCityBootstrap.getInstance().conf  = (Configuration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/syncproxy/configuration"), Configuration.class);
    }

}
