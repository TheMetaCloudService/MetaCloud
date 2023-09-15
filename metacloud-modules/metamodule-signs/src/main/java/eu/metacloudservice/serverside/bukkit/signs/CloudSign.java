/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.signs;


import com.google.common.base.Enums;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.ServicePing;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.SignLayout;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.serverside.bukkit.BukkitBootstrap;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CloudSign {

    @Getter
    private final UUID uuid;
    @Getter
    private final Location signPosition;
    private final TimerBase base;

    @Getter
    private String group;
    @Setter
    @Getter
    private String server;

    public CloudSign(UUID uuid, Location signPosition, String group) {
        this.uuid = uuid;
        this.signPosition = signPosition;
        this.group = group;
        server = "";

        this.base = new TimerBase();
        this.base.schedule(new TimerTask() {
            @Override
            public void run() {

                if (CloudAPI.getInstance().getGroups().stream().noneMatch(group1 -> group1.getGroup().equalsIgnoreCase(group))){
                    BukkitBootstrap.getInstance().signsAPI.removeSign(getUuid().toString());
                    setLayout(new SignLayout(new String[]{"", "", "", ""}, "0", false, "0"));
                }else {
                    if (Objects.requireNonNull(CloudAPI.getInstance().getGroups().stream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null)).isMaintenance()){
                        server = "";
                        setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().maintenanceLayout);
                    }else if (server.isEmpty()){
                        String result = BukkitBootstrap.getInstance().getSignDriver().getFreeServer(group);

                        if (result != null && !result.isEmpty()){
                            CloudService service = CloudAPI.getInstance().getServicePool().getService(result);
                            if (service == null) {
                                setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().searchingLayout);
                                return;
                            }else {
                                Configuration configuration = BukkitBootstrap.getInstance().signsAPI.getConfig();

                                if (service.getPlayercount() >= Objects.requireNonNull(CloudAPI.getInstance().getGroups().stream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null)).getMaxPlayers() && configuration.isHideFull()){
                                    setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().searchingLayout);
                                    return;
                                }
                                server = result;
                                setLayout(service.getPlayercount() == 0 ? BukkitBootstrap.getInstance().getSignDriver().getWorker().emptyLayout : BukkitBootstrap.getInstance().getSignDriver().getWorker().onlineLayout);
                            }
                        }else {
                            setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().searchingLayout);
                        }
                    }else {
                        CloudService service = CloudAPI.getInstance().getServicePool().getService(server);
                        if (service == null){
                            server = "";
                            setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().searchingLayout);
                        }else if (service.getState() != ServiceState.LOBBY){
                            server = "";
                            setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().searchingLayout);
                        }else if (service.getPlayercount() >= Objects.requireNonNull(CloudAPI.getInstance().getGroups().stream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null)).getMaxPlayers()){
                            Configuration configuration = BukkitBootstrap.getInstance().signsAPI.getConfig();
                            if (configuration.isHideFull()){
                                server = "";
                                setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().searchingLayout);
                            }else {
                                setLayout(BukkitBootstrap.getInstance().getSignDriver().getWorker().fullLayout);
                            }
                        }else {
                            setLayout(service.getPlayercount() == 0 ? BukkitBootstrap.getInstance().getSignDriver().getWorker().emptyLayout : BukkitBootstrap.getInstance().getSignDriver().getWorker().onlineLayout);
                        }

                    }
                }
            }
        }, 1, 1, TimeUtil.SECONDS);

    }

    public void setLayout(SignLayout layout) {
        Block block = this.signPosition.getBlock();

        if (block.getState() instanceof Sign sign) {
            for (int i = 0; i < 4; i++) {
                String lineText = ( layout.getLines()[i]).replace("&", "§").replace("%service_group_name%", group);

                if (!server.isEmpty()) {
                    CloudService service = CloudAPI.getInstance().getServicePool().getService(server);
                    ServicePing ping = new ServicePing();
                    try {
                        ping.pingServer(service.getAddress(), service.getPort(), 300);
                    } catch (IOException ignored) {}

                    lineText = lineText
                            .replace("%service_name%", server)
                            .replace("%service_state%", service.getState().toString())
                            .replace("%online_players%", String.valueOf(service.getPlayercount()))
                            .replace("%service_motd%", ping.getMotd())
                            .replace("%max_players%", String.valueOf(service.getGroup().getMaxPlayers()));
                }

                sign.setLine(i, lineText);

            }

            if (block.getBlockData() instanceof WallSign){
                Directional directional = (Directional) block.getBlockData();
                Block blockBehind = block.getRelative(directional.getFacing().getOppositeFace());
                if (!layout.getItemID().equalsIgnoreCase("")){

                    Material material = Material.matchMaterial(layout.getItemID());
                    if (material != null && material.isBlock()){
                        blockBehind.setType(material);
                    }
                }
            }

            sign.setEditable(false);
            if (layout.isGlowText() && !layout.getColor().isEmpty()){
                DyeColor color = DyeColor.valueOf(layout.getColor());
                sign.setColor(color);
            }

            sign.setGlowingText(layout.isGlowText());
            sign.update();
        }

        if (BukkitBootstrap.getInstance().signsAPI.getConfig().isUseKnockBack()){
            for (var entity : Objects.requireNonNull(signPosition.getWorld()).getNearbyEntities(signPosition, BukkitBootstrap.getInstance().signsAPI.getConfig().getKnockbackDistance(), BukkitBootstrap.getInstance().signsAPI.getConfig().getKnockbackDistance(), BukkitBootstrap.getInstance().signsAPI.getConfig().getKnockbackDistance())){
                if (entity instanceof Player){
                    if (!entity.hasPermission("metacloud.sign.bypass")){
                        entity.setVelocity(((Player) entity).getEyeLocation().toVector()
                                .subtract(signPosition.getBlock().getLocation().toVector())
                                .normalize()
                                .multiply(BukkitBootstrap.getInstance().signsAPI.getConfig().getKnockbackStrength()).setY(0.3));
                    }
                }
            }
        }
    }


    public void delete(){
        this.base.cancel();
    }


}