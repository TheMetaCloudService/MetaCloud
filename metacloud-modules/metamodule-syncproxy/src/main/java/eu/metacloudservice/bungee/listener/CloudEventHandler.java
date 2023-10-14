/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bungee.listener;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.DesignConfig;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIReloadEvent;

public class CloudEventHandler implements ICloudListener {

    @Subscribe
    public void handle(CloudRestAPIReloadEvent e){
        BungeeBootstrap.getInstance().conf = (Configuration) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/module/syncproxy/configuration"), Configuration.class);

        BungeeBootstrap.getInstance().configuration =   BungeeBootstrap.getInstance().conf.getConfiguration().stream().filter(designConfig -> designConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().get();
           }

}
