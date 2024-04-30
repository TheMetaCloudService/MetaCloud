package eu.metacloudservice.serverside.bukkit.entry;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.ServicePing;
import eu.metacloudservice.config.SignLayout;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.serverside.bukkit.SignBootstrap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;

import java.io.IOException;

import static eu.metacloudservice.serverside.bukkit.SignBootstrap.signsAPI;

public class CloudSign {

    private final String group;
    private String service;
    private final Location location;

    public CloudSign(String group, Location signPosition) {
        this.group = group;
        this.location = signPosition;
        this.service = "";
    }

    public void drawSign() {
        Bukkit.getScheduler().runTaskAsynchronously(SignBootstrap.instance, () -> {
            if (signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(group)).findFirst().orElse(null) == null){
                return;
            }else if (!service.isEmpty()){
                if (CloudAPI.getInstance().getGroupPool().getGroup(group).isMaintenance()) service = "";
                else if (!CloudAPI.getInstance().getServicePool().serviceNotNull(service)) service = "";
                else if (signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(group)).findFirst().orElse(null).isHideFull() && CloudAPI.getInstance().getServicePool().getService(service).getPlayercount() >= CloudAPI.getInstance().getGroupPool().getGroup(group).getMaxPlayers() ) service = "";
                else if (CloudAPI.getInstance().getServicePool().getService(service).getState() != ServiceState.LOBBY) service = "";
            }

            SignLayout layout = determineLayout();
            String[] lines = generateSignLines(layout);
            Bukkit.getScheduler().runTask(SignBootstrap.instance, () -> {
                Block signBlock = location.getBlock();
                if (signBlock.getState() instanceof Sign sign) {
                    updateSign(sign, lines, layout);
                }
            });
        });
    }

    private String[] generateSignLines(SignLayout layout) {
        String[] lines = new String[4];
        lines[0] = layout.getLines()[0].replace("&", "§").replace("%service_group_name%", group);
        lines[1] = layout.getLines()[1].replace("&", "§").replace("%service_group_name%", group);
        lines[2] = layout.getLines()[2].replace("&", "§").replace("%service_group_name%", group);
        lines[3] = layout.getLines()[3].replace("&", "§").replace("%service_group_name%", group);

        if (!service.isEmpty()) {
        try {
            CloudService cloudService = CloudAPI.getInstance().getServicePool().getService(service);
            ServicePing ping = new ServicePing();
            try {
                ping.pingServer(cloudService.getAddress(), cloudService.getPort(), 300);
            } catch (IOException ignored) {
            }

            lines[0] = lines[0]
                    .replace("%service_name%", this.service)
                    .replace("%service_id%", CloudAPI.getInstance().getServicePool().getService(this.service).getID())
                    .replace("%service_state%", cloudService.getState().toString())
                    .replace("%service_node%", CloudAPI.getInstance().getServicePool().getService(this.service).getGroup().getStorage().getRunningNode())
                    .replace("%online_players%", String.valueOf(cloudService.getPlayercount()))
                    .replace("%service_motd%", ping.getMotd())
                    .replace("%max_players%", String.valueOf(cloudService.getGroup().getMaxPlayers()));
            lines[1] = lines[1]
                    .replace("%service_name%", this.service)
                    .replace("%service_id%", CloudAPI.getInstance().getServicePool().getService(this.service).getID())
                    .replace("%service_state%", cloudService.getState().toString())
                    .replace("%service_node%", CloudAPI.getInstance().getServicePool().getService(this.service).getGroup().getStorage().getRunningNode())
                    .replace("%online_players%", String.valueOf(cloudService.getPlayercount()))
                    .replace("%service_motd%", ping.getMotd())
                    .replace("%max_players%", String.valueOf(cloudService.getGroup().getMaxPlayers()));
            lines[2] = lines[2]
                    .replace("%service_name%", this.service)
                    .replace("%service_id%", CloudAPI.getInstance().getServicePool().getService(this.service).getID())
                    .replace("%service_state%", cloudService.getState().toString())
                    .replace("%service_node%", CloudAPI.getInstance().getServicePool().getService(this.service).getGroup().getStorage().getRunningNode())
                    .replace("%online_players%", String.valueOf(cloudService.getPlayercount()))
                    .replace("%service_motd%", ping.getMotd())
                    .replace("%max_players%", String.valueOf(cloudService.getGroup().getMaxPlayers()));
            lines[3] = lines[3]
                    .replace("%service_name%", this.service)
                    .replace("%service_id%", CloudAPI.getInstance().getServicePool().getService(this.service).getID())
                    .replace("%service_node%", CloudAPI.getInstance().getServicePool().getService(this.service).getGroup().getStorage().getRunningNode())
                    .replace("%service_state%", cloudService.getState().toString())
                    .replace("%online_players%", String.valueOf(cloudService.getPlayercount()))
                    .replace("%service_motd%", ping.getMotd())
                    .replace("%max_players%", String.valueOf(cloudService.getGroup().getMaxPlayers()));
        }catch (Exception e){

        }
        }

        return lines;
    }

    private void updateSign(Sign sign, String[] lines, SignLayout layout) {
        for (int i = 0; i < 4; i++) {
            sign.setLine(i, lines[i]);
        }

        if (sign.getBlock().getBlockData() instanceof WallSign wallSign) {
            Material material = Material.matchMaterial(layout.getItemID());
            if (material != null && material.isBlock()) {
                Block blockBehind = sign.getBlock().getRelative(wallSign.getFacing().getOppositeFace());
                blockBehind.setType(material);
            }
        }

        sign.setEditable(false);

        if (!layout.getGlowColor().isEmpty()) {
            DyeColor color = DyeColor.valueOf(layout.getGlowColor());
            sign.setColor(color);
        }
        sign.update();
    }



    private SignLayout determineLayout() {
        if (CloudAPI.getInstance().getGroupPool().getGroup(group).isMaintenance()) {
            return signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(group)).findFirst().orElse(null).getMaintenance().get(SignBootstrap.maintenance);
        } else if (service.isEmpty()  || service.equalsIgnoreCase(" ") || !CloudAPI.getInstance().getServicePool().serviceNotNull(service)) {
            return signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(group)).findFirst().orElse(null).getSearching().get(SignBootstrap.searching);
        } else {
            CloudService cloudService = CloudAPI.getInstance().getServicePool().getService(service);
            int playerCount = cloudService == null ? 0 : cloudService.getPlayercount();
            int maxPlayers = CloudAPI.getInstance().getGroupPool().getGroup(group) == null ? 0 : CloudAPI.getInstance().getGroupPool().getGroup(group).getMaxPlayers();
            if (playerCount >= maxPlayers) {
                return signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(group)).findFirst().orElse(null).getFull().get(SignBootstrap.full);
            } else if (playerCount == 0) {
                return signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(group)).findFirst().orElse(null).getEmpty().get(SignBootstrap.empty);
            } else {
                return signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(group)).findFirst().orElse(null).getOnline().get(SignBootstrap.online);
            }
        }
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getGroup() {
        return group;
    }

    public String getService() {
        return service;
    }

    public Location getLocation() {
        return location;
    }
}
