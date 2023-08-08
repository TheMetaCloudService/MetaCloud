/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.signs;


import com.google.common.base.Enums;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.ServicePing;
import eu.metacloudservice.config.SignLayout;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.serverside.bukkit.BukkitBootstrap;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ServerPing;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.material.MaterialData;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CloudSign {

    @Getter
    private final UUID uuid;
    @Getter
    private final Location signPosition;

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
    }

    public void setLayout(SignLayout layout) {
        Bukkit.getScheduler().runTask(BukkitBootstrap.getInstance(), () -> {
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



                sign.setEditable(false);
                sign.update();
            }
        });
    }


    private BlockFace getFace(String in) {
        if (in == null) {
            return null;
        }

        Matcher matcher = Pattern.compile("(.*)\\[facing=(.*),waterlogged=(.*)]").matcher(in);
        return matcher.matches() ? Enums
                .getIfPresent(BlockFace.class, matcher.group(2).toUpperCase()).orNull() : null;
    }

    private BlockFace getOpposite(BlockFace blockFace) {
        if (blockFace == null) {
            return BlockFace.SELF;
        }

        switch (blockFace) {
            case EAST:
                return BlockFace.WEST;
            case NORTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.EAST;
            case SOUTH:
                return BlockFace.NORTH;
            default:
                return BlockFace.SELF;
        }
    }

}