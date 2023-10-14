package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.terminal.animation.AnimationDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "group", aliases = {"g", "template", "temp"}, ENdescription = "here you can manage your groups", DEdescription = "hier kannst du deine Gruppen verwalten")
public class GroupCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args.length == 1){
            if (args[0].equalsIgnoreCase("create")){
                Driver.getInstance().getMessageStorage().setupType = "GROUP";
                Driver.getInstance().getTerminalDriver().joinSetup();
            }else if (args[0].equalsIgnoreCase("list")){
                if ( Driver.getInstance().getGroupDriver().getAll().isEmpty()){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "es wurde keine gruppe gefunden '§fgroup create§r'", "no group was found '§fgroup create§7'");
                    return;
                }
              ArrayList<Group> groups =   Driver.getInstance().getGroupDriver().getAll();
                for (int i = 0; i != groups.size() ; i++) {
                    Group group =  groups.get(i);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, group.getGroup() +"~" + group.getGroupType() + " | "+ group.getStorage().getRunningNode());
                }
            }else {
                sendHelp();
            }
        }else {
            if (args.length== 2){
                if (args[1].equalsIgnoreCase("delete")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Driver.getInstance().getGroupDriver().delete(group);
                        CloudManager.serviceDriver.delete.add(group);
                        CloudManager.serviceDriver.getServices(group).forEach(taskedService -> CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName()));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                "die angegebene Gruppe '§f"+group+"§r' wurde erfolgreich gelöscht",
                                "the specified group '§f"+group+"§r' was successfully deleted");

                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }


                }else  if (args[1].equalsIgnoreCase("info")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Group raw = Driver.getInstance().getGroupDriver().load(group);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                "Dies ist die Konfiguration von §b" + group + "§r:§f\n" +new ConfigDriver().convert(raw),
                                "this is the config from §b" + group + "§r:§f\n" + new ConfigDriver().convert(raw));
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else {
                    sendHelp();
                }
            }else  if (args.length== 3) {
                if (args[1].equalsIgnoreCase("setmaintenance")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Group raw = Driver.getInstance().getGroupDriver().load(group);
                        raw.setMaintenance(args[2].equalsIgnoreCase("true"));
                        Driver.getInstance().getGroupDriver().update(group, raw);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                "Die Wartungen wurde für die Gruppe bearbeitet",
                                "The maintenance has been processed for the group");
                        Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                    }else if (group.equals("--all")) {
                        Driver.getInstance().getGroupDriver().getAll().forEach(group1 -> {
                            group1.setMaintenance(args[2].equalsIgnoreCase("true"));
                            Driver.getInstance().getGroupDriver().update(group, group1);
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + group, new ConfigDriver().convert(group1));
                        });
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                "Die Wartungen wurde für alle bearbeitet",
                                "The maintenance has been processed for all groups");
                    }else{
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else  if (args[1].equalsIgnoreCase("settemplate")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if (Driver.getInstance().getTemplateDriver().get().contains(args[2].replace(" ", ""))){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.getStorage().setTemplate(args[2].replace(" ", ""));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                    "Die Vorlage der Gruppe wurde geändert, sie wird nun von der angegebenen Vorlage gestartet",
                                    "The template of the group has been changed, it is now started from the specified template");
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "das Template wurde nicht gefunden",
                                    "the template was not found");
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else if (args[1].equalsIgnoreCase("setminamount")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setMinimalOnline(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                    "Die Mindestanzahl der Dienste würde für die Gruppe bearbeitet werden",
                                    "The minimum service number would be edited for the group");
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "du kannst nur eine zahl angeben",
                                    "you can only specify one number");
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else  if (args[1].equalsIgnoreCase("setpriority")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setPriority(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                    "Die Priorität würde geändert werden",
                                    "The priority would be changed");
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "du kannst nur eine zahl angeben",
                                    "you can only specify one number");
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else  if (args[1].equalsIgnoreCase("setJavaEnvironment")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Group raw = Driver.getInstance().getGroupDriver().load(group);
                        raw.getStorage().setJavaEnvironment(args[2]);
                        Driver.getInstance().getGroupDriver().update(group, raw);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                "Die Java-Umgebung wurde auf '§f"+args[2]+"§r' gesetzt",
                                "The Java environment was set to '§f"+args[2]+"§r'");
                        Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else  if (args[1].equalsIgnoreCase("setmaxplayers")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setMaxPlayers(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                    "Die maximale Anzahl der Spieler wurde für die Gruppe geändert",
                                    "The maximum number of players was changed for the group");
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "du kannst nur eine zahl angeben",
                                    "you can only specify one number");
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else  if (args[1].equalsIgnoreCase("setstartnewpercen")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setStartNewPercent(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                    "Die Prozent zahl würde erfolgreich für die Gruppe geändert",
                                    "The percentage number would be successfully changed for the group");
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "du kannst nur eine zahl angeben",
                                    "you can only specify one number");
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else if (args[1].equalsIgnoreCase("setpermission")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].contains(".") ){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setPermission(args[2]);
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                    "Die Berechtigung für die Gruppe wurde erfolgreich geändert",
                                    "The permission was successfully changed for the group");
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "die Erlaubnis muss mit einem Punkt versehen sein",
                                    "the permission must have a dot included");
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else  if (args[1].equalsIgnoreCase("setmaxamount")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+") || args[2].equalsIgnoreCase("-1")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setMinimalOnline(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                    "Die maximale Anzahl von Servern, die online sein können, wurde für die Gruppe geändert",
                                    "The maximum number of servers that can be online has been changed for the group");
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "du kannst nur eine zahl angeben",
                                    "you can only specify one number");
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                                "the group '§f"+group+"§r' was not found");
                    }
                }else {
                        sendHelp();
                }
            }else {
                sendHelp();
            }
        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> commands = new ArrayList<>();
        if (args.length == 0){
            commands.add("create");
            commands.add("list");
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> commands.add(group.getGroup()));
            commands.add("--all");
        }
        if (args.length == 1 && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("list")){
            commands.add("delete");
            commands.add("info");
            commands.add("setmaintenance");
            commands.add("setmaxplayers");
            commands.add("settemplate");
            commands.add("setminamount");
            commands.add("setjavaenvironment");
            commands.add("setpriority");
            commands.add("setstartnewpercen");
            commands.add("setpermission");
            commands.add("setmaxamount");
        }
        if (args.length == 2){
            if (args[1].equalsIgnoreCase("setmaintenance") && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("list")) {
                commands.add("true");
                commands.add("false");
            }  if (args[1].equalsIgnoreCase("settemplate") && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("list")) {
                ArrayList<String> rawtemplates = Driver.getInstance().getTemplateDriver().get();
                commands.addAll(rawtemplates);
            }
        }

        return commands;
    }

    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup create §7~ um eine neue Gruppe zu erstellen",
                " >> §fgroup create §7~ to create a new group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup list §7~ zeigt Ihnen alle derzeit verfügbaren Gruppen an",
                " >> §fgroup list §7~ shows you all groups that are currently available");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] delete §7~ eine bestehende Gruppe löschen",
                " >> §fgroup [group] delete §7~ delete an existing group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] info §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §fgroup [group] info §7~ to switch the maintenance on and off again");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group/--all] setmaintenance [true/false] §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §fgroup [group/--all] setmaintenance [true/false] §7~ to switch the maintenance on and off again");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] setmaxplayers [count] §7~ um die maximalen Spielerzahlen ändern",
                " >> §fgroup [group] setmaxplayers [count] §7~ to change the maximum number of players");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] setminamount [count] §7~ die Anzahl der Server festlegen, die immer online sein sollen",
                " >> §fgroup [group] setminamount [count] §7~ set the number of servers that should always be online");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] setmaxamount [count] §7~ die Anzahl der Server festlegen, die maximal online sein sollen",
                " >> §fgroup [group] setmaxamount [count] §7~ set the maximum number of servers that should be online");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] settemplate [template] §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §fgroup [group] settemplate [template] §7~ to switch the maintenance on and off again");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] setjavaenvironment [path] §7~ setze die java umgebung neu",
                " >> §fgroup [group] setjavaenvironment [path] §7~ reset the java environment");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] setpriority [priority] §7~ die Priorität einer Gruppe festlegen",
                " >> §fgroup [group] setpriority [priority] §7~ set the priority of a group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] setpermission [permission] §7~ die Beitrittserlaubnis für die Gruppe festlegen",
                " >> §fgroup [group] setpermission [permission] §7~ set the join permission of the group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup [group] setstartnewpercen [startnewpercen] §7~ die Zahl in Prozent festlegen, wann ein neuer Server gestartet werden soll",
                " >> §fgroup [group] setstartnewpercen [startnewpercen] §7~ set the number in percent when to start a new server");
    }
}
