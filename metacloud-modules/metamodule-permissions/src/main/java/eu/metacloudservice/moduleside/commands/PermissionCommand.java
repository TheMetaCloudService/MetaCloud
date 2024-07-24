/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.moduleside.config.*;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.lang.Boolean.parseBoolean;

@CommandInfo(command = "perms",description = "command-permission-description", aliases = {"permissions", "rank"})
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
            if (CloudPermissionAPI.getInstance().getPlayers().stream().anyMatch(player -> player.getUuid().equals(UUIDDriver.getUUID(username)) )){
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
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equals(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-now-permission")
                                .replace("%player%", username).replace("%permission%", permission).replace("%time%", time));

                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-already-permission")
                                .replace("%player%", username));
                    }
                }else if (args.length == 5 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("remove")) {
                    String permission = args[4];
                    // Implementiere Logik für "/permission user [user] perm remove [permission]"
                    if (pp.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        pp.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equals(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-not-longer-permission")
                                .replace("%player%", username).replace("%permission%", permission));

                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-not-permission")
                                .replace("%player%", username));

                    }

                }


                else if (args.length >= 5 && args.length < 7 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("set")) {

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
                        pp.getGroups().clear();
                        pp.getGroups().add(new IncludedAble(group, time));
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equals(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-now-group")
                                .replace("%player%", username).replace("%group%", group).replace("%time%", time));
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-already-group")
                                .replace("%player%", username));

                    }

                }
                else if (args.length >= 5 && args.length < 7 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")) {
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
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equals(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-now-group")
                                .replace("%player%", username).replace("%group%", group).replace("%time%", time));
                              }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-has-already-group")
                                .replace("%player%", username));

                    }

                }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("remove")) {
                    String group = args[4];
                    if (pp.getGroups().isEmpty()){

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-not-included")
                                .replace("%player%", username));


                    }
                    // Implementiere Logik für "/permission user [user] group remove [group]"
                    if (pp.getGroups().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group))){
                        pp.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(group));
                        if (pp.getGroups().isEmpty()){
                            ArrayList<String> groups = new ArrayList<>();
                            groups.addAll(CloudPermissionAPI.getInstance().getGroups().stream().filter(PermissionGroup::getIsDefault).map(PermissionGroup::getGroup).toList());
                            groups.forEach(permissionGroup -> pp.getGroups().add(new IncludedAble(permissionGroup, "LIFETIME")));
                        }
                        configuration.getPlayers().removeIf(permissionPlayer -> permissionPlayer.getUuid().equals(UUIDDriver.getUUID(username)));
                        configuration.getPlayers().add(pp);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-remove-group" )
                                .replace("%group%", group));
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-not-included")
                                .replace("%player%", username));
                    }

                }else {
                    sendMessage();
                }
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-player-not-found")
                        .replace("%player%", username));
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
                        configuration.getGroups().add(new PermissionGroup(args[1], false, 99,"&b" +groupName + " &8| &7" ,"", "", "",new ArrayList<>(), new ArrayList<>()));
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-group-create" )
                                .replace("%group%", groupName));

                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-group-already-exists")
                                .replace("%group%", groupName));

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
                     Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-group-delete")
                            .replace("%group%", groupName));



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
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-default")
                                .replace("%group%", groupName));
                    }else if (type.equalsIgnoreCase("tagpower")){
                        int integer = Integer.parseInt(value);
                        pg.setTagPower(integer);
                        configuration.getGroups().removeIf(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(groupName));
                        configuration.getGroups().add(pg);
                        new ConfigDriver("./modules/permissions/config.json").save(configuration);
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-power")
                                .replace("%group%", groupName).replace("%power%",  "" +integer));
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-pleas-use"));

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
                        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(configuration));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-has-now-permission")
                                .replace("%group%", groupName).replace("%permission%", permission).replace("%time%", time));

                    }else {

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-has-already-permission")
                                .replace("%group%", groupName).replace("%permission%", permission));

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
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-has-not-longer-permission")
                                .replace("%group%", groupName).replace("%permission%", permission));

                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-has-not-permission")
                                .replace("%group%", groupName).replace("%permission%", permission));


                    }
                } else if (args.length >= 4 && args[2].equalsIgnoreCase("include")) {
                    // Implementiere Logik für "/permission group [group] include [group] [time]"
                    String includedGroup = args[3];
                    String time = args.length >= 5 ? args[4] : "LIFETIME";
                    if (pg.getIncluded().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(includedGroup))) {
                        if (CloudPermissionAPI.getInstance().getGroups().stream().noneMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(includedGroup))) {

                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,  Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-group-not-found")
                                    .replace("%group%", includedGroup));
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
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-now-included")
                                .replace("%group%", groupName).replace("%target%", includedGroup).replace("%time%", time));
                    }else {

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-already-included")
                                .replace("%group%", groupName).replace("%target%", includedGroup));

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

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-not-longer-excluded")
                                .replace("%group%", groupName).replace("%target%", excludedGroup));
                           }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-not-excluded")
                                .replace("%group%", groupName).replace("%target%", excludedGroup));
                    }
                }else {
                    sendMessage();
                }
            }else {

                Driver.getInstance().getTerminalDriver().log(Type.COMMAND,  Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-group-not-found")
                        .replace("%group%", groupName));
            }
        } else {
            sendMessage();

        }
    }


    private void sendMessage(){

        //TODO: change help page because "/perms user [name] group set [name] [time]" was added


        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-1"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-2"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-3"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-4"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-5"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-6")
                );
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                "");

        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,

                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-7"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-8"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-9"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-10"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-12"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-13"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-14"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-permission-help-15")
                );

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
            if (args[2].equalsIgnoreCase("group")){
                suggestion.add("set");
            }
            }else if (args.length == 4 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("set")){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> suggestion.add(permissionGroup.getGroup()));
            }else if (args.length == 4 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> suggestion.add(permissionGroup.getGroup()));
            }else if (args.length == 4 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("remove")){
                CloudPermissionAPI.getInstance().getPlayer(args[1]).getGroups().forEach(includedAble -> suggestion.add(includedAble.getGroup()));
            }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("set")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
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
