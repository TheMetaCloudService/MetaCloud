package eu.metacloudservice.moduleside.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.moduleside.MetaModule;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


@CommandInfo(command = "permissions", DEdescription = "Verwaltung aller Berechtigungen von Gruppen und Spielern", ENdescription = "manage all permissions of groups and players", aliases = "perms")
public class PermissionsCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args[0].equalsIgnoreCase("groups")){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                    "Liste der Gruppen:",
                    "List of groups:");

            MetaModule.permissionDriver.getGroups().forEach(groupEntry -> {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " > " + groupEntry.getName() +" | default: " + groupEntry.isDefaultGroup());
            });

        }else if (args[0].equalsIgnoreCase("group")){
            if (args.length >= 2){
                String group = args[1];
                if (args.length == 2){
                    if (MetaModule.permissionDriver.getGroup(group) == null)
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die angegebene Gruppe wurde nicht gefunden",
                                "The specified group was not found");


                        else {
                        GroupEntry entry = MetaModule.permissionDriver.getGroup(group);
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "name: " + entry.getName());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "default: " + entry.isDefaultGroup());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "prefix: " + entry.getPrefix());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "suffix: " + entry.getSuffix());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "tag power: " + entry.getTagPower());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "includes: " );
                        entry.getInclude().forEach((s, s2) -> {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " - " + s + " | " + s2 );
                        });
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "permissions: " );
                        entry.getPermissions().forEach((perms, options) -> {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " - " +perms + " | §f" + options.get("set") + " [@"+options.get("cancellationAt")+"]");
                        });
                    }
                }else  if (args.length == 3){
                    if (args[2].equalsIgnoreCase("create")){
                        if (MetaModule.permissionDriver.getGroup(group) != null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die Gruppe existiert bereits",
                                    "The group already exists");
                        else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die Gruppe '§f"+group+"§r' wurde erfolgreich erstellt",
                                    "The group '§f"+group+"§r' was successfully created");
                            GroupEntry defaultGroup = new GroupEntry();
                            defaultGroup.setName(group);
                            defaultGroup.setPrefix("§7"+group+" §8| §7");
                            defaultGroup.setSuffix("");
                            defaultGroup.setInclude(new HashMap<>());
                            defaultGroup.setTagPower(0);
                            defaultGroup.setPermissions(new HashMap<>());
                            defaultGroup.setDefaultGroup(false);
                            MetaModule.permissionDriver.createGroup(defaultGroup);


                        }

                    }else if (args[2].equalsIgnoreCase("delete")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");
                        else {
                            MetaModule.permissionDriver.deleteGroup(group);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die Gruppe '§f"+group+"§r' wurde erfolgreich gelöscht",
                                    "The group '§f"+group+"§r' was successfully deleted");
                        }
                    }else if (args[2].equalsIgnoreCase("changedefault")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");
                        else {

                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Der Default status wurde für die Gruppe '§f"+group+"§r' geändert",
                                    "The default status was changed for the group '§f"+group+"§r'");
                            MetaModule.permissionDriver.changeDefault(group);

                        }
                    }else{
                        sendHelp();
                    }
                }else if (args.length == 4){

                    if (args[2].equalsIgnoreCase("settagpower")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");
                        else {
                            String power = args[3];
                            if (power.matches("[0-9]+")){
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Power wurde erfolgreich gesetzt",
                                        "The power was set successfully");
                                MetaModule.permissionDriver.setGroupPower(group, Integer.parseInt(power));
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Bitte gebe als Power eine zahl an",
                                        "Please enter a number as power");
                            }
                        }
                    }else if (args[2].equalsIgnoreCase("delperm")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");
                            else {

                            String permission = args[3];

                            if (MetaModule.permissionDriver.hasGroupPermission(group,permission)){
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Permission '§f"+permission+"§r' wurde der Gruppe '§f"+group+"§r' entfernt",
                                        "The '§f"+permission+"§r' permission was removed from the '§f"+group+"§r' group.");
                                MetaModule.permissionDriver.removePermissionFromGroup(group, permission);
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Du kannst die Permission nicht entfernen da sie nicht vergeben ist",
                                        "You cannot remove the permission because it is not granted");
                            }
                        }


                    }else if (args[2].equalsIgnoreCase("exclude")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");

                      else {
                            String include = args[3];
                            if (MetaModule.permissionDriver.isInclude(group, include)){
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f"+include+"§r' ist nicht mehr in der Gruppe '§f"+group+"§r' enthalten.",
                                        "The '§f"+include+"§r' group is no longer included in the '§f"+group+"§r' group");
                                MetaModule.permissionDriver.removeInclude(group, include);
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f"+include+"§r'  ist nicht in der Gruppe '§f"+group+"§r' enthalten.",
                                        "The group '§F"+include+"§r' is not included with the group '§f"+group+"§r'.");
                            }
                        }

                    }else {
                        sendHelp();
                    }
                }else if (args.length == 5){
                     if (args[2].equalsIgnoreCase("include")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");

                        else {
                            String include = args[3];
                            String time = args[4];
                            if (!MetaModule.permissionDriver.isInclude(group, include)){
                                if (time.matches("[0-9]+") ||time.equalsIgnoreCase("LIFETIME")){
                                    if (time.equalsIgnoreCase("LIFETIME")){
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Die Gruppe '§f"+include+"§r' ist nun enthalten | §fLIFETIME",
                                                "The '§f"+include+"§r' group is now included  | §fLIFETIME");
                                        MetaModule.permissionDriver.addInclude(group, include, -1);

                                    }else {
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Die Gruppe '§f"+include+"§r' ist nun enthalten | §f"+time+" Tag(e)",
                                                "The '§f"+include+"§r' group is now included | §f"+time+" day(s)");
                                        MetaModule.permissionDriver.addInclude(group, include, Integer.parseInt(time));
                                    }
                                }else {
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Bitte geben Sie die Anzahl der Tage oder LIFETIME ein",
                                            "Please enter the number of days or Lifetime");
                                }
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f"+include+"§r'  ist bereits in der Gruppe '§f"+group+"§r' enthalten",
                                        "The group '§f"+include+"§r' is already included in the group '§f"+group+"§r'");
                            }
                        }
                    }else {
                        sendHelp();
                    }
                }else if (args.length == 6) {
                    if (args[2].equalsIgnoreCase("addperm")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");

                        else {
                            String permission = args[3];
                            String bol = args[4];
                            String timeraw = args[5];

                            if (!MetaModule.permissionDriver.hasGroupPermission(group, permission)){
                                if (timeraw.matches("[0-9]+") ||timeraw.equalsIgnoreCase("LIFETIME")){
                                    int time = args[5].equalsIgnoreCase("LIFETIME") ? -1 : Integer.parseInt(args[5]) ;
                                    if (bol.equalsIgnoreCase("TRUE") || bol.equalsIgnoreCase("FALSE")){
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Die Berechtigungen wurden der Gruppe '§f"+group+"§r' hinzugefügt. -> '§f"+permission+"@"+bol.equalsIgnoreCase("TRUE")+" ["+time+"]"+"§r'",
                                                "The permissions have been added to the '§f"+group+"§r' group. -> '§f"+permission+"@"+bol.equalsIgnoreCase("TRUE")+" ["+time+"]"+"§r");

                                        MetaModule.permissionDriver.addPermissionToGroup(group, permission, bol.equalsIgnoreCase("TRUE"), time);
                                    }else {
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Bitte geben Sie ein Bolean an, das True oder False bedeutet",
                                                "Please specify a Bolean that means True or False");
                                    }
                                }else {
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Bitte geben Sie die Anzahl der Tage oder LIFETIME ein",
                                            "Please enter the number of days or Lifetime");
                                }

                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe hat bereits das Recht",
                                        "The group already has the right");
                            }
                        }
                    }
                }else  {
                sendHelp();
                }
            }else {
                sendHelp();
            }
        }else if (args[0].equalsIgnoreCase("user")){
            if (args.length >= 2) {
                String user = args[1];
                if (args.length == 2){
                    if (!MetaModule.permissionDriver.existsPlayer(user))
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der User war noch nie auf dem Netzwerk",
                                "The user has never been on the network");

                  else {
                        PlayerConfiguration configuration = MetaModule.permissionDriver.getPlayer(user);
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "name: " + user);
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "UUID: " + configuration.getUUID());
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "groups: ");
                        configuration.getGroups().forEach(givenGroup -> {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " - " + givenGroup.getGroup() + " | §f" + givenGroup.getCancellationAt());
                        });

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "permissions: ");
                        configuration.getPermissions().forEach((perms, options) -> {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " - " +perms + " | §f" + options.get("set") + " [@"+options.get("cancellationAt")+"]");
                        });
                    }
                }else if (args.length == 4){
                    if (!MetaModule.permissionDriver.existsPlayer(user))
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der User war noch nie auf dem Netzwerk",
                                "The user has never been on the network");
                    else {
                        if (args[2].equalsIgnoreCase("delperm")){
                            String perms = args[3];
                            if (MetaModule.permissionDriver.hasPermission(user, perms)){
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Permission '§f"+perms+"§r' wurde '§f"+user+"§r' entfernt",
                                        "The '§f"+perms+"§r' permission was removed from '§f"+user+"§r.");
                                MetaModule.permissionDriver.removePermissionFromPlayer(user, perms);

                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Du kannst die Permission nicht entfernen da sie nicht vergeben ist",
                                        "You cannot remove the permission because it is not granted");
                            }
                        }  else    if (args[2].equalsIgnoreCase("delgroup")){
                            String group = args[3];
                            if (MetaModule.permissionDriver.playerHasGroup(user, group)){
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der Spieler '§f"+user+"§r' ist nun nicht mehr in der Gruppe '§f"+group+"§r'.",
                                        "The player '§f"+user+"§r' is not longer in the group '§f"+group+"§r.");
                                MetaModule.permissionDriver.removePlayerGroup(user, group);

                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Du kannst die group nicht entfernen da sie nicht vergeben ist",
                                        "You cannot remove the group because it is not granted");
                            }
                        }else {
                            sendHelp();
                        }
                    }
                }else if (args.length == 5){
                    if (!MetaModule.permissionDriver.existsPlayer(user))
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der User war noch nie auf dem Netzwerk",
                                "The user has never been on the network");
                    else {
                        if (args[2].equalsIgnoreCase("addgroup")){
                            String group = args[3];
                            String time = args[4];
                            if (!MetaModule.permissionDriver.playerHasGroup(user, group)){
                                if (time.matches("[0-9]+") ||time.equalsIgnoreCase("LIFETIME")){
                                    if (MetaModule.permissionDriver.getGroup(group) == null){
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Die Gruppe wurde nicht gefunden",
                                                "The group was not found");
                                        return;
                                    }
                                    if (time.equalsIgnoreCase("LIFETIME")){
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Der User '§f"+user+"§r' ist nun in der Gruppe '§f"+group+"§r' | §fLIFETIME",
                                                "The user '§f "+user+"§r' is now in the group '§f "+group+"§r' | §fLIFETIME");
                                        MetaModule.permissionDriver.addRankToPlayer(user, group, -1);

                                    }else {
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Der User '§f"+user+"§r' ist nun in der Gruppe '§f"+group+"§r' | §f"+time+" Tag(e) ",
                                                "The user '§f "+user+"§r' is now in the group '§f "+group+"§r' | §f"+time+" day(s)");
                                        MetaModule.permissionDriver.addRankToPlayer(user, group, Integer.parseInt(time));
                                    }
                                }else {
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Bitte geben Sie die Anzahl der Tage oder LIFETIME ein",
                                            "Please enter the number of days or Lifetime");
                                }

                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der User hat bereits die Gruppe",
                                        "The user has already created the group");
                            }
                        }else {
                            sendHelp();
                        }
                    }
                }else if (args.length == 6){
                    if (!MetaModule.permissionDriver.existsPlayer(user))
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der User war noch nie auf dem Netzwerk",
                                "The user has never been on the network");
                    else {
                        if (args[2].equalsIgnoreCase("addperm")){
                            String permission = args[3];
                            String bol = args[4];
                            Integer time = args[5].equalsIgnoreCase("LIFETIME") ? -1 : Integer.parseInt(args[5]) ;

                            if (!MetaModule.permissionDriver.hasPermission(permission, permission)){
                                if (bol.equalsIgnoreCase("TRUE") || bol.equalsIgnoreCase("FALSE")){
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Die Berechtigungen wurden den Spieler '§f"+user+"§r' hinzugefügt. -> '§f"+permission+"@"+bol.equalsIgnoreCase("TRUE")+" ["+time+"]"+"§r'",
                                            "The permissions have been added to '§f"+user+"§r'. -> '§f"+permission+"@"+bol.equalsIgnoreCase("TRUE")+" ["+time+"]"+"§r");

                                    MetaModule.permissionDriver.addPermissionToPlayer(user, permission, bol.equalsIgnoreCase("TRUE"), time);
                                }else {
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Bitte geben Sie ein Bolean an, das True oder False bedeutet",
                                            "Please specify a Bolean that means True or False");
                                }
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der Spieler hat bereits das Recht",
                                        "The player already has the right");
                            }
                        }else {
                            sendHelp();
                        }
                    }
                }else {
                    sendHelp();
                }
            }else {
                sendHelp();
            }



        }else {
            sendHelp();
        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {

        ArrayList<String> returns = new ArrayList<>();

        if (args.length == 0){
            returns.add("groups");
            returns.add("group");
            returns.add("user");
        }else {
            if (args[0].equalsIgnoreCase("group")){
                if (args.length == 1){
                    MetaModule.permissionDriver.getGroups().forEach(groupEntry -> returns.add(groupEntry.getName()));
                }else {
                    if (args.length == 2){
                        returns.add("create");
                        returns.add("delete");
                        returns.add("changedefault");
                        returns.add("settagpower");
                        returns.add("addperm");
                        returns.add("delperm");
                        returns.add("include");
                        returns.add("exclude");
                    }else {
                        if (args[2].equalsIgnoreCase("addperm")){
                            if (args.length == 4){
                                returns.add("true");
                                returns.add("false");
                            }
                            if (args.length == 5){
                                returns.add("30");
                                returns.add("90");
                                returns.add("120");
                                returns.add("180");
                                returns.add("240");
                                returns.add("300");
                                returns.add("360");
                                returns.add("LIFETIME");
                            }
                        }if (args[2].equalsIgnoreCase("include")){
                            if (args.length == 4){
                                returns.add("30");
                                returns.add("90");
                                returns.add("120");
                                returns.add("180");
                                returns.add("240");
                                returns.add("300");
                                returns.add("360");
                                returns.add("LIFETIME");
                            }
                        }
                    }
                }
            }else if (args[0].equalsIgnoreCase("user")){

                if (args.length == 1){
                    MetaModule.permissionDriver.getPlayers().forEach(playerConfiguration -> returns.add(new UUIDDriver().getUsername(playerConfiguration.getUUID())));
                }else if (args.length == 2){
                    returns.add("addperm");
                    returns.add("delperm");
                    returns.add("addgroup");
                    returns.add("delgroup");
                }else {
                    if (args[2].equalsIgnoreCase("delgroup")){
                        if (args.length == 3){
                            MetaModule.permissionDriver.getGroups().forEach(groupEntry -> returns.add(groupEntry.getName()));
                        }
                    }else if (args[2].equalsIgnoreCase("addgroup")){
                        if (args.length == 3){
                            MetaModule.permissionDriver.getGroups().forEach(groupEntry -> returns.add(groupEntry.getName()));
                        }else if (args.length == 4){
                            returns.add("30");
                            returns.add("90");
                            returns.add("120");
                            returns.add("180");
                            returns.add("240");
                            returns.add("300");
                            returns.add("360");
                            returns.add("LIFETIME");
                        }
                    } else if (args[2].equalsIgnoreCase("addperm")){
                        if (args.length == 4){
                            returns.add("true");
                            returns.add("false");
                        }else if (args.length == 5){
                            returns.add("30");
                            returns.add("90");
                            returns.add("120");
                            returns.add("180");
                            returns.add("240");
                            returns.add("300");
                            returns.add("360");
                            returns.add("LIFETIME");
                        }
                    }
                }
            }
        }
        return returns;
    }

    public void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms groups §7~ sehe alle Gruppen",
                " >> §fperms groups §7~ see all groups");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] §7~ sehe alle Informationen von der Gruppe",
                " >> §fperms group [group] §7~ see all information from the group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] create §7~ erstelle eine neue Gruppe",
                " >> §fperms group [group] create §7~ create a new group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] delete §7~ lösche eine gruppe",
                " >> §fperms group [group] delete §7~ delete a group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] changedefault §7~ ändere den Default status",
                " >> §fperms group [group] changedefault §7~ change the default status");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] settagpower [power] §7~ ändere die Tag power einer Gruppes",
                " >> §fperms group [group] settagpower [power] §7~ change the tag power of a group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] addperm [permission] [set] [time] §7~ füge der Gruppe eine permission hinzus",
                " >> §fperms group [group] addperm [permission] [set] [time] §7~ add a permission to the group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] delperm [permission] §7~ entfernen einer Gruppe die permission",
                " >> §fperms group [group] delperm [permission] §7~ remove a group's permission");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] include [group] [time] §7~ eine Gruppe in eine andere Gruppe aufnehmene",
                " >> §fperms group [group] include [group] [time] §7~ add a group to another group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms group [group] exclude [group] §7~ eine Gruppe von einer anderen Gruppe ausschließen",
                " >> §fperms group [group] exclude [group] §7~ exclude a group from another group");

        //PLAYER
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [user] §7~ sehe alle Informationen von einen Spieler",
                " >> §fperms user [user] §7~ see all information from one player");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [user] addperm [permission] [set] [time] §7~ um einem Spieler die permissions zu erteilen",
                " >> §fperms user [user] addperm [permission] [set] [time] §7~ to add permission to a player");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [user] delperm [permission] §7~ um einem Spieler die permissions weg zunehmen",
                " >> §fperms user [user] delperm [permission] §7~ to take away the permissions of a player");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [user] addgroup [group] [time] §7~ um einen Spieler zur einer Gruppe zu addenn",
                " >> §fperms user [user] addgroup [group] [time] §7~ to add a player to a group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fperms user [user] delgroup [group] §7~ um einen Spieler von einer Gruppe zu entfernen",
                " >> §fperms user [user] delgroup [group] §7~ to remove a player from a group");
    }
}