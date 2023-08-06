/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.signs;


import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.config.SignLayout;
import eu.metacloudservice.pool.service.entrys.CloudService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;

import java.net.Socket;
import java.util.UUID;

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
    }

    public void setLayout(SignLayout layout){
        Block block = this.signPosition.getBlock();
        if (block instanceof Sign sign){

            for (int i = 0; i != 3 ; i++) {
                if (server.isEmpty()){
                    sign.setLine(i, layout.getLines()[i]
                            .replace("&", "§")
                            .replace("%service_group_name%", group));
                }else {
                    CloudService service = CloudAPI.getInstance().getServicePool().getService(server);

                    sign.setLine(i, layout.getLines()[i]
                            .replace("&", "§")
                            .replace("%service_group_name%", group)
                            .replace("%service_name%", server)
                            .replace("%service_state%", service.getState().toString())
                            .replace("%online_players%", String.valueOf(service.getPlayercount()))
                            .replace("%max_players%", String.valueOf(service.getGroup().getMaxPlayers()))
                            .replace("%service_id%", String.valueOf(service.getID()))
                            .replace("%service_motd%", service.getMOTD()));
                }
            }
            sign.setEditable(false);
            sign.setColor(DyeColor.BLUE);
            sign.setGlowingText(true);
            sign.update(true);
        }

    }



}