package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerConnect;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerKick;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased.PacketOutAPIPlayerMessage;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;

import java.util.*;
import java.util.concurrent.TimeUnit;


@CommandInfo(command = "cloudplayers", description = "command-player-description", aliases = {"players", "cp"})
public class PlayersCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args[0].equalsIgnoreCase("list")){
            PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
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
                if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(Objects.requireNonNull(UUIDDriver.getUUID(args[0])).toString()))){
                    CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "name: " + player.getCloudplayername());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "uuid: " + player.getCloudplayeruuid());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "proxy: " + player.getCloudplayerproxy());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "server: " + player.getCloudplayerservice());
                    int time = Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.getCloudplayerconnect())));
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "time: " +   time + " Second(s)");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-not-found"));

                }
            }else {
                if (args[1].equalsIgnoreCase("kick")){
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(args[0]))){
                        if (args.length >= 3) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);

                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerKick(player.getCloudplayername(), message));
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-kick"));

                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-not-found"));

                    }
                }else if (args[1].equalsIgnoreCase("sendMessage")){
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(Objects.requireNonNull(UUIDDriver.getUUID(args[0])).toString()))){
                        if (args.length >= 3) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerMessage(player.getCloudplayername(),message));
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-message"));

                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-not-found"));

                    }
                }else if (args[1].equalsIgnoreCase("connect")){
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(Objects.requireNonNull(UUIDDriver.getUUID(args[0])).toString()))){
                        if (args.length == 3) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);

                            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutAPIPlayerConnect(player.getCloudplayername(), args[2]));
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-send"));

                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-not-found"));

                    }

                }else if (args[1].equalsIgnoreCase("op")) {
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(Objects.requireNonNull(UUIDDriver.getUUID(args[0])).toString()))){
                        if (args.length == 2) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                            CloudManager.serviceDriver.getService(player.getCloudplayerservice()).handelExecute("op " + player.getCloudplayername());
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-op"));

                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-not-found"));

                    }
                }else if (args[1].equalsIgnoreCase("deop")) {

                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    if (general.getCloudplayers().stream().anyMatch(s -> s.equalsIgnoreCase(Objects.requireNonNull(UUIDDriver.getUUID(args[0])).toString()))){
                        if (args.length == 2) {
                            CloudPlayerRestCache player = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(args[0])), CloudPlayerRestCache.class);
                            CloudManager.serviceDriver.getService(player.getCloudplayerservice()).handelExecute("deop " + player.getCloudplayername());
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-de-op"));

                        }else {
                            sendHelp();
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-not-found"));
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
            general.getCloudplayers().forEach(s -> returns.add(UUIDDriver.getUsername(UUID.fromString(s))));
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
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-help-1"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-help-2"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-help-3"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-help-4"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-help-5"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-help-6"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-player-help-7")
        );
    }
}
