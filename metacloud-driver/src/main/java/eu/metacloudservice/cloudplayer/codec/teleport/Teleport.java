package eu.metacloudservice.cloudplayer.codec.teleport;

import lombok.Getter;

public class Teleport {

    @Getter
    private final String player;

    @Getter
    private final int posX;

    @Getter
    private final int posY;

    @Getter
    private final int posZ;

    public Teleport(final String player, final int posX, final int posY, final int posZ) {
        this.player = player;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }
}
