package eu.metacloudservice.moduleside.handlers;

import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.events.listeners.player.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.events.listeners.player.CloudPlayerSwitchEvent;
import eu.metacloudservice.moduleside.MetaModule;

public class CloudPlayerHandler implements ICloudListener {

    @Subscribe
    public void handle(CloudPlayerConnectedEvent event){
        MetaModule.permissionDriver.createPlayer(event.getName());
        MetaModule.permissionDriver.updateRanks(event.getName());
        MetaModule.permissionDriver.checkGroups();
    }

    @Subscribe
    public void handle(CloudPlayerDisconnectedEvent event){
        MetaModule.permissionDriver.createPlayer(event.getName());
        MetaModule.permissionDriver.updateRanks(event.getName());
        MetaModule.permissionDriver.checkGroups();

    }


    @Subscribe
    public void handle(CloudPlayerSwitchEvent event){
        MetaModule.permissionDriver.createPlayer(event.getName());
        MetaModule.permissionDriver.updateRanks(event.getName());
        MetaModule.permissionDriver.checkGroups();
    }
}
