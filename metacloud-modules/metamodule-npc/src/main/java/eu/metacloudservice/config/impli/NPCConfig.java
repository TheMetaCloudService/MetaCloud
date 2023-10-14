/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config.impli;

import eu.metacloudservice.config.enums.ClickAction;

public class NPCConfig {

    public String targetGroup;
    public String inventoryName;
    public boolean collidable;
    public boolean tracingPlayers;
    public String glowColor;
    public ClickAction leftClickAction;
    public ClickAction rightClickAction;
    public NPCNamesLayout names;
    public ItemLayout onlineItem;
    public ItemLayout emptyItem;
    public ItemLayout fullItem;


    public NPCConfig() {
    }

    public NPCConfig(String targetGroup, boolean hideFull, ClickAction leftClickAction, ClickAction rightClickAction, NPCNamesLayout names, ItemLayout onlineItem, ItemLayout emptyItem, ItemLayout fullItem, String inventoryName) {
        this.targetGroup = targetGroup;
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;
        this.names = names;
        this.onlineItem = onlineItem;
        this.emptyItem = emptyItem;
        this.fullItem = fullItem;
        this.inventoryName = inventoryName;
        this.collidable = true;
        this.tracingPlayers = true;
        this.glowColor = "";

    }

    public boolean isCollidable() {
        return collidable;
    }

    public boolean isTracingPlayers() {
        return tracingPlayers;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public ClickAction getLeftClickAction() {
        return leftClickAction;
    }

    public ClickAction getRightClickAction() {
        return rightClickAction;
    }

    public NPCNamesLayout getNames() {
        return names;
    }

    public ItemLayout getOnlineItem() {
        return onlineItem;
    }

    public ItemLayout getEmptyItem() {
        return emptyItem;
    }

    public ItemLayout getFullItem() {
        return fullItem;
    }
}
