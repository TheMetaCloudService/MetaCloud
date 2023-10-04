/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.entry;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.ServicePing;
import eu.metacloudservice.config.SignLayout;
import eu.metacloudservice.pool.service.entrys.CloudService;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;

import java.io.IOException;
import java.util.UUID;

public class CloudSign {

    private final UUID signUUID;
    private final String group;
    private String service;
    private final Location signPosition;


    public CloudSign(UUID signUUID, String group, Location signPosition) {
        this.signUUID = signUUID;
        this.group = group;
        this.signPosition = signPosition;
    }

    public void drawSign(SignLayout layout){
        if (this.signPosition.getBlock().getState() instanceof Sign sign) {
            String[] lines = layout.getLines();

            for (int i = 0; i < 4; i++) {
                String lineText = (lines[i]).replace("&", "§").replace("%service_group_name%", group);
                if (!service.isEmpty()) {
                    CloudService service = CloudAPI.getInstance().getServicePool().getService(this.service);
                    ServicePing ping = new ServicePing();
                    try {
                        ping.pingServer(service.getAddress(), service.getPort(), 300);
                    } catch (IOException ignored) {
                    }

                    lineText = lineText
                            .replace("%service_name%", this.service)
                            .replace("%service_state%", service.getState().toString())
                            .replace("%online_players%", String.valueOf(service.getPlayercount()))
                            .replace("%service_motd%", ping.getMotd())
                            .replace("%max_players%", String.valueOf(service.getGroup().getMaxPlayers()));

                    sign.setLine(i, lineText);
                }
            }

            if (this.signPosition.getBlock().getBlockData() instanceof WallSign) {
                Directional directional = (Directional) this.signPosition.getBlock().getBlockData();
                Block blockBehind = this.signPosition.getBlock().getRelative(directional.getFacing().getOppositeFace());
                if (!layout.getItemID().equalsIgnoreCase("")) {
                    Material material = Material.matchMaterial(layout.getItemID());
                    if (material != null && material.isBlock()) {
                        blockBehind.setType(material);
                    }
                }
            }

            sign.setEditable(false);
            if (layout.isGlowText() && !layout.getColor().isEmpty()) {
                DyeColor color = DyeColor.valueOf(layout.getColor());
                sign.setColor(color);
            }

            sign.setGlowingText(layout.isGlowText());
            sign.update();
        }
    }

    public void setService(String service) {
        this.service = service;
    }

    public UUID getSignUUID() {
        return signUUID;
    }

    public String getGroup() {
        return group;
    }

    public String getService() {
        return service;
    }

    public Location getSignPosition() {
        return signPosition;
    }
}
