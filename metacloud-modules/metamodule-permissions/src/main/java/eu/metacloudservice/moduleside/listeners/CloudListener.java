package eu.metacloudservice.moduleside.listeners;

import eu.metacloudservice.config.groups.GroupConfiguration;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.config.player.entry.GivenGroup;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.EventHandler;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.listeners.CloudPlayerConnectedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CloudListener implements ICloudListener {


    @EventHandler
    public void handle(CloudPlayerConnectedEvent event){

    }
}
