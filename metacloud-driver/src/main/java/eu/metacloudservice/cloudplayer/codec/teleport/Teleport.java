package eu.metacloudservice.cloudplayer.codec.teleport;

import lombok.Getter;

public class Teleport {

    @Getter
    private String player;

    @Getter
    private int posX;

    @Getter
    private int posY;

    @Getter
    private int posZ;

    public Teleport(String player, int posX, int posY, int posZ) {
        this.player = player;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }
}
