package eu.metacloudservice.cloudplayer.codec.teleport;

public class TeleportBuilder {
    private String player;
    private int posX;
    private int posY;
    private int posZ;

    public TeleportBuilder() {
        // Set default values if needed
        this.player = "";
        this.posX = 0;
        this.posY = 0;
        this.posZ = 0;
    }

    public TeleportBuilder withPlayer(String player) {
        this.player = player;
        return this;
    }

    public TeleportBuilder withPosX(int posX) {
        this.posX = posX;
        return this;
    }

    public TeleportBuilder withPosY(int posY) {
        this.posY = posY;
        return this;
    }

    public TeleportBuilder withPosZ(int posZ) {
        this.posZ = posZ;
        return this;
    }

    public Teleport build() {
        return new Teleport(player, posX, posY, posZ);
    }
}
