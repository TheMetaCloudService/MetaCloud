package eu.metacloudservice.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.commands.translate.Translator;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.PacketInPlayerSwitchService;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.velocity.VelocityBootstrap;
import lombok.SneakyThrows;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.UUID;

public class CloudConnectListener {


    private final ArrayList<UUID> connected = new ArrayList<>();
    private final ProxyServer server;
    public ServerInfo target;

    public CloudConnectListener(ProxyServer server) {
        this.server = server;
    }


    @Subscribe(order = PostOrder.FIRST)
    public void handel(ServerPreConnectEvent event){
        if (event.getOriginalServer().getServerInfo().getName().equalsIgnoreCase("lobby")){

           CloudService service = VelocityBootstrap.getLobby(event.getPlayer());

           if (service != null) {
               target = server.getServer(service.getName()).get().getServerInfo();
               if (target != null){
                   event.setResult(ServerPreConnectEvent.ServerResult.allowed(server.getServer(target.getName()).get()));
               }else event.setResult(ServerPreConnectEvent.ServerResult.denied());
           }else event.setResult(ServerPreConnectEvent.ServerResult.denied());
        }else if (event.getOriginalServer() == null){
            target = server.getServer(VelocityBootstrap.getLobby(event.getPlayer()).getName()).get().getServerInfo();
            if (target != null){
                event.setResult(ServerPreConnectEvent.ServerResult.allowed(server.getServer(target.getName()).get()));
            }else event.setResult(ServerPreConnectEvent.ServerResult.denied());
        }
    }



    @Subscribe
    public void handle(PostLoginEvent event){
        LiveService service = (LiveService)(new ConfigDriver("./CLOUDSERVICE.json")).read(LiveService.class);
        Group group = CloudAPI.getInstance().getGroupPool().getGroup(service.getGroup());


        if (CloudAPI.getInstance().getPlayerPool().getPlayers().stream().anyMatch(cloudPlayer -> cloudPlayer.getUsername().equalsIgnoreCase(event.getPlayer().getUsername()))){
            event.getPlayer().disconnect(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickAlreadyOnNetwork"))));
        }else {
            this.connected.add(event.getPlayer().getUniqueId());
            CloudAPI.getInstance().sendPacketSynchronized(new PacketInPlayerConnect(event.getPlayer().getUsername(), service.getService()));

            if (group.isMaintenance()) {
                if (!server.getPlayer(event.getPlayer().getUniqueId()).get().hasPermission("metacloud.connection.maintenance")
                        && !CloudAPI.getInstance().getWhitelist().contains(server.getPlayer(event.getPlayer().getUniqueId()).get().getUsername())){
                    event.getPlayer().disconnect(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNetworkIsMaintenance"))));
                }else {
                    if (CloudAPI.getInstance().getPlayerPool().getPlayers().size() >= group.getMaxPlayers()
                            && !server.getPlayer(event.getPlayer().getUniqueId()).get().hasPermission("metacloud.connection.full")
                            && !CloudAPI.getInstance().getWhitelist().contains(server.getPlayer(event.getPlayer().getUniqueId()).get().getUsername())){
                        event.getPlayer().disconnect(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNetworkIsFull"))));


                    }else if (server.getPlayer(event.getPlayer().getUniqueId()).isPresent()
                            && VelocityBootstrap.getLobby( server.getPlayer(event.getPlayer().getUniqueId()).get()) == null){

                        event.getPlayer().disconnect(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNoFallback"))));

                    }
                }
            }else {
                if (CloudAPI.getInstance().getPlayerPool().getPlayers().size() >= group.getMaxPlayers()
                        && !server.getPlayer(event.getPlayer().getUniqueId()).get().hasPermission("metacloud.connection.full")
                        && !CloudAPI.getInstance().getWhitelist().contains(server.getPlayer(event.getPlayer().getUniqueId()).get().getUsername())){
                    event.getPlayer().disconnect(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNetworkIsFull"))));


                }else if (server.getPlayer(event.getPlayer().getUniqueId()).isPresent()
                        && VelocityBootstrap.getLobby( server.getPlayer(event.getPlayer().getUniqueId()).get()) == null){

                    event.getPlayer().disconnect(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNoFallback"))));

                }
            }
        }
    }

    @Subscribe
    public void handle(DisconnectEvent event){
        if (this.connected.contains(event.getPlayer().getUniqueId())) {
            CloudAPI.getInstance().sendPacketSynchronized(new PacketInPlayerDisconnect(event.getPlayer().getUsername()));
        }
    }

    @Subscribe
    public void handle(ServerConnectedEvent event){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInPlayerSwitchService(event.getPlayer().getUsername(), event.getServer().getServerInfo().getName()));
    }

    @SneakyThrows
    @Subscribe
    public void handle(KickedFromServerEvent event){
        if (event.getPlayer().isActive()) {

            if (CloudAPI.getInstance().getGroupPool().getGroup(CloudAPI.getInstance().getService().getGroup()).isMaintenance() &&!server.getPlayer(event.getPlayer().getUniqueId()).get().hasPermission("metacloud.connection.maintenance")
                    && !CloudAPI.getInstance().getWhitelist().contains(server.getPlayer(event.getPlayer().getUniqueId()).get().getUsername())){
                event.getPlayer().disconnect(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNetworkIsMaintenance"))));
            }else {
                CloudService target = VelocityBootstrap.getLobby(event.getPlayer(), event.getServer().getServerInfo().getName());
                if (target == null) {
                    event.setResult(KickedFromServerEvent.DisconnectPlayer.create(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNoFallback")))));
                } else {
                    if (event.getServerKickReason().isPresent()){
                        String  reason = PlainTextComponentSerializer.plainText().serialize(event.getServerKickReason().get());
                        if (reason.startsWith("Outdated server! I'm still on") || reason.startsWith("Outdated client! Please use ")){
                            if (event.getServer().getServerInfo().getName().equalsIgnoreCase("LOBBY")){
                                event.setResult(KickedFromServerEvent.DisconnectPlayer.create(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("notTheRightVersion")
                                        .replace("%current_service_version%", reason
                                                .replace("Outdated server! I'm still on ", "")
                                                .replace("Outdated client! Please use ", ""))))));
                            }
                        }else {
                            if (server.getServer(target.getName()).isPresent()){
                                event.setResult(KickedFromServerEvent.RedirectPlayer.create(server.getServer(target.getName()).get()));
                            }else {
                                event.setResult(KickedFromServerEvent.DisconnectPlayer.create(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNoFallback")))));
                            }
                        }
                    }else {
                        if (server.getServer(target.getName()).isPresent()){
                            event.setResult(KickedFromServerEvent.RedirectPlayer.create(server.getServer(target.getName()).get()));
                        }else {
                            event.setResult(KickedFromServerEvent.DisconnectPlayer.create(VelocityBootstrap.message.deserialize(new Translator().translate(CloudAPI.getInstance().getMessages().getMessages().get("kickNoFallback")))));

                        }
                    }
                }
            }


        }
    }





}
