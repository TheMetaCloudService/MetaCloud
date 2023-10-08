/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.commands;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.moduleside.config.*;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;

@CommandInfo(command = "perms", DEdescription = "Alle Berechtigungen können hier verwaltet werden", ENdescription = "All permissions can be managed here", aliases = {"permissions", "rank"})
public class PermissionCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendMessage();
        }else if (args[0].equalsIgnoreCase("user")){
            handleUser(args);
        }else if (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("groups")){
            handleGroup(args);
        }else {
            sendMessage();
        }
    }

    private void handleUser(String[] args){
        Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
        if (args.length >= 2 && args[0].equalsIgnoreCase("user")) {
            String username = args[1];
            if (CloudPermissionAPI.getInstance().getPlayers().stream().anyMatch(player -> player.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(username)) )){
                PermissionPlayer pp = CloudPermissionAPI.getInstance().getPlayer(username);
                if (args.length == 3 && args[2].equalsIgnoreCase("info")) {
                    // Implementiere Logik für "/permission user [user] info"#

                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Player: " + UUIDDriver.getUsername(pp.getUuid()));
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Able groups:" );
                    pp.getGroups().forEach(includedAble ->                   Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "- "  + includedAble.getGroup() + " ~ " + includedAble.getTime()));
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Able permissions:" );
                    pp.getPermissions().forEach(permissionAble ->                         Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime() ));

                } else if (args.length >= 6 && args.length <= 8 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("add")) {
                    String permission = args[4];
                    boolean value = !args[5].equalsIgnoreCase("true") && !args[5].equalsIgnoreCase("false") || Boolean.valueOf(args[5]);
                    String time = args.length == 7 ? args[6] : "LIFETIME";
                    // Implementiere Logik für "/permission user [user] perm add [permission] [true/false] [time]"
                    if (pp.getPermissions().stream().noneMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        if (!time.equalsIgnoreCase("LIFETIME")){
                            LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                            LocalDateTime calculatedDateTime = currentDateTime.plusDays(Integer.parseInt(time)); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                            time = calculatedDateTime.format(dateTimeFormatter);
                        }

                        pp.getPermissions().add(new PermissionAble(permission, value, time));
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f"+username+"§r' hat die Berechtigung '§f"+permission+"§r@§f"+time+"§r' erfolgreich erhalten.",
                                "The player '§f"+username+"§r' has successfully received the permission '§f"+permission+"§r@§f"+time+"§r' .");

                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f"+username+"§r' besitzt diese Berechtigung bereits.",
                                "The player '§f"+username+"§r' already has this permissions.");
                    }
                }else if (args.length == 5 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("remove")) {
                    String permission = args[4];
                    // Implementiere Logik für "/permission user [user] perm remove [permission]"
                    if (pp.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        pp.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Dem Spieler '§F" + username + "§r' wurde die Berechtigung '" + permission + "' nun entzogen.",
                                "The player '§f" + username + "§r' has now been deprived of the permission '" + permission+ "'.");

                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f"+username+"§r' besitzt diese Berechtigung nicht.",
                                "The player '§f"+username+"§r' does not have this permission.");

                    }

                } else if (args.length >= 5 && args.length < 7 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")) {
                    String group = args[4];
                    String time = args.length == 6 ? args[5] : "LIFETIME";
                    // Implementiere Logik für "/permission user [user] group add [group] [time]"
                    if (pp.getGroups().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group))){
                        if (!time.equalsIgnoreCase("LIFETIME")){
                            LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                            LocalDateTime calculatedDateTime = currentDateTime.plusDays(Integer.parseInt(time)); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                            time = calculatedDateTime.format(dateTimeFormatter);
                        }

                        pp.getGroups().add(new IncludedAble(group, time));
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f"+username+"§r' hat die Gruppe '§f"+group+"§r@§f§"+time+"§r' erfolgreich erhalten.",
                                "The player '§f"+username+"§r' has successfully received the group '§f"+group+"§r@§f§"+time+"§r'.");
                              }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f"+username+"§r' besitzt diese Gruppe bereits.",
                                "The player '§f"+username+"§r' already has this group.");
                    }

                }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("remove")) {
                    String group = args[4];
                    if (pp.getGroups().isEmpty()){
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f"+username+"§r' hat die Gruppe nicht eingeschlossen.",
                                "The player '§f"+username+"§r' has not included the group.");


                    }
                    // Implementiere Logik für "/permission user [user] group remove [group]"
                    if (pp.getGroups().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group))){
                        pp.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(group));
                        if (pp.getGroups().isEmpty()){
                            ArrayList<PermissionGroup> groups  = (ArrayList<PermissionGroup>) configuration.getGroups().stream().filter(PermissionGroup::getIsDefault).toList();
                            groups.forEach(permissionGroup ->      pp.getGroups().add(new IncludedAble(permissionGroup.getGroup(), "LIFETIME")));
                        }
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f"+group+"§r' wurde erfolgreich entfernt.",
                                "The group '§f"+group+"§r' has been successfully removed.");
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f"+username+"§r' hat die Gruppe nicht eingeschlossen.",
                                "The player '§f"+username+"§r' has not included the group.");
                    }

                }else {
                    sendMessage();
                }
            }else {
                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        "Die amgegebene Spieler '§f"+username+"§r' wurde nicht gefunden",
                        "The specified player '§f" + username + "§r' was not found.");
            }
        } else {
            sendMessage();
        }
    }

    private void handleGroup(String[] args){

        Configuration configuration = (Configuration) new ConfigDriver("./modules/permissions/config.json").read(Configuration.class);
        if (args.length >= 1 && args[0].equalsIgnoreCase("groups")) {
            // Implementiere Logik für "/permission groups"
            StringBuilder result = new StringBuilder();
            if (!CloudPermissionAPI.getInstance().getGroups().isEmpty()) {
                // Füge den ersten Gruppennamen hinzu
                result.append(CloudPermissionAPI.getInstance().getGroups().get(0).getGroup());

                // Füge die restlichen Gruppennamen mit einem Komma hinzu
                for (int i = 1; i < CloudPermissionAPI.getInstance().getGroups().size(); i++) {
                    result.append(", ").append(CloudPermissionAPI.getInstance().getGroups().get(i).getGroup());
                }
            }
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"groups:");
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,">> §f" +result.toString() );
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("group")) {
            String groupName = args[1];
            if (CloudPermissionAPI.getInstance().isGroupExists(groupName) ||(args.length == 3 && args[2].equalsIgnoreCase("create"))){

                PermissionGroup pg = CloudPermissionAPI.getInstance().getGroup(groupName);
                if (args.length == 3 && args[2].equalsIgnoreCase("info")) {
                    // Implementiere Logik für "/permission group [group] info"

                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Group name: "+ pg.getGroup());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Default: "+ pg.getIsDefault());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Tag-Power: "+ pg.getTagPower());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Prefix: "+ pg.getPrefix());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Suffix: "+ pg.getSuffix());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Able groups: ");
                    pg.getIncluded().forEach(includedAble ->     Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"- " +includedAble.getGroup() + " ~ " + includedAble.getTime() ));
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"Able permissions: ");
                    pg.getPermissions().forEach(permissionAble ->           Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime()));


                } else if (args.length == 3 && args[2].equalsIgnoreCase("create")) {
                    // Implementiere Logik für "/permission group [group] create"
                    if (!CloudPermissionAPI.getInstance().isGroupExists(groupName)){
                        configuration.getGroups().add(new PermissionGroup(args[1], false, 99,"&b" +groupName + " &8| &7" ,"",new ArrayList<>(), new ArrayList<>()));
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' wurde erfolgreich erstellt.",
                                "The group '§f" + groupName + "§r' has been successfully created.");
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' existiert bereits.",
                                "The group '§f" + groupName + "§r' already exists.");
                    }
                } else if (args.length == 3 && args[2].equalsIgnoreCase("delete")) {
                    // Implementiere Logik für "/permission group [group] delete"
                    configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                    configuration.getGroups().forEach(permissionGroup -> {
                        permissionGroup.getIncluded().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(groupName));
                    });
                    configuration.getPlayers().forEach(permissionPlayer -> {

                        permissionPlayer.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(groupName));

                    });
                    new ConfigDriver("./modules/permissions/config.json").save(configuration);
                    Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Die Gruppe '§f" + groupName + "§r' wurde erfolgreich gelöscht.",
                            "The group '§f" + groupName + "§r' has been successfully deleted.");


                } else if (args.length == 5 && args[2].equalsIgnoreCase("edit")) {
                    String type = args[3];
                    String value = args[4];
                    // Implementiere Logik für "/permission group [group] edit [type] [value]"
                    if (type.equalsIgnoreCase("default")){
                        pg.setIsDefault(!pg.getIsDefault());
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                        configuration.getGroups().add(pg);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' wurde nun als Standard festgelegt.",
                                "The group '§f" + groupName + "§r' has now been set as default.");
                    }else if (type.equalsIgnoreCase("tagpower")){
                        int integer = Integer.parseInt(value);
                        pg.setTagPower(integer);
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                        configuration.getGroups().add(pg);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Tag-Power der Gruppe '§f" + groupName + "§r' wurde geändert auf '§f" + integer + "§R'.",
                                "The tag power of the group '§f" + groupName + "§r' has been changed to '§f" + integer + "§r'.");
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "bitte nutze: §fdefault, tagpower ",
                                "pleas  use: §fdefault, tagpower");
                    }

                }
                else if (args.length >= 6 && args.length < 8 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("add")) {

                    // Implementiere Logik für "/permission group [group] perm add [permission] [true/false] [time]"
                    String permission = args[4];
                    boolean value = parseBoolean(args[5]);
                    String time = args.length == 7 ? args[6] : "LIFETIME";

                    if (!time.equalsIgnoreCase("LIFETIME")){
                        LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                        LocalDateTime calculatedDateTime = currentDateTime.plusDays(Integer.parseInt(time)); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        time = calculatedDateTime.format(dateTimeFormatter);
                    }
                    if (pg.getPermissions().stream().noneMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        pg.getPermissions().add(new PermissionAble(permission, value, time));
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                        configuration.getGroups().add(pg);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' hat nun die Berechtigung '§f" + permission + "§r@§f"+time+"§r'.",
                                "The group '§f" + groupName + "§r' now has the permission '§f" + permission + "§r@§f"+time+"§r'.");
                    }else {

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' hat bereits die Berechtigung '§f" + permission + "§r'.",
                                "The group '§f" + groupName + "§r' has alreasy the permission '§f" + permission + "§r'.");

                    }
                } else if (args.length == 5 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("remove")) {
                    String permission = args[4];
                    // Implementiere Logik für "/permission group [group] perm remove [permission]"

                    if (pg != null && pg.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        pg.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                        configuration.getGroups().add(pg);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' hat nun nicht mehr die Berechtigung '§f" +permission + "§r'.",
                                "The group '§f" + groupName + "§r' no longer has the permission '§f" + permission + "§r'.");
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' besitzt nicht die Berechtigung '§f" + permission + "§r'.",
                                "The group '§f" + groupName + "§r' does not have the permission '§f" + permission + "§r'.");

                    }
                } else if (args.length >= 4 && args[2].equalsIgnoreCase("include")) {
                    // Implementiere Logik für "/permission group [group] include [group] [time]"
                    String includedGroup = args[3];
                    String time = args.length >= 5 ? args[4] : "LIFETIME";
                    if (pg.getIncluded().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(includedGroup))) {
                        if (CloudPermissionAPI.getInstance().getGroups().stream().noneMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(includedGroup))) {

                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die amgegebene Gruppe '§f"+includedGroup+"§r' wurde nicht gefunden",
                                    "The specified group '§f" + includedGroup + "§r' was not found.");
                            return;
                        }
                        if (!time.equalsIgnoreCase("LIFETIME")){
                            LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                            LocalDateTime calculatedDateTime = currentDateTime.plusDays(Integer.parseInt(time)); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                            time = calculatedDateTime.format(dateTimeFormatter);

                        }

                        pg.getIncluded().add(new IncludedAble(includedGroup, time));
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                        configuration.getGroups().add(pg);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' erbt nun von der Gruppe '§f"+includedGroup+"§r@§f§"+time+"§r'.",
                                "The group '§f" + groupName + "§r' now inherits from the group '§f"+includedGroup+"§r@§f§"+time+"§r'.");
                    }else {

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' erbt bereits von der Gruppe '§f" + includedGroup +"§r'r'.",
                                "The group '§f" + groupName + "§r' does already inherit from the group '§f" + includedGroup + "§r'.");

                    }


                } else if (args.length >= 4 && args[2].equalsIgnoreCase("exclude")) {
                    String excludedGroup = args[3];
                    // Implementiere Logik für "/permission group [group] exclude [group]"
                    if (pg.getIncluded().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(excludedGroup))){
                        pg.getIncluded().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(excludedGroup));
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                        configuration.getGroups().add(pg);
                        configuration.getPlayers().forEach(permissionPlayer -> permissionPlayer.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(groupName)));
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' erbt nicht länger von der Gruppe '§f" + excludedGroup + "§r'.",
                                "The group '§f" + groupName + "§r' no longer inherits from the group '§f" + excludedGroup+ "§r'.");

                           }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f" + groupName + "§r' erbt nicht von der Gruppe '§f" + excludedGroup + "§r'.",
                                "The group '§f" + groupName + "§r' does not inherit from the group '§f" + excludedGroup + "§r'.");
                    }
                }else {
                    sendMessage();
                }
            }else {

                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        "Die amgegebene Gruppe '§f"+groupName+"§r' wurde nicht gefunden",
                        "The specified group '§f" + groupName + "§r' was not found.");
            }
        } else {
            sendMessage();

        }
    }


    private void sendMessage(){


        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] info §7~ sehe alle daten zum Spieler ",
                " >> §fperms user [player] info §7~ see all data about the player ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] perms add [permission] [true/false] ([time]) §7~ einem Spieler eine positive oder negative Erlaubnis erteilen",
                " >> §fperms user [player] perms add [permission] [true/false] ([time])  §7~ to give a player positive or negative permissions");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] perms remove [permission] §7~ einem Spieler die Berechtigungen entziehen ",
                " >> §fperms user [player] perms remove [permission] §7~ revoke a player's privileges ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] group add [group] ([time]) §7~ Füge einen Spieler zur Gruppe hinzu",
                " >> §fperms user [player] group add [group] ([time]) §7~ Add a player to the group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [player] group remove [group] §7~ Remove einen Spieler von einer Gruppe ",
                " >> §fperms user [player] group remove [group] §7~ Remove a player from a group ");


        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                "", "");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms groups §7~ sehe alle vorhandenen Gruppe ",
                " >> §fperms groups §7~ see all available group ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] info §7~ sehe alle daten von einer Gruppe ",
                " >> §fperms group [group] info §7~ see all data about the group ");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] create §7~ um eine Gruppe zu erstellen",
                " >> §fperms group [group] create §7~ to create a new group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] delete §7~ um eine Gruppe zu löschen ",
                " >> §fperms group [group] delete §7~ to delete an group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] edit [type] [value]  §7~ um die Gruppe zu bearbeiten",
                " >> §fperms group [group] edit [type] [value] §7~ to edit the group");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] perms add [permission] [true/false] [time] §7~ einer Grouppe eine positive oder negative Erlaubnis erteilen",
                " >> §fperms group [group] perms add [permission] [true/false] [time]  §7~ to give a group positive or negative permissions");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] perms remove [permission] §7~ einer Gruppe die Berechtigungen entziehen ",
                " >> §fperms group [group] perms remove [permission] §7~ revoke a groups's privileges ");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] include [group] [time] §7~ um eine Gruppe in diese Gruppe aufzunehmen",
                " >> §fperms group [group] include [group]  [time]  §7~ to add a group to this group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] exclude [group] §7~ um eine Gruppe aus dieser Gruppe zu entfernen ",
                " >> §fperms group [group] exclude [group] §7~ to remove a group from this group ");
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> suggestion = new ArrayList<>();
        if (args.length == 0){
            suggestion.add("user");
            suggestion.add("group");
            suggestion.add("groups");
        }else if (args[0].equalsIgnoreCase("user")){
            if (args.length == 1){
                CloudPermissionAPI.getInstance().getPlayers().forEach(player -> suggestion.add(UUIDDriver.getUsername(player.getUuid())));
            }else if (args.length == 2){
                suggestion.add("info");
                suggestion.add("perms");
                suggestion.add("group");
            }else if (args.length == 3 && !args[2].equalsIgnoreCase("info")){
                suggestion.add("add");
                suggestion.add("remove");

            }else if (args.length == 4 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> suggestion.add(permissionGroup.getGroup()));
            }else if (args.length == 4 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("remove")){
                CloudPermissionAPI.getInstance().getPlayer(args[1]).getGroups().forEach(includedAble -> suggestion.add(includedAble.getGroup()));
            }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }else if (args.length == 5 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("true");
                suggestion.add("false");
            }else if (args.length == 4 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("remove")){
                CloudPermissionAPI.getInstance().getPlayer(args[1]).getPermissions().forEach(permissionAble -> suggestion.add(permissionAble.getPermission()));
            }else if (args.length == 6 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }

        }else if (args[0].equalsIgnoreCase("group")){
            if (args.length == 1){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> suggestion.add(permissionGroup.getGroup()));
            }else if (args.length == 2){
                suggestion.add("info");
                suggestion.add("create");
                suggestion.add("delete");
                suggestion.add("edit");
                suggestion.add("perms");
                suggestion.add("include");
                suggestion.add("exclude");
            }else if (args.length == 3 && args[2].equalsIgnoreCase("edit")){
                suggestion.add("default");
                suggestion.add("tagpower");
            }else if (args.length == 4 && args[2].equalsIgnoreCase("edit") && args[3].equalsIgnoreCase("default")){
                suggestion.add("true");
                suggestion.add("false");
            }else if (args.length == 3 && args[2].equalsIgnoreCase("perms")){
                suggestion.add("add");
                suggestion.add("remove");
            }else if (args.length == 5 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("true");
                suggestion.add("false");
            }else if (args.length == 6 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }else if (args.length == 4 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("remove")){
                CloudPermissionAPI.getInstance().getGroup(args[1]).getPermissions().forEach(permissionAble -> suggestion.add(permissionAble.getPermission()));
            }else if (args.length == 3 && args[2].equalsIgnoreCase("exclude")){

                CloudPermissionAPI.getInstance().getGroup(args[1]).getIncluded().forEach(includedAble -> suggestion.add(includedAble.getGroup()));
            }else if (args.length == 3 && args[2].equalsIgnoreCase("include")){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> {
                    if (!permissionGroup .getGroup().equalsIgnoreCase(args[1])) {
                        suggestion.add(permissionGroup.getGroup());
                    }else {
                        return;
                    }
                });
            }else if (args.length == 4 && args[2].equalsIgnoreCase("include")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }
        }
        return suggestion;
    }
}
