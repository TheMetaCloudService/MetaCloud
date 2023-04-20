package eu.metacloudservice.moduleside.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.moduleside.MetaModule;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class PermissionsCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args[0].equalsIgnoreCase("group")){
            if (args.length >= 2){
                String group = args[1];
                if (args.length == 2){
                    if (MetaModule.permissionDriver.getGroup(group) == null)
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die angegebene Gruppe wurde nicht gefunden",
                                "The specified group was not found");


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
                    entry.getPermissions().forEach((s, aBoolean) -> {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " - " + s + " | " + aBoolean );
                    });
                }else  if (args.length == 3){
                    if (args[2].equalsIgnoreCase("create")){
                        if (MetaModule.permissionDriver.getGroup(group) != null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die Gruppe existiert bereits",
                                    "The group already exists");


                        GroupEntry defaultGroup = new GroupEntry();
                        defaultGroup.setName(group);
                        defaultGroup.setPrefix("§7"+group+" §8| §7");
                        defaultGroup.setSuffix("");
                        defaultGroup.setInclude(new HashMap<>());
                        defaultGroup.setTagPower(0);
                        defaultGroup.setPermissions(new HashMap<>());
                        defaultGroup.setDefaultGroup(true);

                        MetaModule.permissionDriver.createGroup(defaultGroup);

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f"+group+"§r' wurde erfolgreich erstellt",
                                "The group '§f"+group+"§r' was successfully created");

                    }else if (args[2].equalsIgnoreCase("delete")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");

                        MetaModule.permissionDriver.deleteGroup(group);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Die Gruppe '§f"+group+"§r' wurde erfolgreich gelöscht",
                                "The group '§f"+group+"§r' was successfully deleted");
                    }else if (args[2].equalsIgnoreCase("changedefault")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");

                        MetaModule.permissionDriver.changeDefault(group);

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Default status wurde für die Gruppe '§f"+group+"§r' geändert",
                                "The default status was changed for the group '§f"+group+"§r'");

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

                                MetaModule.permissionDriver.setGroupPower(group, Integer.parseInt(power));
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Power wurde erfolgreich gesetzt",
                                        "The power was set successfully");
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Dbitte gebe als Power eine zahl an",
                                        "please enter a number as power");
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
                                MetaModule.permissionDriver.removePermissionFromGroup(group, permission);
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Permission '§f"+permission+"§r' wurdeen der Gruppe '§f"+group+"§r' hinzugefügt",
                                        "The '§f"+permission+"§r' permission was added to the '§f"+group+"§r' group.");
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
                                        "The '§f"+include+"§r' group is no longer included in the '§F"+group+"§R' group");
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
                    if (args[2].equalsIgnoreCase("addperm")){
                        if (MetaModule.permissionDriver.getGroup(group) == null)
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Die angegebene Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                    "The specified group '§f"+group+"§r' was not found");

                        else {
                            String permission = args[3];
                            String bol = args[4];

                            if (!MetaModule.permissionDriver.hasGroupPermission(group, permission)){
                                if (bol.equalsIgnoreCase("TRUE") || bol.equalsIgnoreCase("FALSE")){
                                    MetaModule.permissionDriver.addPermissionToGroup(group, permission, bol.equalsIgnoreCase("TRUE"));
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Die Berechtigungen wurden der Gruppe '§f"+group+"§r' hinzugefügt. -> '§f"+permission+"@"+bol.equalsIgnoreCase("TRUE")+"§r'",
                                            "The permissions have been added to the '§f"+group+"§r' group. -> '§f"+permission+"@"+bol.equalsIgnoreCase("TRUE")+"§r");
                                }else {
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Bitte geben Sie ein Bolean an, das True oder False bedeutet",
                                            "Please specify a Bolean that means True or False");
                                }
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe hat bereits das Recht",
                                        "The group already has the right");
                            }
                        }
                    }else if (args[2].equalsIgnoreCase("include")){
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
                                        MetaModule.permissionDriver.addInclude(group, include, -1);
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Die Gruppe '§f"+include+"§r' ist nun enthalten | §fLIFETIME",
                                                "The '§f"+include+"§r' group is now included  | §fLIFETIME");
                                    }else {
                                        MetaModule.permissionDriver.addInclude(group, include, Integer.parseInt(time));
                                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                                "Die Gruppe '§f"+include+"§r' ist nun enthalten | §f"+time+" Tage",
                                                "The '§f"+include+"§r' group is now included | §f"+time+" days");
                                    }
                                }else {
                                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                            "Bitte geben Sie die Anzahl der Tage oder LIFETIME einn",
                                            "Please enter the number of days or Lifetime");
                                }
                            }else {
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Die Gruppe '§f"+include+"§r'  ist bereits in der Gruppe '§f"+group+"§r' enthalten",
                                        "The group '§f "+include+"§r' is already included in the group '§f "+group+"§r'");
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
        }else if (args[0].equalsIgnoreCase("user")){
            if (args.length >= 2) {
                String user = args[1];
                if (args.length == 2){
                    if (MetaModule.permissionDriver.getPlayer(user) == null)
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
                        configuration.getPermissions().forEach((s, aBoolean) -> {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " - " +s + " | §f" + aBoolean);
                        });
                    }
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
        return null;
    }

    public void sendHelp(){
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
                " >> §fperms group [group] addperm [permission] [set] §7~ füge der Gruppe eine permission hinzus",
                " >> §fperms group [group] addperm [permission] [set] §7~ add a permission to the group");
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
                " >> §fperms user [user] addperm [permission] [set] §7~ um einem Spieler die permissions zu erteilen",
                " >> §fperms user [user] addperm [permission] [set] §7~ to add permission to a player");
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