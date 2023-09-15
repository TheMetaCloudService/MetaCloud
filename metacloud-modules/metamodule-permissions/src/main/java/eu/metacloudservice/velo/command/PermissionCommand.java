/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velo.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.moduleside.config.*;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PermissionCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player){
            String[] args = invocation.arguments();
            if (player.hasPermission("metacloud.command.perms")){
                Messages messages = CloudAPI.getInstance().getMessages();
                String PREFIX = messages.getPrefix().replace("&", "§");
                if (args.length == 0){
                    sendHelp(player);
                }else if (args[0].equalsIgnoreCase("user")){
                    if (args.length > 1){
                        String target = args[1];
                        if (CloudPermissionAPI.getInstance().getPlayers().stream().anyMatch(player1 -> player1.getUuid().equalsIgnoreCase(UUIDDriver.getUUID(target)))){

                            PermissionPlayer pp = CloudPermissionAPI.getInstance().getPlayer(target);
                            if (args.length == 2){
                                player.sendMessage(Component.text(PREFIX + "player: " + pp.getUuid()));
                                player.sendMessage(Component.text(PREFIX + "able groups: "));
                                pp.getGroups().forEach(includedAble -> player.sendMessage(Component.text(PREFIX + "- " + includedAble.getGroup() + " ~ " + includedAble.getTime())));
                                player.sendMessage(Component.text(PREFIX + "able permissions: "));
                                pp.getPermissions().forEach(permissionAble -> player.sendMessage(Component.text(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime())));
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
                                            CloudPermissionAPI.getInstance().updatePlayer(pp);
                                            player.sendMessage(Component.text(PREFIX + "The player '§f"+target+"§r' has successfully received the permission '§f"+permission+"§r@§f"+formattedDateTime+"§r' ."));
                                        }else {

                                            player.sendMessage(Component.text(PREFIX + "The player '§f"+target+"§r' already has this permissions"));
                                        }
                                    }

                                }else{
                                    sendHelp(player);
                                }
                            }else if (args[2].equalsIgnoreCase("removeperm")){
                                if (args.length == 4){
                                    if (pp != null && pp.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]))){
                                        pp.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]));
                                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                                        player.sendMessage(Component.text(PREFIX + "The player '§f" + target + "§r' has now been deprived of the permission '" + args[3] + "'."));


                                    }else {
                                        player.sendMessage(Component.text(PREFIX + "The player '§f"+target+"§r' does not have this permission."));
                                    }
                                }else {
                                    sendHelp(player);
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
                                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                                        player.sendMessage(Component.text(PREFIX +  "The player '§f"+target+"§r' has successfully received the group '§f"+group+"§r@§f§"+formattedDateTime+"§r'."));

                                    }else {
                                        player.sendMessage(Component.text(PREFIX + "The player '§f"+target +"§r' already has this group."));
                                    }
                                }else{
                                    sendHelp(player);
                                }
                            }else if (args[2].equalsIgnoreCase("removegroup")){
                                if (args.length == 4){
                                    if (pp.getGroups().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]))){
                                        pp.getGroups().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]));
                                        if (pp.getGroups().isEmpty()){
                                            ArrayList<PermissionGroup> groups  = (ArrayList<PermissionGroup>) CloudPermissionAPI.getInstance().getGroups().stream().filter(PermissionGroup::getIsDefault).toList();
                                            groups.forEach(permissionGroup ->      pp.getGroups().add(new IncludedAble(permissionGroup.getGroup(), "LIFETIME")));
                                        }
                                        CloudPermissionAPI.getInstance().updatePlayer(pp);
                                        player.sendMessage(Component.text(PREFIX +  "The group '§f"+target+"§r' has been successfully removed."));
                                    }else {
                                        player.sendMessage(Component.text(PREFIX + "The player '§f"+target+"§r' has not included the group."));
                                    }
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                sendHelp(player);
                            }
                        }else {
                            player.sendMessage( Component.text(PREFIX +"The specified player '§f" + target + "§r' was not found."));
                        }
                    }else {
                        sendHelp(player);
                    }
                }else if (args[0].equalsIgnoreCase("group")){
                    if (args.length > 1){
                        String group = args[1];
                        if (CloudPermissionAPI.getInstance().isGroupExists(group)){
                            PermissionGroup pg = CloudPermissionAPI.getInstance().getGroup(group);
                            if (args.length == 2){
                                player.sendMessage(Component.text(PREFIX + "Group name: "+ pg.getGroup()));
                                player.sendMessage(Component.text(PREFIX + "Default: "+ pg.getIsDefault()));
                                player.sendMessage(Component.text(PREFIX + "Tag-Power: "+ pg.getTagPower()));
                                player.sendMessage(Component.text(PREFIX + "Prefix: "+ pg.getPrefix()));
                                player.sendMessage(Component.text(PREFIX + "Suffix: "+ pg.getSuffix()));
                                player.sendMessage(Component.text(PREFIX + "Able groups: "));
                                pg.getIncluded().forEach(includedAble ->  player.sendMessage(Component.text(PREFIX + "- " +includedAble.getGroup() + " ~ " + includedAble.getTime() )));
                                player.sendMessage(Component.text(PREFIX + "Able permissions: "));
                                pg.getPermissions().forEach(permissionAble ->        player.sendMessage(Component.text(PREFIX + "- " + permissionAble.getPermission() + " ~ " + permissionAble.getAble() + " ~ " + permissionAble.getTime())));
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
                                            CloudPermissionAPI.getInstance().updateGroup(pg);
                                            player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' now has the permission '§f" + permission + "§r@§f"+formattedDateTime+"§r'."));

                                        }else {
                                            player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' has alreasy the permission '§f" + permission + "§r'."));

                                        }
                                    }else {
                                        sendHelp(player);
                                    }

                                }else{
                                    sendHelp(player);
                                }
                            }else if (args[2].equalsIgnoreCase("removeperm")){
                                if (args.length == 4){
                                    if (pg != null && pg.getPermissions().stream().anyMatch(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]))){
                                        pg.getPermissions().removeIf(permissionAble -> permissionAble.getPermission().equalsIgnoreCase(args[3]));
                                        CloudPermissionAPI.getInstance().updateGroup(pg);
                                        player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' no longer has the permission '§f" + args[3] + "§r'."));
                                    }else {
                                        player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' does not have the permission '§f" + args[3] + "§r'."));

                                    }
                                }else {
                                    sendHelp(player);
                                }
                            }else if (args[2].equalsIgnoreCase("include")){

                                if (args.length == 5){
                                    String target= args[3];

                                    int time = args[4].equalsIgnoreCase("lifetime") ? -1 : Integer.parseInt(args[4]);

                                    assert pg != null;
                                    if (pg.getIncluded().stream().noneMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(target))){
                                        if (CloudPermissionAPI.getInstance().getGroups().stream().noneMatch(permissionGroup -> permissionGroup.getGroup().equalsIgnoreCase(target))){
                                            player.sendMessage(Component.text(PREFIX +   "The specified group '§f" + target + "§r' was not found."));
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
                                        CloudPermissionAPI.getInstance().updateGroup(pg);
                                        player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' now inherits from the group '§f"+target+"§r@§f§"+formattedDateTime+"§r'."));

                                    }else {
                                        player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' does already inherit from the group '§f" + target + "§r'."));

                                    }
                                }else{
                                    sendHelp(player);
                                }

                            }else if (args[2].equalsIgnoreCase("exclude")){
                                if (args.length == 4){
                                    assert pg != null;
                                    if (pg.getIncluded().stream().anyMatch(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]))){
                                        pg.getIncluded().removeIf(includedAble -> includedAble.getGroup().equalsIgnoreCase(args[3]));

                                        CloudPermissionAPI.getInstance().excludeGroup(group, args[3] );
                                        player.sendMessage(Component.text(PREFIX +    "The group '§f" + group + "§r' no longer inherits from the group '§f" + args[3] + "§r'."));

                                    }else {
                                        player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' does not inherit from the group '§f" + args[3] + "§r'."));

                                    }
                                }else {
                                    sendHelp(player);
                                }
                            }else if (args[2].equalsIgnoreCase("setdefault")) {
                                if (args.length == 4){
                                    assert pg != null;
                                    pg.setIsDefault(!pg.getIsDefault());
                                    CloudPermissionAPI.getInstance().updateGroup(pg);
                                    player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' has now been set as default."));
                                }else {
                                    sendHelp(player);
                                }
                            }else if (args[2].equalsIgnoreCase("settagpower")) {
                                if (args.length == 4){
                                    assert pg != null;
                                    Integer.valueOf(args[0]);
                                    int integer = Integer.parseInt(args[3]);
                                    pg.setTagPower(integer);
                                    CloudPermissionAPI.getInstance().updateGroup(pg);
                                    player.sendMessage(Component.text(PREFIX + "The tag power of the group '§f" + group + "§r' has been changed to '§f" + integer + "§r'."));
                                }else {
                                    sendHelp(player);
                                }
                            }else if (args[2].equalsIgnoreCase("create")){
                                player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' already exists."));
                            }else  if (args[2].equalsIgnoreCase("delete")){
                                CloudPermissionAPI.getInstance().deleteGroup(group);
                                player.sendMessage(Component.text(PREFIX + "The group '§f" + group + "§r' has been successfully deleted."));
                            }else {
                                sendHelp(player);
                            }
                        }else {


                            if (args.length == 3){
                                if (args[2].equalsIgnoreCase("create")){
                                    CloudPermissionAPI.getInstance().createGroup(new PermissionGroup(args[1], false, 99,"§b" +group + " §8| §7" ,"",new ArrayList<>(), new ArrayList<>()));
                                    player.sendMessage(Component.text(PREFIX +   "The group '§f" + group + "§r' has been successfully created."));
                                }else {
                                    sendHelp(player);
                                }
                            }else {
                                player.sendMessage(Component.text(PREFIX + "The specified group '§f" + group + "§r' was not found."));}
                        }
                    }else {
                        sendHelp(player);
                    }
                }else if (args[0].equalsIgnoreCase("groups")) {
                    StringBuilder result = new StringBuilder();
                    if (!CloudPermissionAPI.getInstance().getGroups().isEmpty()) {
                        // Füge den ersten Gruppennamen hinzu
                        result.append(CloudPermissionAPI.getInstance().getGroups().get(0).getGroup());

                        // Füge die restlichen Gruppennamen mit einem Komma hinzu
                        for (int i = 1; i < CloudPermissionAPI.getInstance().getGroups().size(); i++) {
                            result.append(", ").append(CloudPermissionAPI.getInstance().getGroups().get(i).getGroup());
                        }
                    }
                    player.sendMessage(Component.text(PREFIX + "groups:"));
                    player.sendMessage(Component.text(PREFIX + result.toString()));

                }else{
                    sendHelp(player);
                }
            }else {
                player.sendMessage(Component.text("§8▷ §7The network uses §bMetacloud§8 [§a"+ Driver.getInstance().getMessageStorage().version+"§8]"));
                player.sendMessage(Component.text("§8▷ §fhttps://metacloudservice.eu/"));
            }
        }
    }

    public void sendHelp(Player player) {
        Messages messages = CloudAPI.getInstance().getMessages();
        String PREFIX = messages.getPrefix().replace("&", "§");
        player.sendMessage(Component.text(PREFIX + "/perms user [player]"));
        player.sendMessage(Component.text(PREFIX + "/perms user [player] addperm [permission] [true/false] [time]"));
        player.sendMessage(Component.text(PREFIX + "/perms user [player] removeperm [permission]"));
        player.sendMessage(Component.text(PREFIX + "/perms user [player] addgroup [group] [time]"));
        player.sendMessage(Component.text(PREFIX + "/perms user [player] removegroup [group]"));
        player.sendMessage(Component.text(PREFIX + ""));
        player.sendMessage(Component.text(PREFIX + "/perms groups"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group]"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] create"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] delete"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] setdefault"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] settargpower [power]"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] addperm [permission] [true/false] [time]"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] removeperm [permission]"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] include [group] [time]"));
        player.sendMessage(Component.text(PREFIX + "/perms group [group] exclude [group]"));


    }

    @Override
    public List<String> suggest(Invocation invocation) {
        List<String> collection = new ArrayList<>();
        String[] args = invocation.arguments();
        Configuration configuration = CloudPermissionAPI.getInstance().getConfig();

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
