package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerSwitchService;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerConnect;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerKick;
import eu.metacloudservice.networking.in.service.playerbased.apibased.PacketInAPIPlayerMessage;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;


@CommandInfo(command = "cloudplayers", DEdescription = "Verwalten aller Spieler der Cloud", ENdescription = "Manage all players of the cloud", aliases = {"players", "cp"})
public class PlayersCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args[0].equalsIgnoreCase("list")){
            PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                    "Alle Spieler auf dem Netzwerk:",
                    "All players on the network:");
            ArrayList<String> sortedPlayers = general.getCloudplayers();
            Collections.sort(sortedPlayers); // Alphabetisch sortieren
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < sortedPlayers.size(); i++) {
                String player = sortedPlayers.get(i);
                CloudPlayerRestCache cache = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" +player), CloudPlayerRestCache.class);
                builder.append(cache.getCloudplayername()).append(" (").append(cache.getCloudplayeruuid()).append(")");
                if (i < sortedPlayers.size() - 1) {
                    builder.append(", ");
                }
            }
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, builder.toString());
        }else {
            if (args.length == 1){
                PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(args[0])))){
                    CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "name: " + player.getCloudplayername());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "uuid: " + player.getCloudplayeruuid());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "proxy: " + player.getCloudplayerproxy());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "server: " + player.getCloudplayerservice());
                    int time = Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.getCloudplayerconnect())));
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "time: " +   time + " Second(s)");
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Es wurde kein Spieler mit diesem Namen gefunden",
                            "No player with this name was found");
                }
            }else {
                if (args[1].equalsIgnoreCase("kick")){
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(args[0]))){
                        if (args.length >= 3) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);

                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerKick(player.getCloudplayername(), message));
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Der Spieler ist vom Netzwerk gekickt worden",
                                    "The player has been kicked from the network");
                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Es wurde kein Spieler mit diesem Namen gefunden",
                                "No player with this name was found");
                    }
                }else if (args[1].equalsIgnoreCase("sendMessage")){
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(args[0])))){
                        if (args.length >= 3) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerMessage(player.getCloudplayername(),message));
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Der Spieler hat die Nachricht erhalten",
                                    "The player has received the message");
                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Es wurde kein Spieler mit diesem Namen gefunden",
                                "No player with this name was found");
                    }
                }else if (args[1].equalsIgnoreCase("connect")){
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(args[0])))){
                        if (args.length == 3) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);

                            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerConnect(player.getCloudplayername(), args[2]));
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Der Spieler wird auf den neuen server verschoben, sofern dieser auch existiert",
                                    "The player will be moved to the new server, if it exists.");
                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Es wurde kein Spieler mit diesem Namen gefunden",
                                "No player with this name was found");
                    }

                }else if (args[1].equalsIgnoreCase("op")) {
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(args[0])))){
                        if (args.length == 2) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                            CloudManager.serviceDriver.getService(player.getCloudplayerservice()).handelExecute("op " + player.getCloudplayername());
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Der Spieler ist nun in op",
                                    "The player is now op.");
                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Es wurde kein Spieler mit diesem Namen gefunden",
                                "No player with this name was found");
                    }
                }else if (args[1].equalsIgnoreCase("deop")) {

                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(args[0])))){
                        if (args.length == 2) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                            CloudManager.serviceDriver.getService(player.getCloudplayerservice()).handelExecute("deop " + player.getCloudplayername());
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                    "Der Spieler ist nun nicht mehr op",
                                    "The player is not longer op.");
                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                                "Es wurde kein Spieler mit diesem Namen gefunden",
                                "No player with this name was found");
                    }
                }else{
                    sendHelp();
                }
            }
        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> returns = new ArrayList<>();
        if (args.length == 0){
            returns.add("list");
            PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
            general.getCloudplayers().forEach(s -> returns.add(UUIDDriver.getUsername(s)));
        }else if (args.length == 1){
            if (!args[0].equalsIgnoreCase("list")){
                returns.add("kick");
                returns.add("sendMessage");
                returns.add("op");
                returns.add("deop");
                returns.add("connect");
            }
        }else {
            if (!args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("connect")){
                CloudManager.serviceDriver.getServices().forEach(taskedService -> {
                    returns.add(taskedService.getEntry().getServiceName());
                });
            }
        }
        return returns;
    }

    private void sendHelp() {
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers list §7~ zeigt dir alle Spieler an ",
                " >> §fcloudplayers list §7~ shows you all players");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers [name] §7~ zeigt alle Informationen von einen Spieler an",
                " >> §fcloudplayers [name] §7~ displays all information from one player");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers [name] op §7~ Gib einen Spieler auf seinen server op",
                " >> §fcloudplayers [name] op §7~ Give a player on his server op");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers [name] deop §7~ Ziehe einen Spieler den op status ab",
                " >> §fcloudplayers [name] deop §7~ Subtract op status from a player");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers [name] kick [reason] §7~ kicke ein Spieler vom Netzwerk",
                " >> §fcloudplayers [name] kick [reason] §7~ kick a player from the network");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers [name] sendMessage [message] §7~ sende einen Spieler eine Nachricht",
                " >> §fcloudplayers [name] sendMessage [message] §7~ send a player a message");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers [name] connect [service] §7~ sende einen Spieler auf einen anderen server",
                " >> §fcloudplayers [name] connect [service] §7~ send a player to another server");
    }
}
