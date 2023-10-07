/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.events;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Priority;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.events.listeners.player.CloudPlayerSwitchEvent;
import eu.metacloudservice.events.listeners.restapi.CloudRestAPIPutEvent;
import eu.metacloudservice.moduleside.config.*;
import eu.metacloudservice.pool.player.entrys.CloudPlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CloudEvents implements ICloudListener {


    @Subscribe(priority =  Priority.MEDIUM)
    public void handle(CloudRestAPIPutEvent event){
        if (event.getPath().equalsIgnoreCase("/module/permission/configuration")){
            new ConfigDriver("./modules/permissions/config.json").save(new ConfigDriver().convert(event.getContent(), Configuration.class));
        }
    }

    @Subscribe(priority = Priority.HIGHEST)
    public void handelConnect(CloudPlayerConnectedEvent event){

        Configuration config = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);

        if (config.getPlayers().stream().noneMatch(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(event.getUniqueId()))){


            ArrayList<IncludedAble> ables = new ArrayList<>();
            config.getGroups().stream().filter(PermissionGroup::getIsDefault).toList().forEach(permissionGroup -> ables.add(new IncludedAble(permissionGroup.getGroup(), "LIFETIME")));

            config.getPlayers().add(new PermissionPlayer(event.getUniqueId(), ables, new ArrayList<>()));
            new ConfigDriver("./modules/permissions/config.json").save(config);
            Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/permissions/config.json").read(Configuration.class)));

        }

        ArrayList<PermissionGroup> updateGroup = new ArrayList<>();

        config.getGroups().forEach(permissionGroup -> {

            ArrayList<IncludedAble> includedAbles = (ArrayList<IncludedAble>) permissionGroup.getIncluded().stream().filter(includedAble -> {
                if (includedAble.getTime().equalsIgnoreCase("LIFETIME")){
                    return true;
                }else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                    LocalDateTime dateTimeA = LocalDateTime.parse(includedAble.getTime(), dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                    return !dateTimeA.isBefore(currentDateTime);
                }
            }).toList();
            ArrayList<PermissionAble> permissionAbles = (ArrayList<PermissionAble>) permissionGroup.getPermissions().stream().filter(permissionAble -> {
                if (permissionAble.getTime().equalsIgnoreCase("LIFETIME")){
                    return true;
                }else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                    LocalDateTime dateTimeA = LocalDateTime.parse(permissionAble.getTime(), dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                    return !dateTimeA.isBefore(currentDateTime);
                }
            }).toList();

            updateGroup.add( new PermissionGroup(permissionGroup.getGroup(), permissionGroup.getIsDefault(), permissionGroup.getTagPower(), permissionGroup.getPrefix(), permissionGroup.getSuffix(), permissionAbles, includedAbles));
        });

        config.getGroups().clear();
        config.getGroups().addAll(updateGroup);


        new ConfigDriver("./modules/permissions/config.json").save(config);
        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/permissions/config.json").read(Configuration.class)));

    }

    @Subscribe(priority = Priority.HIGHEST)
    public void handelConnect(CloudPlayerSwitchEvent event){
        Configuration config = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
        if (config.getPlayers().stream().anyMatch(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(event.getUniqueId()))){
            PermissionPlayer player = config.getPlayers().stream().filter(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(event.getUniqueId())).findFirst().orElse(null);
            if (player == null) return;
            ArrayList<IncludedAble> newGroup = (ArrayList<IncludedAble>) player.getGroups().stream().filter(includedAble -> {
                if (includedAble.getTime().equalsIgnoreCase("LIFETIME")){
                    return true;
                }else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                    LocalDateTime dateTimeA = LocalDateTime.parse(includedAble.getTime(), dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                    return !dateTimeA.isBefore(currentDateTime);
                }
            }).toList();

            ArrayList<PermissionAble> newAble = (ArrayList<PermissionAble>) player.getPermissions().stream().filter(permissionAble -> {
                if (permissionAble.getTime().equalsIgnoreCase("LIFETIME")){
                    return true;
                }else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                    LocalDateTime dateTimeA = LocalDateTime.parse(permissionAble.getTime(), dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                    return !dateTimeA.isBefore(currentDateTime);
                }
            }).toList();

            if (newGroup.isEmpty()){
                config.getGroups().stream().filter(PermissionGroup::getIsDefault).toList().forEach(permissionGroup -> newGroup.add(new IncludedAble(permissionGroup.getGroup(), "LIFETIME")));
            }

            config.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(event.getUniqueId()));
            config.getPlayers().add(new PermissionPlayer(event.getUniqueId(), newGroup, newAble));
            new ConfigDriver("./modules/permissions/config.json").save(config);
            Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/permissions/config.json").read(Configuration.class)));

        }

        ArrayList<PermissionGroup> updateGroup = new ArrayList<>();

        config.getGroups().forEach(permissionGroup -> {

            ArrayList<IncludedAble> includedAbles = (ArrayList<IncludedAble>) permissionGroup.getIncluded().stream().filter(includedAble -> {
                if (includedAble.getTime().equalsIgnoreCase("LIFETIME")){
                    return true;
                }else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                    LocalDateTime dateTimeA = LocalDateTime.parse(includedAble.getTime(), dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                    return !dateTimeA.isBefore(currentDateTime);
                }
            }).toList();
            ArrayList<PermissionAble> permissionAbles = (ArrayList<PermissionAble>) permissionGroup.getPermissions().stream().filter(permissionAble -> {
                if (permissionAble.getTime().equalsIgnoreCase("LIFETIME")){
                    return true;
                }else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                    LocalDateTime dateTimeA = LocalDateTime.parse(permissionAble.getTime(), dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                    return !dateTimeA.isBefore(currentDateTime);
                }
            }).toList();

            updateGroup.add( new PermissionGroup(permissionGroup.getGroup(), permissionGroup.getIsDefault(), permissionGroup.getTagPower(), permissionGroup.getPrefix(), permissionGroup.getSuffix(), permissionAbles, includedAbles));
        });

        config.getGroups().clear();
        config.getGroups().addAll(updateGroup);

        new ConfigDriver("./modules/permissions/config.json").save(config);
        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/permissions/config.json").read(Configuration.class)));

    }
}
