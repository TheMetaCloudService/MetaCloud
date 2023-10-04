/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.subcommand;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.api.PluginCommand;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.moduleside.config.IncludedAble;
import eu.metacloudservice.moduleside.config.PermissionAble;
import eu.metacloudservice.moduleside.config.PermissionPlayer;
import eu.metacloudservice.storage.UUIDDriver;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class PermissionCommand extends PluginCommand {

    @Override
    public void performCommand(PluginCommand command, ProxiedPlayer proxiedPlayer, Player veloPlayer, org.bukkit.entity.Player bukkitPlayer, String[] args) {

        if (args.length == 0){
            sendMessage(proxiedPlayer, veloPlayer);
        }else if (args[0].equalsIgnoreCase("user")){
            handleUser(proxiedPlayer, veloPlayer, args);
        }else if (args[0].equalsIgnoreCase("group")){
            handleGroup(proxiedPlayer, veloPlayer, args);
        }else {
            sendMessage(proxiedPlayer, veloPlayer);
        }
    }

    private void handleUser(ProxiedPlayer proxiedPlayer, Player veloPlayer, String[] args){
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        if (args.length >= 2 && args[0].equalsIgnoreCase("user")) {
            String username = args[1];
            if (CloudPermissionAPI.getInstance().getPlayers().stream().anyMatch(player -> player.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(username)) )){
                PermissionPlayer pp = CloudPermissionAPI.getInstance().getPlayer(username);
                if (args.length >= 3 && args[2].equalsIgnoreCase("info")) {
                    // Implementiere Logik für "/permission user [user] info"#
                    if (proxiedPlayer == null){
                        veloPlayer.sendMessage(Component.text(PREFIX + "player: " + pp.getUuid()));
                        veloPlayer.sendMessage(Component.text(PREFIX + "able groups: "));
                        pp.getGroups().forEach(includedAble -> veloPlayer.sendMessage(Component.text(PREFIX + "- " + includedAble.getGroup() + " ~ " + includedAble.getTime())));
                        veloPlayer.sendMessage(Component.text(PREFIX + "able permissions: "));
                        pp.getPermissions().forEach(permissionAble -> veloPlayer.sendMessage(Component.text(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime())));

                    }else {
                        proxiedPlayer.sendMessage(PREFIX + "player: " + pp.getUuid());
                        proxiedPlayer.sendMessage(PREFIX + "able groups: ");
                        pp.getGroups().forEach(includedAble -> proxiedPlayer.sendMessage(PREFIX + "- " + includedAble.getGroup() + " ~ " + includedAble.getTime()));
                        proxiedPlayer.sendMessage(PREFIX + "able permissions: ");
                        pp.getPermissions().forEach(permissionAble -> proxiedPlayer.sendMessage(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime()));
                    }
                } else if (args.length >= 6 && args[2].equalsIgnoreCase("perm") && args[4].equalsIgnoreCase("add")) {
                    String permission = args[3];
                    boolean value = !args[5].equalsIgnoreCase("true") && !args[5].equalsIgnoreCase("false") || Boolean.valueOf(args[5]);
                    String time = args.length >= 7 ? args[6] : "LIFETIME";
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
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f"+username+"§r' has successfully received the permission '§f"+permission+"§r@§f"+time+"§r' ."));
                        else
                            proxiedPlayer.sendMessage(PREFIX + "The player '§f"+username+"§r' has successfully received the permission '§f"+permission+"§r@§f"+time+"§r' .");
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f"+username+"§r' already has this permissions"));
                        else
                            proxiedPlayer.sendMessage(PREFIX + "The player '§f"+username+"§r' already has this permissions");
                    }
                } else if (args.length >= 4 && args[2].equalsIgnoreCase("perm") && args[3].equalsIgnoreCase("remove")) {
                    String permission = args[4];
                    // Implementiere Logik für "/permission user [user] perm remove [permission]"
                    if (pp.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission))){
                        pp.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(permission));
                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f" + username + "§r' has now been deprived of the permission '" + permission + "'."));
                        else
                            proxiedPlayer.sendMessage(PREFIX + "The player '§f" + username + "§r' has now been deprived of the permission '" + permission + "'.");


                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f"+username+"§r' does not have this permission."));
                        else
                            proxiedPlayer.sendMessage(PREFIX +  "The player '§f"+username+"§r' does not have this permission.");
                    }

                } else if (args.length >= 5 && args[2].equalsIgnoreCase("group") && args[4].equalsIgnoreCase("add")) {
                    String group = args[3];
                    String time = args.length >= 6 ? args[5] : "LIFETIME";
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
                            veloPlayer.sendMessage(Component.text(PREFIX + "The player '§f"+username+"§r' has successfully received the group '§f"+group+"§r@§f§"+time+"§r'."));
                        else
                            proxiedPlayer.sendMessage(PREFIX + "The player '§f"+username+"§r' has successfully received the group '§f"+group+"§r@§f§"+time+"§r'.");
                    }else {
                        if (veloPlayer != null)
                            veloPlayer.sendMessage(Component.text(PREFIX +  "The player '§f"+username +"§r' already has this group."));
                        else
                            proxiedPlayer.sendMessage(PREFIX + "The player '§f"+username +"§r' already has this group.");
                    }

                }else if (args.length >= 4 && args[2].equalsIgnoreCase("group") && args[3].equalsIgnoreCase("remove")) {
                    String group = args[4];
                    // Implementiere Logik für "/permission user [user] group remove [group]"
                }else {
                    sendMessage(proxiedPlayer, veloPlayer);
                }
            }else {
                if (veloPlayer != null)
                    veloPlayer.sendMessage(Component.text(PREFIX + "The player you are looking for was not found, please check that it is spelled correctly."));
                else
                    proxiedPlayer.sendMessage(PREFIX +  "The player you are looking for was not found, please check that it is spelled correctly.");
            }
        } else {
            sendMessage(proxiedPlayer, veloPlayer);
        }
    }

    private void handleGroup(ProxiedPlayer proxiedPlayer, Player veloPlayer, String[] args){
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        if (args.length >= 1 && args[0].equalsIgnoreCase("groups")) {
            // Implementiere Logik für "/permission groups"
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("group")) {
            String groupName = args[1];

            if (args.length >= 3 && args[2].equalsIgnoreCase("info")) {
                // Implementiere Logik für "/permission group [group] info"
            } else if (args.length >= 3 && args[2].equalsIgnoreCase("create")) {
                // Implementiere Logik für "/permission group [group] create"
            } else if (args.length >= 3 && args[2].equalsIgnoreCase("delete")) {
                // Implementiere Logik für "/permission group [group] delete"
            } else if (args.length >= 5 && args[2].equalsIgnoreCase("edit")) {
                String type = args[3];
                String value = args[4];
                // Implementiere Logik für "/permission group [group] edit [type] [value]"
            } else if (args.length >= 7 && args[2].equalsIgnoreCase("perm") && args[4].equalsIgnoreCase("add")) {
                String permission = args[3];
                boolean value = Boolean.parseBoolean(args[5]);
                String time = args.length >= 8 ? args[7] : "unbegrenzt";
                // Implementiere Logik für "/permission group [group] perm add [permission] [true/false] [time]"
            } else if (args.length >= 4 && args[2].equalsIgnoreCase("perm") && args[3].equalsIgnoreCase("remove")) {
                String permission = args[4];
                // Implementiere Logik für "/permission group [group] perm remove [permission]"
            } else if (args.length >= 5 && args[2].equalsIgnoreCase("include")) {
                String includedGroup = args[3];
                String time = args[4];
                // Implementiere Logik für "/permission group [group] include [group] [time]"
            } else if (args.length >= 4 && args[2].equalsIgnoreCase("exclude")) {
                String excludedGroup = args[3];
                // Implementiere Logik für "/permission group [group] exclude [group]"
            }else {
                sendMessage(proxiedPlayer, veloPlayer);
            }
        } else {
            sendMessage(proxiedPlayer, veloPlayer);

        }
    }


    private void sendMessage(ProxiedPlayer proxiedPlayer, Player veloPlayer){
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        if (proxiedPlayer == null){
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission user [user] info"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission user [user] perm add [permission] [true/false] ([time])"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission user [user] perm remove [permission] "));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission user [user] group add [group] ([time])"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission user [user] group remove [group]"));
            veloPlayer.sendMessage(Component.text(PREFIX + " "));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission groups"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] info"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] create"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] delete"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] edit [type] [value]"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] perm add [permission] [true/false] ([time])"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] perm remove [permission]"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] include [group] [time]"));
            veloPlayer.sendMessage(Component.text(PREFIX + "/cloud permission group [group] exclude [group]"));
        }else {
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission user [user] info");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission user [user] perm add [permission] [true/false] [time]");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission user [user] perm remove [permission] ");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission user [user] group add [group] [time]");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission user [user] group set [group] [time]");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission user [user] group remove [group]");
            proxiedPlayer.sendMessage(PREFIX + " ");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission groups");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] info");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] create");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] delete");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] edit [type] [value]");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] perm add [permission] [true/false] [time]");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] perm remove [permission]");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] include [group] [time]");
            proxiedPlayer.sendMessage(PREFIX + "/cloud permission group [group] exclude [group]");
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
