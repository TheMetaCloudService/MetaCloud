package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
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
                Driver.getInstance().getMessageStorage().setuptype= "GROUP";
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
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                "die angegebene Gruppe '§f"+group+"§r' wurde erfolgreich gelöscht",
                                "the specified group '§f"+group+"§r' was successfully deleted");
                        CloudManager.serviceDriver.getServices(group).forEach(taskedService -> CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName()));
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
                if (args[1].equalsIgnoreCase("setmaintenace")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Group raw = Driver.getInstance().getGroupDriver().load(group);
                        raw.setMaintenance(args[2].equalsIgnoreCase("true"));
                        Driver.getInstance().getGroupDriver().update(group, raw);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                                "die Wartungsarbeiten der Gruppe '§f"+group+"§r' wurden geändert",
                                "the maintenance of the '§f"+group+"§r' group has been changed");
                        Driver.getInstance().getWebServer().updateRoute("/groups/" + raw.getGroup(), new ConfigDriver().convert(raw));
                    }else {
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
                                    "Die Vorlage wurde erfolgreich geändert",
                                    "The template was successfully modified");
                            Driver.getInstance().getWebServer().updateRoute("/groups/" + raw.getGroup(), new ConfigDriver().convert(raw));
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
                                    "Die Vorlage wurde erfolgreich geändert",
                                    "The template was successfully modified");
                            Driver.getInstance().getWebServer().updateRoute("/groups/" + raw.getGroup(), new ConfigDriver().convert(raw));
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
        }
        if (args.length == 1){
            commands.add("delete");
            commands.add("info");
            commands.add("setmaintenace");
            commands.add("settemplate");
            commands.add("setminamount");
        }
        if (args.length == 3){
            if (args[1].equalsIgnoreCase("setmaintenace")) {
                commands.add("true");
                commands.add("false");
            }  if (args[1].equalsIgnoreCase("settemplate")) {
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
                " >> §fgroup <group> delete §7~ eine bestehende Gruppe löschen",
                " >> §fgroup <group> delete §7~ delete an existing group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup <group> info §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §fgroup <group> info §7~ to switch the maintenance on and off again");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup <group> setmaintenace <true/false> §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §fgroup <group> setmaintenace <true/false> §7~ to switch the maintenance on and off again");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup <group> setminamount <count> §7~ die Anzahl der Server festlegen, die immer online sein sollen",
                " >> §fgroup <group> setminamount <count> §7~ set the number of servers that should always be online");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fgroup <group> settemplate <template> §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §fgroup <group> settemplate <template> §7~ to switch the maintenance on and off again");
    }
}
