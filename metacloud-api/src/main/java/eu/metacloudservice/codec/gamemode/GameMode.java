package eu.metacloudservice.codec;

public enum GameMode {
    /**
     * Creative mode: fly, build instantly, become invulnerable, and create free items.
     */
    CREATIVE,

    /**
     * Survival mode: normal gameplay type, with no special features.
     */
    SURVIVAL,

    /**
     * Adventure mode: cannot break blocks without the correct tools.
     */
    ADVENTURE,

    /**
     * Spectator mode: cannot interact with the world, invisible to normal players, ability to no-clip through the world.
     */
    SPECTATOR;
}
