/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.events;

import eu.metacloudservice.CloudFlareModule;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.services.CloudProxyConnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudProxyDisconnectedEvent;

public class CloudFlareEvent implements ICloudListener {

    @Subscribe
    public void register(CloudProxyConnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver("./modules/cloudflare/config.json").read(Configuration.class);
        if (!configuration.getEmail().equalsIgnoreCase("me@example.com") && !configuration.getDomain().equalsIgnoreCase("example.com") &&
                !configuration.getApiToken().equalsIgnoreCase("your_api_token") && !configuration.getZoneID().equalsIgnoreCase("your_zone_id") && !configuration.getGroups().isEmpty()) {
            CloudFlareModule.flareHelper.createSRVRecord(event.getName(), event.getGroup(), event.getPort(), event.getNode());
        }
    }

    @Subscribe
    public void unregister(CloudProxyDisconnectedEvent event){
        Configuration configuration = (Configuration) new ConfigDriver("./modules/cloudflare/config.json").read(Configuration.class);
        if (!configuration.getEmail().equalsIgnoreCase("me@example.com") && !configuration.getDomain().equalsIgnoreCase("example.com") &&
                !configuration.getApiToken().equalsIgnoreCase("your_api_token") && !configuration.getZoneID().equalsIgnoreCase("your_zone_id") && !configuration.getGroups().isEmpty()) {

            CloudFlareModule.flareHelper.deleteSRVRecord(event.getName());
        }
    }

}
