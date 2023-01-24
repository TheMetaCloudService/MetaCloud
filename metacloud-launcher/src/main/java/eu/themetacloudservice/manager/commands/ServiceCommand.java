package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.manager.CloudManager;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedService;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;
import java.util.function.Consumer;

@CommandInfo(command = "service", DEdescription = "verwalte alle Services", ENdescription = "manage all services", aliases = {"serv", "task"})
public class ServiceCommand extends CommandAdapter {


    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args.length==1){
            if (args[0].equalsIgnoreCase("list")){

                if ( CloudManager.serviceDriver.getServices().isEmpty()){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "Es wurden keine Services gefunden", "no services were found");
                    return;
                }
                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase("InternalNode") || NettyDriver.getInstance().nettyServer.isChannelFound(managerConfigNodes.getName())).forEach(managerConfigNodes -> {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, managerConfigNodes.getName()+": ");
                    CloudManager.serviceDriver.getServices().stream().filter(taskedService -> taskedService.getEntry().getNode().equalsIgnoreCase(managerConfigNodes.getName())).forEach(taskedService -> {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " > "+taskedService.getEntry().getServiceName() + "~" + taskedService.getEntry().getStatus().toString() + "-" + taskedService.getEntry().getUsedPort());
                    });
                });
            }else {
                sendHelp();
            }
        }else if (args.length == 2){
            if (args[0].equalsIgnoreCase("stopGroup")){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().find(group)){
                    CloudManager.serviceDriver.getServices(group).forEach(taskedService -> CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName()));
                    }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Die Gruppe '§f"+group+"§r' wurde nicht gefunden",
                            "the group '§f"+group+"§r' was not found");
                }

            }else  if (args[0].equalsIgnoreCase("stop")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                    CloudManager.serviceDriver.unregister(service);
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f"+service+"§r' wurde nicht gefunden",
                            "the service '§f"+service+"§r' was not found");
                }
            }else  if (args[0].equalsIgnoreCase("sync")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f"+service+"§r' ist sincronisiert ",
                            "the service '§f"+service+"§r' is sincronized ");
                    CloudManager.serviceDriver.getService(service).handelSync();
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f"+service+"§r' wurde nicht gefunden",
                            "the service '§f"+service+"§r' was not found");
                }
            }else  if (args[0].equalsIgnoreCase("info")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                   TaskedService taskedService = CloudManager.serviceDriver.getService(service);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service name: §f" + taskedService.getEntry().getServiceName());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Node: §f" + taskedService.getEntry().getNode());
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "Service Gruppe: §f" + taskedService.getEntry().getGroupName(),
                            "Service group: §f" + taskedService.getEntry().getGroupName());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Port: §f" + taskedService.getEntry().getUsedPort());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Players: §f" + taskedService.getEntry().getCurrentPlayers());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Status: §f" + taskedService.getEntry().getStatus());
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f"+service+"§r' wurde nicht gefunden",
                            "the service '§f"+service+"§r' was not found");
                }
            }else {
                sendHelp();
            }
        }else {
            if (args[0].equalsIgnoreCase("run")) {
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().find(group)) {
                    if (args[2].matches("[0-9]+")) {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Befehl war erfolgreich",
                                "the command was successful");

                        Group gdata = Driver.getInstance().getGroupDriver().load(group);
                        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                        for (int i = 0; i != Integer.parseInt(args[2]); i++) {
                            String id = "";
                            if (config.getUuid().equals("INT")){
                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group));
                            }else if (config.getUuid().equals("RANDOM")){
                                id = CloudManager.serviceDriver.getFreeUUID();
                            }
                                    CloudManager.serviceDriver.register(new TaskedEntry(
                                    CloudManager.serviceDriver.getFreePort(gdata.getGroupType().equalsIgnoreCase("PROXY")),
                                    gdata.getGroup(),
                                    gdata.getGroup() + config.getSplitter() + id,
                                    gdata.getStorage().getRunningNode(), config.getUseProtocol()));
                            Driver.getInstance().getMessageStorage().canUseMemory = Driver.getInstance().getMessageStorage().canUseMemory - gdata.getUsedMemory();
                        }

                    } else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Du kannst nur eine zahl angeben",
                                "you can only specify one number");
                    }
                } else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Die Gruppe '§f" + group + "§r' wurde nicht gefunden",
                            "the group '§f" + group + "§r' was not found");
                }

            } else if (args[0].equalsIgnoreCase("execute")) {
                StringBuilder msg = new StringBuilder();
                String service = args[1];
                for (int i = 2; i < args.length; i++) {
                    msg.append(args[i]).append(" ");
                }

                if (CloudManager.serviceDriver.getService(service) != null) {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "der Befehl wurde an den Service gesendet",
                            "the command was sent to the service");
                    CloudManager.serviceDriver.getService(service).handelExecute(msg.toString());
                } else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f" + service + "§r' wurde nicht gefunden",
                            "the service '§f" + service + "§r' was not found");
                }
            } else {
                sendHelp();
            }
            }
        }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> commands = new ArrayList<>();

        if (args.length == 0){
            commands.add("list");
            commands.add("run");
            commands.add("stopgroup");
            commands.add("stop");
            commands.add("sync");
            commands.add("info");
            commands.add("execute");
        }else if (args.length == 1 & !args[0].equalsIgnoreCase("list") & !args[0].equalsIgnoreCase("stopgroup")& !args[0].equalsIgnoreCase("run")){
            CloudManager.serviceDriver.getServices().forEach(taskedService -> commands.add(taskedService.getEntry().getServiceName()));
        }else if (args.length == 1){
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> commands.add(group.getGroup()));
        }
        return commands;
    }


    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice list §7~ zeigt dir alle laufenden Service",
                " >> §fservice list §7~ shows you all running service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice run <group> <count> §7~ um ein Service zu starten",
                " >> §fservice run <group> <count> §7~ to start a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice stopgroup <group> §7~ um eine ganze Gruppe stoppen",
                " >> §fservice stopgroup <group> §7~ to stop an entire group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice stop <service> §7~ um ein Service zu stoppen",
                " >> §fservice stop <service> §7~ to stop a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice sync <service> §7~ um einen Service zu synchronisieren",
                " >> §fservice sync <service> §7~ to synchronize a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice info <service> §7~ zeigt die alle Infos zu einen bestimmten Service",
                " >> §fservice info <service> §7~ shows all the info about a particular service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice execute <service> <command> §7~ um einen Befehl auf dem Server auszuführen",
                " >> §fservice execute <service> <command> §7~ to execute a command on the server");
    }

}
