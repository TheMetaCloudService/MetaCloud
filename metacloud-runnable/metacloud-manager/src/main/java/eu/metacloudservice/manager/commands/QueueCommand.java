package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

@CommandInfo(command = "queue", description = "command-queue-description", aliases = {"servicequeue", "waitingline"})
public class QueueCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else {
            if (args[0].equalsIgnoreCase("list")){
                ConcurrentLinkedDeque<String> start =  CloudManager.queueDriver.getQueue_startup();
             ConcurrentLinkedDeque<String> stop =  CloudManager.queueDriver.getQueue_shutdown();
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage( "command-queue-service-in-queue"));

                start.forEach(s -> Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                        " >> " + s + "| START"));
                stop.forEach(s -> Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                        " >> " + s + " | STOP"));
            }else if (args[0].equalsIgnoreCase("remove")){
                String service = args[1];
                ConcurrentLinkedDeque<String> start =  CloudManager.queueDriver.getQueue_startup();
                ConcurrentLinkedDeque<String> stop =  CloudManager.queueDriver.getQueue_shutdown();
                if (stop.stream().noneMatch(s -> s.equalsIgnoreCase(service))&& start.stream().noneMatch(s -> s.equals(s))){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-queue-service-not-in-queue"));
                }else if (stop.stream().anyMatch(s -> s.equalsIgnoreCase(service))){
                    CloudManager.queueDriver.getQueue_shutdown().removeIf(s -> s.equalsIgnoreCase(service));
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-queue-service-remove"));

                }else {
                    CloudManager.queueDriver.getQueue_startup().removeIf(s -> s.equalsIgnoreCase(service));
                    CloudManager.serviceDriver.unregistered(service);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-queue-service-remove"));
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
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-queue-help-1"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-queue-help-2")
        );
    }
}
