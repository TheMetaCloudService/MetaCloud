package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.dummys.WhiteList;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@CommandInfo(command = "service", DEdescription = "verwalte alle Services", ENdescription = "manage all services", aliases = {"serv", "task", "start"})
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
               CloudManager.config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase("InternalNode") || NettyDriver.getInstance().nettyServer.isChannelFound(managerConfigNodes.getName())).forEach(managerConfigNodes -> {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, managerConfigNodes.getName()+": ");
                    CloudManager.serviceDriver.getServices().stream().filter(taskedService -> taskedService.getEntry().getNode().equalsIgnoreCase(managerConfigNodes.getName())).forEach(taskedService -> {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " > "+taskedService.getEntry().getServiceName() + "~" + taskedService.getEntry().getStatus().toString() + "-" + taskedService.getEntry().getUsedPort() +" §r(players: §f"+taskedService.getEntry().getCurrentPlayers()+"§r) ");
                    });
                });
            }else {
                sendHelp();
            }
        }else if (args.length == 2){
            if (args[0].equalsIgnoreCase("whitelist")){
                CloudManager.config.getWhitelist().forEach(s -> Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " > " + s));
            }else if (args[0].equalsIgnoreCase("stopGroup")){
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
            }else  if (args[0].equalsIgnoreCase("restart")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                    CloudManager.serviceDriver.getService(service).handelRestart();
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f"+service+"§r' wurde nicht gefunden",
                            "the service '§f"+service+"§r' was not found");
                }
            }else  if (args[0].equalsIgnoreCase("joinscreen")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null && CloudManager.serviceDriver.getService(service).getEntry().getStatus() != ServiceState.QUEUED){
                    CloudManager.serviceDriver.getService(service).handelScreen();
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f"+service+"§r' wurde nicht gefunden",
                            "the service '§f"+service+"§r' was not found");
                }
            }else  if (args[0].equalsIgnoreCase("sync")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null || service.equalsIgnoreCase("--all")){

                    if (service.equalsIgnoreCase("--all")){
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Alle Services wurden syncronisiert ",
                                "All services were synchronized");
                        CloudManager.serviceDriver.getServices().forEach(TaskedService::handelSync);
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Service '§f"+service+"§r' ist syncronisiert ",
                                "the service '§f"+service+"§r' is syncronized ");
                        CloudManager.serviceDriver.getService(service).handelSync();
                    }
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f"+service+"§r' wurde nicht gefunden",
                            "the service '§f"+service+"§r' was not found");
                }
            }else  if (args[0].equalsIgnoreCase("restartgroup")){
                String service = args[1];
                if (Driver.getInstance().getGroupDriver().load(service) != null){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Gruppe '§f"+service+"§r' wurde neu gestartet ",
                            "the group '§f"+service+"§r' was restarted ");
                    CloudManager.serviceDriver.getServices(service).forEach(TaskedService::handelRestart);
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Die Gruppe '§f"+service+"§r' wurde nicht gefunden",
                            "the group '§f"+service+"§r' was not found");
                }
            }else  if (args[0].equalsIgnoreCase("restartnode")){
                String service = args[1];
                if (((ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class)).getNodes().contains(service)){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Node '§f"+service+"§r' wurde neu gestartet ",
                            "the node '§f"+service+"§r' was restarted ");
                    CloudManager.serviceDriver.getServicesFromNode(service).forEach(TaskedService::handelRestart);
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Die Gruppe '§f"+service+"§r' wurde nicht gefunden",
                            "the group '§f"+service+"§r' was not found");
                }
            }else  if (args[0].equalsIgnoreCase("info")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                   TaskedService taskedService = CloudManager.serviceDriver.getService(service);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service name: §f" + taskedService.getEntry().getServiceName());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service uuid: §f" + taskedService.getEntry().getUUID());
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
                    int time = Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - taskedService.getEntry().getTime())));
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,"Service untätig: §f" + time+ " Sekunde(n)",
                            "Service IDLE: §f" + time+ " Second(s)");
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
                        Group gdata = Driver.getInstance().getGroupDriver().load(group);

                        if (gdata.getMaximalOnline() != -1){
                            if (gdata.getMaximalOnline() < (CloudManager.serviceDriver.getServices(group).size() + Integer.parseInt(args[2]))){
                                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                        "Der angegebene Service-Startbetrag kann nicht ausgeführt werden, da er die maximale Anzahl der Services überschreiten würde",
                                        "The specified service start amount cannot be executed because it would exceed the maximum services");
                                return;
                            }
                        }
                        for (int i = 0; i != Integer.parseInt(args[2]); i++) {
                            String id = "";
                            if (CloudManager.config.getUuid().equals("INT")){
                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group));
                            }else if (CloudManager.config.getUuid().equals("RANDOM")){
                                id = CloudManager.serviceDriver.getFreeUUID();
                            }
                                    CloudManager.serviceDriver.register(new TaskedEntry(
                                    CloudManager.serviceDriver.getFreePort(gdata.getGroupType().equalsIgnoreCase("PROXY")),
                                    gdata.getGroup(),
                                    gdata.getGroup() + CloudManager.config.getSplitter() + id,
                                    gdata.getStorage().getRunningNode(), CloudManager.config.getUseProtocol(), id));
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
                if (CloudManager.serviceDriver.getService(service) != null && NettyDriver.getInstance().nettyServer.isChannelFound(service) || service.equalsIgnoreCase("--all")) {

                  if ( service.equalsIgnoreCase("--all")){
                      Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                              "Der Befehl wurde an alle Services gesendet",
                              "The command was sent to all services");
                      CloudManager.serviceDriver.getServices().forEach(taskedService -> taskedService.handelExecute(msg.toString()));
                  }else {
                      Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                              "der Befehl wurde an den Service '§f"+service+"§r' gesendet",
                              "the command was sent to the service '§f"+service+"§r' ");
                      CloudManager.serviceDriver.getService(service).handelExecute(msg.toString());
                  }

                } else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Service '§f" + service + "§r' wurde nicht gefunden",
                            "the service '§f" + service + "§r' was not found");
                }
            } else if (args[0].equalsIgnoreCase("whitelist")) {
                String  option = args[1];
                String user = args[2];
                if (option.equalsIgnoreCase("add")){
                    if (!CloudManager.config.getWhitelist().contains(user)){
                        CloudManager.config.getWhitelist().add(user);
                        new ConfigDriver("./service.json").save(CloudManager.config);
                        WhiteList whitelistConfig = new WhiteList();
                        whitelistConfig.setWhitelist(CloudManager.config.getWhitelist());
                        Driver.getInstance().getWebServer().updateRoute("/default/whitelist", new ConfigDriver().convert(whitelistConfig));

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f" + user + "§r' wurde in die Whitelist aufgenommenn",
                                "the player '§f" + user + "§r' was added to the whitelistd");
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f" + user + "§r' wurde bereits gefunden",
                                "the player '§f" + user + "§r' was already found");
                    }
                }else if (option.equalsIgnoreCase("remove")){
                    if (CloudManager.config.getWhitelist().contains(user)){
                        CloudManager. config.getWhitelist().remove(user);
                        new ConfigDriver("./service.json").save(CloudManager.config);
                        WhiteList whitelistConfig = new WhiteList();
                         whitelistConfig.setWhitelist(CloudManager.config.getWhitelist());
                        Driver.getInstance().getWebServer().updateRoute("/default/whitelist", new ConfigDriver().convert(whitelistConfig));
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f" + user + "§r' wurde von der Whitelist entfernt",
                                "the player '§f" + user + "§r' was removed from the whitelist");
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Der Spieler '§f" + user + "§r' wurde nicht gefunden",
                                "the player '§f" + user + "§r' was not found");
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
            commands.add("list");
            commands.add("run");
            commands.add("stopgroup");
            commands.add("joinscreen");
            commands.add("restart");
            commands.add("stop");
            commands.add("sync");
            commands.add("info");
            commands.add("execute");
            commands.add("restartgroup");
            commands.add("restartnode");
            commands.add("whitelist");
        }else if (args.length == 1 && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("stopgroup") && ! args[0].equalsIgnoreCase("whitelist") && !args[0].equalsIgnoreCase("run") && !args[0].equalsIgnoreCase("restartgroup") && !args[0].equalsIgnoreCase("restartnode")){
            CloudManager.serviceDriver.getServices().forEach(taskedService -> commands.add(taskedService.getEntry().getServiceName()));
            if (args[0].equalsIgnoreCase("execute") || args[0].equalsIgnoreCase("sync")){
                commands.add("--all");
            }

        }else if (args.length == 1 && args[0].equalsIgnoreCase("whitelist")){
            commands.add("add");
            commands.add("remove");
        }else if (args.length== 1 && args[0].equalsIgnoreCase("restartnode")) {

            ((ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class)).getNodes().forEach(managerConfigNodes -> {
                commands.add(managerConfigNodes.getName());
            });
        }else if (args.length == 2 && args[0].equalsIgnoreCase("whitelist") && args[1].equalsIgnoreCase("remove")){
            commands.addAll(CloudManager.config.getWhitelist());
        }else if (args.length== 1 ){
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> commands.add(group.getGroup()));
        }


        return commands;
    }


    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice list §7~ zeigt dir alle laufenden Service",
                " >> §fservice list §7~ shows you all running service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice run [group] [count] §7~ um ein Service zu starten",
                " >> §fservice run [group] [count] §7~ to start a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice stopgroup [group] §7~ um eine ganze Gruppe stoppen",
                " >> §fservice stopgroup [group] §7~ to stop an entire group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice joinscreen [service] §7~ um einen Screen von einen Service zu joinen",
                " >> §fservice joinscreen [service] §7~ to join a screen from a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice stop [service] §7~ um ein service neu zustarten",
                " >> §fservice stop [service] §7~ to restart a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice restart [service] §7~ um ein Service zu stoppen",
                " >> §fservice restart [service] §7~ to stop a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice restartgroup [group] §7~ um ein Gruppe neu zustarten",
                " >> §fservice restartgroup [group] §7~ to restart a group");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice restartnode [node] §7~ um einen Node neu zustarten",
                " >> §fservice restartnode [node] §7~ to restart a node");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice sync [service/--all] §7~ um einen Service zu synchronisieren",
                " >> §fservice sync [service/--all] §7~ to synchronize a service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice info [service] §7~ zeigt die alle Infos zu einen bestimmten Service",
                " >> §fservice info [service] §7~ shows all the info about a particular service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice execute [service/--all] [command] §7~ um einen Befehl auf dem Server auszuführen",
                " >> §fservice execute [service/--all] [command] §7~ to execute a command on the server");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fservice whitelist [add/remove] [user] §7~ um Spieler zur Whitelist hinzuzufügen oder von ihr zu entfernen",
                " >> §fservice whitelist [add/remove] [user] §7~ to add players to the whitelist or to remove them from it");
    }

}
