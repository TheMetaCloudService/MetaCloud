package eu.themetacloudservice.bungeecord.command;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bungeecord.CloudPlugin;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.message.Messages;
import eu.themetacloudservice.network.cloudcommand.*;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.webserver.dummys.WhitelistConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CloudCommand extends Command {

    public CloudCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (!player.hasPermission("metacloud.command.use")){
                Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);

                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "§cYou do not have any rights to use the command");
            }else {
                if (args.length == 0){
                    sendHelp(player);
                }else if (args.length==1){
                    if (args[0].equalsIgnoreCase("reload")){

                        PackageCloudCommandRELOAD reload = new PackageCloudCommandRELOAD();
                        NettyDriver.getInstance().nettyClient.sendPacket(reload);
                        Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");

                    }else if (args[0].equalsIgnoreCase("exit")){
                        PackageCloudCommandEXIT exit =  new PackageCloudCommandEXIT();
                        NettyDriver.getInstance().nettyClient.sendPacket(exit);
                        Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");

                    }else {
                        sendHelp(player);
                    }
                }else if (args.length == 2){
                    if (args[0].equalsIgnoreCase("sync")){

                        if (CloudPlugin.getInstance().getRestDriver().get("/" + args[1]) == null){
                            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                            player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the service was not found");

                            return;
                        }

                        Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");
                        PackageCloudCommandSYNC sync = new PackageCloudCommandSYNC(args[1]);
                        NettyDriver.getInstance().nettyClient.sendPacket(sync);
                    }else if (args[0].equalsIgnoreCase("togglemaintenance")){
                        if (CloudPlugin.getInstance().getRestDriver().get("/" + args[1]) == null){
                            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                            player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the group was not found");

                            return;
                        }
                        Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");
                        PackageCloudCommandMAINTENANCE maintenance = new PackageCloudCommandMAINTENANCE(args[1]);
                        NettyDriver.getInstance().nettyClient.sendPacket(maintenance);
                    }else {
                        sendHelp(player);
                    }
                }else if (args.length == 3){
                    if (args[0].equalsIgnoreCase("setmaxplayers")){
                        String group = args[1];
                        int amount = Integer.parseInt(args[2]);
                        if (CloudPlugin.getInstance().getRestDriver().get("/" + group) == null){
                            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                            player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the group was not found");

                            return;
                        }
                        Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");
                        PackageCloudCommandPLAYERS players = new PackageCloudCommandPLAYERS(group, amount);
                        NettyDriver.getInstance().nettyClient.sendPacket(players);
                    }else if (args[0].equalsIgnoreCase("whitelist")){

                        String name = args[2];
                        WhitelistConfig whitelistConfig = (WhitelistConfig)(new ConfigDriver()).convert(CloudPlugin.getInstance().getRestDriver().get("/whitelist"), WhitelistConfig.class);

                        if (args[1].equalsIgnoreCase("add")){
                            if (whitelistConfig.getWhitelist().contains(name)){
                                Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the player is already whitelisted");
                                return;
                            }
                            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                            player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");
                            PackageCloudCommandWITELIST witelist = new PackageCloudCommandWITELIST(true,name);
                            NettyDriver.getInstance().nettyClient.sendPacket(witelist);
                        }else  if (args[1].equalsIgnoreCase("remove")){
                            if (!whitelistConfig.getWhitelist().contains(name)){
                                Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the player is not whitelisted");
                                return;
                            }
                            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                            player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");
                            PackageCloudCommandWITELIST witelist = new PackageCloudCommandWITELIST(false,name);
                            NettyDriver.getInstance().nettyClient.sendPacket(witelist);
                        }else {
                            sendHelp(player);
                        }


                    }else if (args[0].equalsIgnoreCase("service")){
                        if (args[1].equalsIgnoreCase("stop")){
                            String service = args[2];


                            if (CloudPlugin.getInstance().getRestDriver().get("/" + service) == null){
                                Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the service was not found");

                                return;
                            }
                            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                            player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");
                            PackageCloudCommandSTOP stop = new PackageCloudCommandSTOP(service);
                            NettyDriver.getInstance().nettyClient.sendPacket(stop);
                        }else  if (args[1].equalsIgnoreCase("stopgroup")){
                            String group = args[2];

                            if (CloudPlugin.getInstance().getRestDriver().get("/" + group) == null){
                                Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the group was not found");

                                return;
                            }
                            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
                            player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") +  "the action was sent to the Cloud");
                            PackageCloudCommandSTOPGROUP stopgroup = new PackageCloudCommandSTOPGROUP(group);
                            NettyDriver.getInstance().nettyClient.sendPacket(stopgroup);
                        }else {
                            sendHelp(player);
                        }
                    }else {
                        sendHelp(player);
                    }
                }
            }
        }

    }

    public void sendHelp(ProxiedPlayer player){
        Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud setmaxplayers <group> <amount>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud togglemaintenance <group>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud whitelist <add/remove> <player>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud service run <group> <amount>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud service stop <service>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud service stopgroup <group>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud sync <service>");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud exit");
        player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix()).replace("&", "§") + "/cloud reload");
    }
}
