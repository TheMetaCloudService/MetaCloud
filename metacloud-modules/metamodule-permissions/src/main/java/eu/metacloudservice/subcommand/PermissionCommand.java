/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.subcommand;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.commands.PluginCommand;
import eu.metacloudservice.commands.PluginCommandInfo;
import eu.metacloudservice.commands.translate.Translator;
import eu.metacloudservice.bootstrap.bungee.BungeeBootstrap;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.moduleside.config.IncludedAble;
import eu.metacloudservice.moduleside.config.PermissionAble;
import eu.metacloudservice.moduleside.config.PermissionGroup;
import eu.metacloudservice.moduleside.config.PermissionPlayer;
import eu.metacloudservice.storage.UUIDDriver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;


@PluginCommandInfo(command = "permission", description = "/cloud permission")
public class PermissionCommand extends PluginCommand {

    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {

        if (args.length == 0){
            sendMessage(proxiedPlayer, veloPlayer);
        }else if (args[0].equalsIgnoreCase("user")){
            handleUser(proxiedPlayer, veloPlayer, args);
        }else if (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("groups")){
            handleGroup(proxiedPlayer, veloPlayer, args);
        }else {
            sendMessage(proxiedPlayer, veloPlayer);
        }
    }

    private void handleUser(ProxiedPlayer proxiedPlayer, Player veloPlayer, String[] args){
        final Translator translator = new Translator();
        final Messages messages = CloudAPI.getInstance().getMessages();
        final String PREFIX = messages.getMessages().get("prefix").replace("&", "§");
        if (args.length >= 2 && args[0].equalsIgnoreCase("user")) {
            final String username = args[1];
            if (CloudPermissionAPI.getInstance().getPlayers().stream().anyMatch(player -> player.getUuid().equals(UUIDDriver.getUUID(username)) )){
                PermissionPlayer pp = CloudPermissionAPI.getInstance().getPlayer(username);
                if (args.length == 3 && args[2].equalsIgnoreCase("info")) {
                    // Implementiere Logik für "/permission user [user] info"#
                    if (proxiedPlayer == null){
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "player: " + pp.getUuid())));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "able groups: ")));
                        pp.getGroups().forEach(includedAble -> veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " + includedAble.getGroup() + " ~ " + includedAble.getTime()))));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "able permissions: ")));
                        pp.getPermissions().forEach(permissionAble -> veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime()))));

                    }else {
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "player: " + pp.getUuid())));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "able groups: ")));
                        pp.getGroups().forEach(includedAble ->  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " + includedAble.getGroup() + " ~ " + includedAble.getTime()))));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "able permissions: ")));
                        pp.getPermissions().forEach(permissionAble ->  BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime()))));
                    }
                } else if (args.length >= 6 && args.length <= 8 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("add")) {
                   final String permission = args[4];
                    final boolean value = !args[5].equalsIgnoreCase("true") && !args[5].equalsIgnoreCase("false") || Boolean.valueOf(args[5]);
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
                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' has successfully received the permission '§f"+permission+"§7@§f"+time+"§7' .")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' has successfully received the permission '§f"+permission+"§7@§f"+time+"§7' .")));
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' already has this permissions")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' already has this permissions")));
                    }
                } else if (args.length == 5 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("remove")) {
                    final String permission = args[4];
                    // Implementiere Logik für "/permission user [user] perm remove [permission]"
                    if (pp.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        pp.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f" + username + "§7' has now been deprived of the permission '§f" + permission + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f" + username + "§7' has now been deprived of the permission '§f" + permission + "§7'.")));


                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' does not have this permission.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The player '§f"+username+"§7' does not have this permission.")));
                    }

                } else if (args.length >= 5 && args.length < 7 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")) {
                    final String group = args[4];
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
                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' has successfully received the group '§f"+group+"§7@§f§"+time+"§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' has successfully received the group '§f"+group+"§7@§f§"+time+"§7'.")));
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The player '§f"+username +"§7' already has this group.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username +"§7' already has this group.")));
                    }

                }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("remove")) {
                    String group = args[4];
                    // Implementiere Logik für "/permission user [user] group remove [group]"
                    if (pp.getGroups().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group))){
                        pp.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(group));
                        if (pp.getGroups().isEmpty()){
                            ArrayList<String> groups = new ArrayList<>();
                            groups.addAll(CloudPermissionAPI.getInstance().getGroups().stream().filter(PermissionGroup::getIsDefault).map(PermissionGroup::getGroup).toList());
                            groups.forEach(permissionGroup -> pp.getGroups().add(new IncludedAble(permissionGroup, "LIFETIME")));               }
                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The group '§f"+group+"§7' has been successfully removed from the player '§f"+username+"§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The group '§f"+group+"§7' has been successfully removed from the player '§f"+username+"§7'.")));
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +   "The player '§f"+username+"§7' has not included the group '§f"+group+"§7' .")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The player '§f"+username+"§7' has not included the group '§f"+group+"§7' .")));
                    }

                }else if (args.length >= 5 && args.length < 7 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("set")) {
                    final String group = args[4];
                    String time = (args.length == 6) ? args[5] : "LIFETIME";
                    if (pp.getGroups().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(group))) {
                        if (!time.equalsIgnoreCase("LIFETIME")) {
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            LocalDateTime calculatedDateTime = currentDateTime.plusDays(Integer.parseInt(time));
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                            time = calculatedDateTime.format(dateTimeFormatter);
                        }
                        pp.getGroups().clear();
                        pp.getGroups().add(new IncludedAble(group, time));
                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' has successfully received the group '§f"+group+"§7@§f§"+time+"§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' has successfully received the group '§f"+group+"§7@§f§"+time+"§7'.")));

                    } else if (veloPlayer != null) {
                        veloPlayer.sendMessage((Component)MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+ username + "§7' already has this group.")));
                    } else {
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player '§f"+username+"§7' already has this group.")));
                    }
                }else {
                    sendMessage(proxiedPlayer, veloPlayer);
                }
            }else {
                if (veloPlayer != null)
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The player you are looking for was not found, please check that it is spelled correctly.")));
                else
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The player you are looking for was not found, please check that it is spelled correctly.")));
            }
        } else {
            sendMessage(proxiedPlayer, veloPlayer);
        }
    }

    private void handleGroup(ProxiedPlayer proxiedPlayer, Player veloPlayer, String[] args){
        final Translator translator = new Translator();
        final Messages messages = CloudAPI.getInstance().getMessages();
        final String PREFIX = messages.getMessages().get("prefix").replace("&", "§");
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
            if (veloPlayer != null) {
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "groups:")));
                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + result.toString())));
            }else{
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "groups:" )));
                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  result.toString())));

            }
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("group")) {
          final   String groupName = args[1];
            if (CloudPermissionAPI.getInstance().isGroupExists(groupName) ||(args.length == 3 && args[2].equalsIgnoreCase("create"))){

                PermissionGroup pg = CloudPermissionAPI.getInstance().getGroup(groupName);
                if (args.length == 3 && args[2].equalsIgnoreCase("info")) {
                    // Implementiere Logik für "/permission group [group] info"

                    if (proxiedPlayer == null){
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Group name: "+ pg.getGroup())));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Default: "+ pg.getIsDefault())));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Tag-Power: "+ pg.getTagPower())));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Prefix: "+ pg.getPrefix())));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Suffix: "+ pg.getSuffix())));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Able groups: ")));
                        pg.getIncluded().forEach(includedAble ->  veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " +includedAble.getGroup() + " ~ " + includedAble.getTime() ))));
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Able permissions: ")));
                        pg.getPermissions().forEach(permissionAble ->        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime()))));

                    }else {
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Group name: "+ pg.getGroup())));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Default: "+ pg.getIsDefault())));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Tag-Power: "+ pg.getTagPower())));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Prefix: "+ pg.getPrefix())));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Suffix: "+ pg.getSuffix())));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Able groups: ")));
                        pg.getIncluded().forEach(includedAble ->   BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " +includedAble.getGroup() + " ~ " + includedAble.getTime() ))));
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "Able permissions: ")));
                        pg.getPermissions().forEach(permissionAble ->         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime()))));

                    }
                  } else if (args.length == 3 && args[2].equalsIgnoreCase("create")) {
                    // Implementiere Logik für "/permission group [group] create"
                    if (!CloudPermissionAPI.getInstance().isGroupExists(groupName)){
                        CloudPermissionAPI.getInstance().createGroup(new PermissionGroup(groupName, false, 99,"§b" +groupName + " §8| §7" ,"", "", "",new ArrayList<>(), new ArrayList<>()));
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The group '§f" + groupName + "§7' has been successfully created.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +   "The group '§f" + groupName + "§7' has been successfully created.")));
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' is already exists")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The group '§f" + groupName + "§7' is already exists")));
                    }
                } else if (args.length == 3 && args[2].equalsIgnoreCase("delete")) {
                    // Implementiere Logik für "/permission group [group] delete"
                    CloudPermissionAPI.getInstance().deleteGroup(groupName);
                    if (veloPlayer != null)
                        veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' has been successfully deleted.")));
                    else
                         BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' has been successfully deleted.")));
                } else if (args.length == 5 && args[2].equalsIgnoreCase("edit")) {
                    String type = args[3];
                    String value = args[4];
                    // Implementiere Logik für "/permission group [group] edit [type] [value]"
                    if (type.equalsIgnoreCase("default")){
                        Boolean recover = parseBoolean(value);
                        pg.setIsDefault(recover);
                        CloudPermissionAPI.getInstance().updateGroup(pg);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The group '§f" + groupName + "§7' has now been set default to '§f"+recover+"§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The group '§f" + groupName + "§7' has now been set default to '§f"+recover+"§7'.")));
                    }else if (type.equalsIgnoreCase("tagpower")){
                        int integer = Integer.parseInt(value);
                        pg.setTagPower(integer);
                        CloudPermissionAPI.getInstance().updateGroup(pg);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The tag power of the group '§f" + groupName + "§7' has been changed to '§f" + integer + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The tag power of the group '§f" + groupName + "§7' has been changed to '§f" + integer + "§7'.")));
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "pleas use: §fdefault, tagpower")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"pleas use: §fdefault, tagpower")));
                    }

                }

                else if (args.length >= 6 && args.length < 8 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("add")) {

                    // Implementiere Logik für "/permission group [group] perm add [permission] [true/false] [time]"
                  final   String permission = args[4];
                  final   boolean value = parseBoolean(args[5]);
                    String time = args.length == 7 ? args[6] : "LIFETIME";

                    if (!time.equalsIgnoreCase("LIFETIME")){
                        LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                        LocalDateTime calculatedDateTime = currentDateTime.plusDays(Integer.parseInt(time)); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        time = calculatedDateTime.format(dateTimeFormatter);
                    }
                    if (pg.getPermissions().stream().noneMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){

                        pg.getPermissions().add(new PermissionAble(permission, value, time));
                        CloudPermissionAPI.getInstance().updateGroup(pg);

                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' now has the permission '§f" + permission + "§7@§f"+time+"§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' now has the permission '§f" + permission + "§7@§f"+time+"§7'.")));

                    }else {

                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +"The group '§f" + groupName + "§7' has alreasy the permission '§f" + permission + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' has alreasy the permission '§f" + permission + "§7'.")));

                    }
                } else if (args.length == 5 && args[2].equalsIgnoreCase("perms") && args[3].equalsIgnoreCase("remove")) {
                    String permission = args[4];
                    // Implementiere Logik für "/permission group [group] perm remove [permission]"

                    if (pg != null && pg.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        pg.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
                        CloudPermissionAPI.getInstance().updateGroup(pg);

                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' no longer has the permission '§f" + permission + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' no longer has the permission '§f" + permission + "§7'.")));

                    }else {

                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' does not have the permission '§f" + permission + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' does not have the permission '§f" + permission + "§7'.")));

                    }
                } else if (args.length >= 4 && args[2].equalsIgnoreCase("include")) {
                    // Implementiere Logik für "/permission group [group] include [group] [time]"
                  final   String includedGroup = args[3];
                    String time = args.length >= 5 ? args[4] : "LIFETIME";
                    if (pg.getIncluded().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(includedGroup))) {
                        if (CloudPermissionAPI.getInstance().getGroups().stream().noneMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(includedGroup))) {

                            if (veloPlayer != null)
                                veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The specified group '§f" + includedGroup + "§7' was not found.")));
                            else
                                 BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The specified group '§f" + includedGroup + "§7' was not found.")));
                            return;
                        }
                        if (!time.equalsIgnoreCase("LIFETIME")){
                            LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                            LocalDateTime calculatedDateTime = currentDateTime.plusDays(Integer.parseInt(time)); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                            time = calculatedDateTime.format(dateTimeFormatter);

                        }

                        pg.getIncluded().add(new IncludedAble(includedGroup, time));
                        CloudPermissionAPI.getInstance().updateGroup(pg);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' now inherits from the group '§f"+includedGroup+"§7@§f"+time+"§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' now inherits from the group '§f"+includedGroup+"§7@§f"+time+"§7'.")));

                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' does already inherit from the group '§f" + includedGroup + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' does already inherit from the group '§f" + includedGroup + "§7'.")));
                    }


                } else if (args.length >= 4 && args[2].equalsIgnoreCase("exclude")) {
                   final String excludedGroup = args[3];
                    // Implementiere Logik für "/permission group [group] exclude [group]"
                    if (pg.getIncluded().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(excludedGroup))){
                        pg.getIncluded().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(excludedGroup));
                        CloudPermissionAPI.getInstance().excludeGroup(groupName, excludedGroup );
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The group '§f" + groupName + "§7' no longer inherits from the group '§f" + excludedGroup + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +   "The group '§f" + groupName + "§7' no longer inherits from the group '§f" + excludedGroup + "§7'.")));
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' does not inherit from the group '§f" +excludedGroup + "§7'.")));
                        else
                             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group '§f" + groupName + "§7' does not inherit from the group '§f" +excludedGroup + "§7'.")));

                    }
                }else {
                    sendMessage(proxiedPlayer, veloPlayer);
                }
            }else {
                if (veloPlayer != null)
                    veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "The group you are looking for was not found, please check that it is spelled correctly.")));
                else
                     BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX +  "The group you are looking for was not found, please check that it is spelled correctly.")));
            }
        } else {
            sendMessage(proxiedPlayer, veloPlayer);

        }
    }


    private void sendMessage(ProxiedPlayer proxiedPlayer, Player veloPlayer){
        final Messages messages = CloudAPI.getInstance().getMessages();
        final String PREFIX = messages.getMessages().get("prefix").replace("&", "§");
        final Translator translator = new Translator();
        if (proxiedPlayer == null){
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] info")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] perms add [permission] [true/false] ([time])")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] perms remove [permission] ")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] group add [group] ([time])")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] group set [group] ([time])")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] group remove [group]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + " ")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission groups")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] info")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] create")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] delete")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] edit [type] [value]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] perm add [permission] [true/false] ([time])")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] perm remove [permission]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] include [group] [time]")));
            veloPlayer.sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] exclude [group]")));
        }else {
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] info")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] perm add [permission] [true/false] ([time])")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] perm remove [permission] ")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] group add [group] ([time])")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] group set [group] ([time])")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission user [user] group remove [group]")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + " ")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission groups")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] info")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] create")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] delete")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] edit [type] [value]")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] perms add [permission] [true/false] ([time])")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] perms remove [permission]")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] include [group] ([time])")));
             BungeeBootstrap.getInstance().audiences.player(proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(translator.translate(PREFIX + "/cloud permission group [group] exclude [group]")));
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> suggestion = new ArrayList<>();
        if (args.length == 0){
            suggestion.add("user");
            suggestion.add("group");
            suggestion.add("groups");
        }else if (args[0].equalsIgnoreCase("user")){
            if (args.length == 2){
                CloudPermissionAPI.getInstance().getPlayers().forEach(player -> suggestion.add(UUIDDriver.getUsername(player.getUuid())));
            }else if (args.length == 3){
                suggestion.add("info");
                suggestion.add("perms");
                suggestion.add("group");
            }else if (args.length == 4 && !args[2].equalsIgnoreCase("info")){
                suggestion.add("add");
                suggestion.add("remove");
                if (args[2].equalsIgnoreCase("group")){
                    suggestion.add("set");
                }
            }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("set")){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> suggestion.add(permissionGroup.getGroup()));
            }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> suggestion.add(permissionGroup.getGroup()));
            }else if (args.length == 5 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("remove")){
                CloudPermissionAPI.getInstance().getPlayer(args[1]).getGroups().forEach(includedAble -> suggestion.add(includedAble.getGroup()));
            }else if (args.length == 6 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("set")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }else if (args.length == 6 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("add")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }else if (args.length == 6 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("true");
                suggestion.add("false");
            }else if (args.length == 5 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("remove")){
                CloudPermissionAPI.getInstance().getPlayer(args[1]).getPermissions().forEach(permissionAble -> suggestion.add(permissionAble.getPermission()));
            }else if (args.length == 7 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }

        }else if (args[0].equalsIgnoreCase("group")){
            if (args.length == 2){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> suggestion.add(permissionGroup.getGroup()));
            }else if (args.length == 3){
                suggestion.add("info");
                suggestion.add("create");
                suggestion.add("delete");
                suggestion.add("edit");
                suggestion.add("perms");
                suggestion.add("include");
                suggestion.add("exclude");
            }else if (args.length == 4 && args[2].equalsIgnoreCase("edit")){
                suggestion.add("default");
                suggestion.add("tagpower");
            }else if (args.length == 5 && args[2].equalsIgnoreCase("edit") && args[3].equalsIgnoreCase("default")){
                suggestion.add("true");
                suggestion.add("false");
            }else if (args.length == 4 && args[2].equalsIgnoreCase("perms")){
                suggestion.add("add");
                suggestion.add("remove");
            }else if (args.length == 6 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("true");
                suggestion.add("false");
            }else if (args.length == 7 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("add")){
                suggestion.add("LIFETIME");
                suggestion.add("120");
                suggestion.add("90");
                suggestion.add("60");
                suggestion.add("30");
                suggestion.add("15");
            }else if (args.length == 5 && args[2].equalsIgnoreCase("perms")&& args[3].equalsIgnoreCase("remove")){
                CloudPermissionAPI.getInstance().getGroup(args[1]).getPermissions().forEach(permissionAble -> suggestion.add(permissionAble.getPermission()));
            }else if (args.length == 4 && args[2].equalsIgnoreCase("exclude")){

                CloudPermissionAPI.getInstance().getGroup(args[1]).getIncluded().forEach(includedAble -> suggestion.add(includedAble.getGroup()));
            }else if (args.length == 4 && args[2].equalsIgnoreCase("include")){
                CloudPermissionAPI.getInstance().getGroups().forEach(permissionGroup -> {
                    if (!permissionGroup .getGroup().equalsIgnoreCase(args[1])) {
                        suggestion.add(permissionGroup.getGroup());
                    }else {
                        return;
                    }
                });
            }else if (args.length == 5 && args[2].equalsIgnoreCase("include")){
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
