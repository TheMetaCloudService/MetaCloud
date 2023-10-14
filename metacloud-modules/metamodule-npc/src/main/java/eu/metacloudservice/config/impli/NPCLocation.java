/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config;

public class NPCLocation {

    private String npcUUID;
    private Double locationPosX;
    private Double locationPosY;
    private Double locationPosZ;
    private String locationWorld;
    private String groupName;
    private String leftClickAction;
    private String rightClickAction;
    private String skinOwner;
    private boolean skinOwnerName;
    private boolean shouldLookAtPlayer;
    private boolean shouldImitatePlayer;

    public NPCLocation(String npcUUID, Double locationPosX, Double locationPosY, Double locationPosZ, String locationWorld, String groupName) {
        this.npcUUID = npcUUID;
        this.locationPosX = locationPosX;
        this.locationPosY = locationPosY;
        this.locationPosZ = locationPosZ;
        this.locationWorld = locationWorld;
        this.groupName = groupName;
    }

    public String getNpcUUID() {
        return npcUUID;
    }

    public Double getLocationPosX() {
        return locationPosX;
    }

    public Double getLocationPosY() {
        return locationPosY;
    }

    public Double getLocationPosZ() {
        return locationPosZ;
    }

    public String getLocationWorld() {
        return locationWorld;
    }

    public String getGroupName() {
        return groupName;
    }
}
