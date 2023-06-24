package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;
import java.util.LinkedList;

@CommandInfo(command = "queue", DEdescription = "Verwalte die Warteschlange, Sehe was startet", ENdescription = "Manage the queue, See what starts", aliases = {"servicequeue", "waitingline"})
public class QueueCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else {
            if (args[0].equalsIgnoreCase("list")){
             LinkedList<String> start =  CloudManager.queueDriver.getQueue_startup();
             LinkedList<String> stop =  CloudManager.queueDriver.getQueue_shutdown();

                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        "Hier sind alle Dienste, die sich in der Startwarteschlange befinden: ",
                        "here are all services that are in the start queue: ");
                start.forEach(s -> Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        " >> " + s,
                        " >> " + s));
                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        "Hier sind alle Dienste, die sich in der Stopwarteschlange befinden: ",
                        "here are all services that are in the stop queue: ");
                stop.forEach(s -> Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        " >> " + s,
                        " >> " + s));

            }else if (args[0].equalsIgnoreCase("remove")){
                String service = args[1];
                LinkedList<String> start =  CloudManager.queueDriver.getQueue_startup();
                LinkedList<String> stop =  CloudManager.queueDriver.getQueue_shutdown();

                if (stop.stream().noneMatch(s -> s.equalsIgnoreCase(service))&& start.stream().noneMatch(s -> s.equals(s))){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der service ist in keiner Warteschlange vertreten",
                            "The service is not represented in any queue");
                }else if (stop.stream().anyMatch(s -> s.equalsIgnoreCase(service))){
                    CloudManager.queueDriver.getQueue_shutdown().removeIf(s -> s.equalsIgnoreCase(service));
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der service wird nun nicht mehr gestoppt",
                            "The service is now no longer stopped");
                }else {
                    CloudManager.queueDriver.getQueue_startup().removeIf(s -> s.equalsIgnoreCase(service));
                    CloudManager.serviceDriver.unregistered(service);
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der service wird nun nicht mehr gestartet",
                            "The service is now no longer started");
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
            commands.add("remove");
            commands.add("list");
        }
        return commands;
    }

    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fqueue list §7~ Sehe alle Services in der Queue",
                " >> §fqueue list §7~ See all services in the queue");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fqueue remove [service] §7~ remove Services von der Queue",
                " >> §fqueue remove [service] §7~ remove services from the queue");
    }
}
