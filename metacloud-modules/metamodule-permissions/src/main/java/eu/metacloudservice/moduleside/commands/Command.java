/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.commands;

import com.google.gson.GsonBuilder;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.moduleside.config.*;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

@CommandInfo(command = "perms", DEdescription = "Alle Berechtigungen können hier verwaltet werden", ENdescription = "All permissions can be managed here", aliases = {"permissions", "rank"})
public class Command extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args[0].equalsIgnoreCase("user")){
            if (args.length > 1){
                String player = args[1];
                Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
                if (configuration.getPlayers().stream().anyMatch(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(player)))){
                    PermissionPlayer pp = configuration.getPlayers().stream().filter(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(player))).findFirst().orElse(null);
                    if (args.length == 2){
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Player: " + UUIDDriver.getUsername(pp.getUuid()));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Able groups:" );
                        pp.getGroups().forEach(includedAble ->                   Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "- "  + includedAble.getGroup() + " ~ " + includedAble.getTime()));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Able permissions:" );
                        pp.getPermissions().forEach(permissionAble ->                         Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime() ));
                    }else if (args[2].equalsIgnoreCase("addperm")) {
                        if (args.length == 6){
                            String permission = args[3];

                            if ((args[4].equalsIgnoreCase("true") || args[4].equalsIgnoreCase("false"))){
                                boolean able = Boolean.parseBoolean(args[4]);
                                int time = args[5].equalsIgnoreCase("lifetime") ? -1 : Integer.parseInt(args[5]);

                                assert pp != null;
                                if (pp.getPermissions().stream().noneMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){

                                    String formattedDateTime;
                                    if (time != -1){
                                        LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                                        LocalDateTime calculatedDateTime = currentDateTime.plusDays(time); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                                        formattedDateTime = calculatedDateTime.format(dateTimeFormatter);
                                    }else {
                                        formattedDateTime = "LIFETIME";
                                    }
                                   pp.getPermissions().add(new PermissionAble(permission, able, formattedDateTime));
                                    configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(player)));
                                    configuration.getPlayers().add(pp);
                                    new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                    Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Der Spieler '§f"+player+"§r' hat die Berechtigung '§f"+permission+"§r@§f"+formattedDateTime+"§r' erfolgreich erhalten.",
                                            "The player '§f"+player+"§r' has successfully received the permission '§f"+permission+"§r@§f"+formattedDateTime+"§r' .");
                                }else {

                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Der Spieler '§f"+player+"§r' besitzt diese Berechtigung bereits.",
                                            "The player '§f"+player+"§r' already has this permissions.");
                                }
                            }

                        }else{
                            sendHelp();
                        }
                    }else if (args[2].equalsIgnoreCase("removeperm")){
                        if (args.length == 4){
                            if (pp != null && pp.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]))){
                                pp.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]));
                                configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(player)));
                                configuration.getPlayers().add(pp);
                                new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));

                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Dem Spieler '§F" + player + "§r' wurde die Berechtigung '" + args[3] + "' nun entzogen.",
                                        "The player '§f" + player + "§r' has now been deprived of the permission '" + args[3] + "'.");


                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der Spieler '§f"+player+"§r' besitzt diese Berechtigung nicht.",
                                        "The player '§f"+player+"§r' does not have this permission.");

                            }
                        }else {
                            sendHelp();
                        }
                    }else if (args[2].equalsIgnoreCase("addgroup")){

                        if (args.length == 5){
                            String group = args[3];

                            int time = args[4].equalsIgnoreCase("lifetime") ? -1 : Integer.parseInt(args[4]);

                            assert pp != null;
                            if (pp.getGroups().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group))){


                                String formattedDateTime;
                                if (time != -1){
                                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                                    LocalDateTime calculatedDateTime = currentDateTime.plusDays(time); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                                    formattedDateTime = calculatedDateTime.format(dateTimeFormatter);
                                }else {
                                    formattedDateTime = "LIFETIME";
                                }
                                pp.getGroups().add(new IncludedAble(group, formattedDateTime));
                                configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(player)));
                                configuration.getPlayers().add(pp);
                                new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der Spieler '§f"+player+"§r' hat die Gruppe  '§f"+group+"§r@§f§"+formattedDateTime+"§r' erfolgreich erhalten.",
                                        "The player '§f"+player+"§r' has successfully received the group '§f"+group+"§r@§f§"+formattedDateTime+"§r'.");

                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der Spieler '§f"+player+"§r' besitzt diese Gruppe bereits.",
                                        "The player '§f"+player+"§r' already has this group.");
                            }
                        }else{
                            sendHelp();
                        }

                    }else if (args[2].equalsIgnoreCase("removegroup")){
                        if (args.length == 4){
                            if (pp.getGroups().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]))){
                                pp.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]));
                                if (pp.getGroups().isEmpty()){
                                   ArrayList<PermissionGroup> groups  = (ArrayList<PermissionGroup>) configuration.getGroups().stream().filter(PermissionGroup::getIsDefault).toList();
                                   groups.forEach(permissionGroup ->      pp.getGroups().add(new IncludedAble(permissionGroup.getGroup(), "LIFETIME")));
                                }
                                configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(player)));
                                configuration.getPlayers().add(pp);
                                new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));

                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f"+args[3]+"§r' wurde erfolgreich entfernt.",
                                        "The group '§f"+args[3]+"§r' has been successfully removed.");
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der Spieler '§f"+player+"§r' hat die Gruppe nicht eingeschlossen.",
                                        "The player '§f"+player+"§r' has not included the group.");
                            }
                        }else {
                            sendHelp();
                        }
                    }else {
                        sendHelp();
                    }
                }else {

                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Die amgegebene Spieler '§f"+player+"§r' wurde nicht gefunden",
                            "The specified player '§f" + player + "§r' was not found.");
                }
            }else {
                sendHelp();
            }
        }else if (args[0].equalsIgnoreCase("group")){
            if (args.length > 1){
                String group = args[1];
                Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
                if (configuration.getGroups().stream().anyMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group))){
                    PermissionGroup pg = configuration.getGroups().stream().filter(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null);
                    if (args.length == 2){
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Group name: "+ pg.getGroup());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Default: "+ pg.getIsDefault());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Tag-Power: "+ pg.getTagPower());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Prefix: "+ pg.getPrefix());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Suffix: "+ pg.getSuffix());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Able groups: ");
                        pg.getIncluded().forEach(includedAble ->     Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"- " +includedAble.getGroup() + " ~ " + includedAble.getTime() ));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Able permissions: ");
                        pg.getPermissions().forEach(permissionAble ->           Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime()));
                    }else if (args[2].equalsIgnoreCase("addperm")) {
                        if (args.length == 6){
                            String permission = args[3];

                            if ((args[4].equalsIgnoreCase("true") || args[4].equalsIgnoreCase("false"))){
                                boolean able = Boolean.parseBoolean(args[4]);
                                int time = args[5].equalsIgnoreCase("lifetime") ? -1 : Integer.parseInt(args[5]);

                                assert pg != null;
                                if (pg.getPermissions().stream().noneMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){

                                    String formattedDateTime;
                                    if (time != -1){
                                        LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                                        LocalDateTime calculatedDateTime = currentDateTime.plusDays(time); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                                        formattedDateTime = calculatedDateTime.format(dateTimeFormatter);
                                    }else {
                                        formattedDateTime = "LIFETIME";
                                    }

                                    pg.getPermissions().add(new PermissionAble(permission, able, formattedDateTime));
                                    configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
                                    configuration.getGroups().add(pg);
                                    new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                    Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Die Gruppe '§f" + group + "§r' hat nun die Berechtigung '§f" + permission + "§r@§f"+formattedDateTime+"§r'.",
                                            "The group '§f" + group + "§r' now has the permission '§f" + permission + "§r@§f"+formattedDateTime+"§r'.");

                                }else {
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Die Gruppe '§f" + group + "§r' hat bereits die Berechtigung '§f" + permission + "§r'.",
                                            "The group '§f" + group + "§r' has alreasy the permission '§f" + permission + "§r'.");

                                }
                            }else {
                                sendHelp();
                            }

                        }else{
                            sendHelp();
                        }
                    }else if (args[2].equalsIgnoreCase("removeperm")){
                        if (args.length == 4){
                            if (pg != null && pg.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]))){
                                pg.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]));
                                configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
                                configuration.getGroups().add(pg);
                                new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f" + group + "§r' hat nun nicht mehr die Berechtigung '§f" + args[3] + "§r'.",
                                        "The group '§f" + group + "§r' no longer has the permission '§f" + args[3] + "§r'.");
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f" + group + "§r' besitzt nicht die Berechtigung '§f" + args[3] + "§r'.",
                                        "The group '§f" + group + "§r' does not have the permission '§f" + args[3] + "§r'.");

                            }
                        }else {
                            sendHelp();
                        }
                    }else if (args[2].equalsIgnoreCase("include")){

                        if (args.length == 5){
                            String target= args[3];

                            int time = args[4].equalsIgnoreCase("lifetime") ? -1 : Integer.parseInt(args[4]);

                            assert pg != null;
                            if (pg.getIncluded().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(target))){
                                if (configuration.getGroups().stream().noneMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(target))){
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Die amgegebene Gruppe '§f"+target+"§r' wurde nicht gefunden",
                                            "The specified group '§f" + target + "§r' was not found.");
                                    return;
                                }
                                String formattedDateTime;
                                if (time != -1){
                                    LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                                    LocalDateTime calculatedDateTime = currentDateTime.plusDays(time); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                                    formattedDateTime = calculatedDateTime.format(dateTimeFormatter);
                                }else {
                                    formattedDateTime = "LIFETIME";
                                }
                                pg.getIncluded().add(new IncludedAble(target, formattedDateTime));
                                configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
                                configuration.getGroups().add(pg);
                                new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f" + group + "§r' erbt nun von der Gruppe '§f"+target+"§r@§f§"+formattedDateTime+"§r'.",
                                        "The group '§f" + group + "§r' now inherits from the group '§f"+target+"§r@§f§"+formattedDateTime+"§r'.");

                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f" + group + "§r' erbt bereits von der Gruppe '§f" + target +"§r'r'.",
                                        "The group '§f" + group + "§r' does already inherit from the group '§f" + target + "§r'.");

                            }
                        }else{
                            sendHelp();
                        }

                    }else if (args[2].equalsIgnoreCase("exclude")){
                        if (args.length == 4){
                            assert pg != null;
                            if (pg.getIncluded().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]))){
                                pg.getIncluded().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]));
                                configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
                                configuration.getGroups().add(pg);
                                configuration.getPlayers().forEach(permissionPlayer -> permissionPlayer.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(group)));
                                new ConfigDriver("./modules/permissions/config.json").save(configuration);
                                Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));

                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f" + group + "§r' erbt nicht länger von der Gruppe '§f" + args[3] + "§r'.",
                                        "The group '§f" + group + "§r' no longer inherits from the group '§f" + args[3] + "§r'.");

                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f" + group + "§r' erbt nicht von der Gruppe '§f" + args[3] + "§r'.",
                                        "The group '§f" + group + "§r' does not inherit from the group '§f" + args[3] + "§r'.");

                            }
                        }else {
                            sendHelp();
                        }
                    }else if (args[2].equalsIgnoreCase("setdefault")) {
                        if (args.length == 4){
                            assert pg != null;
                            pg.setIsDefault(!pg.getIsDefault());
                            configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
                            configuration.getGroups().add(pg);
                            new ConfigDriver("./modules/permissions/config.json").save(configuration);
                            Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die Gruppe '§f" + group + "§r' wurde nun als Standard festgelegt.",
                                    "The group '§f" + group + "§r' has now been set as default.");




                        }else {
                            sendHelp();
                        }
                    }else if (args[2].equalsIgnoreCase("settagpower")) {
                        if (args.length == 4){
                            assert pg != null;
                            int integer = Integer.parseInt(args[3]);
                            pg.setTagPower(integer);
                            configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
                            configuration.getGroups().add(pg);
                            new ConfigDriver("./modules/permissions/config.json").save(configuration);
                            Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die Tag-Power der Gruppe '§f" + group + "§r' wurde geändert auf '§f" + integer + "§R'.",
                                    "The tag power of the group '§f" + group + "§r' has been changed to '§f" + integer + "§r'.");


                        }else {
                            sendHelp();
                        }
                    }else if (args[2].equalsIgnoreCase("create")){
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + group + "§r' existiert bereits.",
                                "The group '§f" + group + "§r' already exists.");

                    }else  if (args[2].equalsIgnoreCase("delete")){
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(group));
                        configuration.getGroups().forEach(permissionGroup -> {
                            permissionGroup.getIncluded().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(group));
                        });
                        configuration.getPlayers().forEach(permissionPlayer -> {

                            permissionPlayer.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(group));

                        });
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + group + "§r' wurde erfolgreich gelöscht.",
                                "The group '§f" + group + "§r' has been successfully deleted.");



                    }else {
                        sendHelp();
                    }
                    }else {

                    if (args.length == 3){
                        if (args[2].equalsIgnoreCase("create")){
                            configuration.getGroups().add(new PermissionGroup(args[1], false, 99,"§b" +group + " §8| §7" ,"",new ArrayList<>(), new ArrayList<>()));
                            new ConfigDriver("./modules/permissions/config.json").save(configuration);
                            Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die Gruppe '§f" + group + "§r' wurde erfolgreich erstellt.",
                                    "The group '§f" + group + "§r' has been successfully created.");


                        }else {
                            sendHelp();
                        }
                    }else {

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die amgegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "The specified group '§f" + group + "§r' was not found.");
                    }
                }
            }else {
                sendHelp();
            }
        }else if (args[0].equalsIgnoreCase("groups")) {
            Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
            StringBuilder result = new StringBuilder();
            if (!configuration.getGroups().isEmpty()) {
                // Füge den ersten Gruppennamen hinzu
                result.append(configuration.getGroups().get(0).getGroup());

                // Füge die restlichen Gruppennamen mit einem Komma hinzu
                for (int i = 1; i < configuration.getGroups().size(); i++) {
                    result.append(", ").append(configuration.getGroups().get(i).getGroup());
                }
            }
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"groups:");
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,">> §f" +result.toString() );
        }else{
            sendHelp();
        }

    }

    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] §7~ sehe alle daten zum Spieler ",
                " >> §fperms user [player] §7~ see all data about the player ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] addperm [permission] [true/false] [time] §7~ einem Spieler eine positive oder negative Erlaubnis erteilen",
                " >> §fperms user [player] addperm [permission] [true/false] [time]  §7~ to give a player positive or negative permissions");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] removeperm [permission] §7~ einem Spieler die Berechtigungen entziehen ",
                " >> §fperms user [player] removeperm [permission] §7~ revoke a player's privileges ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] addgroup [group] [time] §7~ Füge einen Spieler zur Gruppe hinzu",
                " >> §fperms user [player] addgroup [group]  [time]  §7~ Add a player to the group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] removegroup [group] §7~ Remove einen Spieler von einer Gruppe ",
                " >> §fperms user [player] removegroup [group] §7~ Remove a player from a group ");


        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                "", "");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms groups §7~ sehe alle vorhandenen Gruppe ",
                " >> §fperms groups §7~ see all available group ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] §7~ sehe alle daten von einer Gruppe ",
                " >> §fperms group [group] §7~ see all data about the group ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] create §7~ um eine Gruppe zu erstellen",
                " >> §fperms group [group] create §7~ to create a new group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] delete §7~ um eine Gruppe zu löschen ",
                " >> §fperms group [group] delete §7~ to delete an  group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] setdefault §7~ die Gruppe als Standard festlegen",
                " >> §fperms group [group] setdefault §7~ set the group as default");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] settagpower [power] §7~ die Tag-Power der Gruppe festlegen",
                " >> §fperms group [group] settagpower [power] §7~ set the tag power of the group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] addperm [permission] [true/false] [time] §7~ einer Grouppe eine positive oder negative Erlaubnis erteilen",
                " >> §fperms group [group] addperm [permission] [true/false] [time]  §7~ to give a group positive or negative permissions");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] removeperm [permission] §7~ einer Gruppe die Berechtigungen entziehen ",
                " >> §fperms group [group] removeperm [permission] §7~ revoke a groups's privileges ");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] include [group] [time] §7~ um eine Gruppe in diese Gruppe aufzunehmen",
                " >> §fperms group [group] include [group]  [time]  §7~ to add a group to this group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] exclude [group] §7~ um eine Gruppe aus dieser Gruppe zu entfernen ",
                " >> §fperms group [group] exclude [group] §7~ to remove a group from this group ");
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> collection = new ArrayList<>();

        Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
        if (args.length == 0){
            collection.add("user");
            collection.add("group");
            collection.add("groups");
        }else if (args[0].equalsIgnoreCase("user")){
            if (args.length == 1){
                configuration.getPlayers().forEach(permissionPlayer -> collection.add(UUIDDriver.getUsername(permissionPlayer.getUuid())));
            }else   if (args.length == 2){
                collection.add("addperm");
                collection.add("removeperm");
                collection.add("addgroup");
                collection.add("removegroup");
            }else {
                if (args[2].equalsIgnoreCase("removegroup") || args[2].equalsIgnoreCase("addgroup")){
                    if (args.length==3){
                        if (args[2].equalsIgnoreCase("removegroup")) {
                            Objects.requireNonNull(configuration.getPlayers().stream().filter(player -> player.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(args[1]))).findFirst().orElse(null)).getGroups().forEach(permissionGroup -> collection.add(permissionGroup.getGroup()));
                        }else {
                            configuration.getGroups().forEach(permissionGroup -> {
                                if ( Objects.requireNonNull(configuration.getPlayers().stream().filter(player -> player.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(args[1]))).findFirst().orElse(null)).getGroups().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(permissionGroup.getGroup()))){
                                    collection.add(permissionGroup.getGroup());
                                }
                            });
                        }
                    }else {
                        if (args[2].equalsIgnoreCase("addgroup")){
                            collection.add("30");
                            collection.add("60");
                            collection.add("90");
                            collection.add("120");
                            collection.add("LIFETIME");
                        }
                    }
                }else if (args[2].equalsIgnoreCase("addperm")){
                    if (args.length==4){
                        collection.add("true");
                        collection.add("false");
                    }else         if (args.length==5){
                        collection.add("30");
                        collection.add("60");
                        collection.add("90");
                        collection.add("120");
                        collection.add("LIFETIME");
                    }
                }else if (args[2].equalsIgnoreCase("removeperm")){
                    if (args.length==3){
                        Objects.requireNonNull(configuration.getPlayers().stream().filter(player -> player.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(args[1]))).findFirst().orElse(null)).getPermissions().forEach(permissionAble -> collection.add(permissionAble.getPermission()));
                    }
                }
            }
        }else if (args[0].equalsIgnoreCase("group")){
            if (args.length == 1){
                configuration.getGroups().forEach(permissionGroup -> collection.add(permissionGroup.getGroup()));
            }else   if (args.length == 2){
                collection.add("create");
                collection.add("delete");
                collection.add("settagpower");
                collection.add("setdefault");
                collection.add("addperm");
                collection.add("removeperm");
                collection.add("include");
                collection.add("exclude");
            }else {
                if (args[2].equalsIgnoreCase("exclude") || args[2].equalsIgnoreCase("include")){
                    if (args.length==3){
                        if (args[2].equalsIgnoreCase("exclude")){
                            Objects.requireNonNull(configuration.getGroups().stream().filter(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(args[1])).findFirst().orElse(null)).getIncluded().forEach(includedAble -> {
                                collection.add(includedAble.getGroup());
                            });
                        }else {
                            configuration.getGroups().stream().filter(permissionGroup -> !permissionGroup.getGroup().equalsIgnoreCase(args[1]) &&
                                            Objects.requireNonNull(configuration.getGroups()
                                                            .stream()
                                                            .filter(permissionGroup1 -> permissionGroup1.getGroup().equalsIgnoreCase(args[1]))
                                                            .findFirst()
                                                            .orElse(null))
                                                    .getIncluded()
                                                    .stream()
                                                    .noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(permissionGroup.getGroup())) )
                                    .forEach(permissionGroup -> {
                                collection.add(permissionGroup.getGroup());
                            });
                        }
                    }else {
                        if ( args[2].equalsIgnoreCase("include")){
                            collection.add("30");
                            collection.add("60");
                            collection.add("90");
                            collection.add("120");
                            collection.add("LIFETIME");
                        }
                    }
                }else if (args[2].equalsIgnoreCase("addperm")){
                    if (args.length==4){
                        collection.add("true");
                        collection.add("false");
                    }else         if (args.length==5){
                        collection.add("30");
                        collection.add("60");
                        collection.add("90");
                        collection.add("120");
                        collection.add("LIFETIME");
                    }
                }else if (args[2].equalsIgnoreCase("removeperm")){
                    if (args.length==3){
                        Objects.requireNonNull(configuration.getGroups().stream().filter(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(args[1])).findFirst().orElse(null)).getPermissions().forEach(permissionAble -> collection.add(permissionAble.getPermission()));
                    }
                }
            }

        }else {
            collection.add("user");
            collection.add("group");
            collection.add("groups");
        }
        return collection;
    }
}
