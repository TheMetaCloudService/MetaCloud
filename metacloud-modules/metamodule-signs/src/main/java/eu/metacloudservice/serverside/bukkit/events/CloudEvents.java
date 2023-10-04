/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.events;

import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.services.CloudServiceChangeStateEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceConnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceDisconnectedEvent;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.serverside.bukkit.SignBootstrap;

public class CloudEvents implements ICloudListener {

    @Subscribe
    public void handelConnect(CloudServiceConnectedEvent event){
        SignBootstrap.signDriver.getQueuedServer().add(event.getName());
    }

    @Subscribe
    public void handelConnect(CloudServiceChangeStateEvent event){
        if (ServiceState.valueOf(event.getState()) != ServiceState.LOBBY){
            SignBootstrap.signDriver.getQueuedServer().removeIf(s -> s.equals(event.getName()));
        }
    }
    @Subscribe
    public void handelConnect(CloudServiceDisconnectedEvent event){
        SignBootstrap.signDriver.getQueuedServer().removeIf(s -> s.equals(event.getName()));
    }
}
