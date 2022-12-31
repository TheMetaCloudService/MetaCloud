package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;
import java.util.function.Consumer;

@CommandInfo(command = "group", aliases = {"g"}, ENdescription = "here you can manage your groups", DEdescription = "hier kannst du deine Gruppen verwalten")
public class GroupCommand extends CommandAdapter {
    @Override
    public boolean performCommand(CommandAdapter command, String[] args) {


        if (args.length == 0){
            sendHelp();
        }else if (args.length == 1){
            if (args[0].equalsIgnoreCase("create")){
                Driver.getInstance().getMessageStorage().setuptype= "GROUP";
                Driver.getInstance().getTerminalDriver().joinSetup();
            }else if (args[0].equalsIgnoreCase("list")){
                if ( Driver.getInstance().getGroupDriver().getAll().isEmpty()){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "es wurde keine gruppe gefunden [§egroup create§r]", "no group was found [§egroup create§7]");
                    return false;
                }
                Driver.getInstance().getGroupDriver().getAll().forEach(group -> {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, group.getGroup() +"~" + group.getGroupType() + " | "+ group.getStorage().getRunningNode());
                });
            }else {
                sendHelp();
            }
        }else {
            //todo: make the hole command
        }

        return false;
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
            commands.add("switchmaintenance");
        }

        return commands;
    }

    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §egroup create §7~ um eine neue Gruppe zu erstellen",
                " >> §egroup create §7~ to create a new group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §egroup list §7~ zeigt Ihnen alle derzeit verfügbaren Gruppen an",
                " >> §egroup list §7~ shows you all groups that are currently available");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §egroup <group> delete §7~ eine bestehende Gruppe löschen",
                " >> §egroup <group> delete §7~ delete an existing group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §egroup <group> switchmaintenance §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §egroup <group> switchmaintenance §7~ to switch the maintenance on and off again");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §egroup <group> info §7~ um die Wartung ein- und wieder auszuschalten",
                " >> §egroup <group> info §7~ to switch the maintenance on and off again");
    }
}
