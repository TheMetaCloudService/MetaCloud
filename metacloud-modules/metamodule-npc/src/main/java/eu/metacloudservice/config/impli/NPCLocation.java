/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.config.impli;

public class NPCLocation {

    private String npcUUID;
    private Double locationPosX;
    private Double locationPosY;
    private Double locationPosZ;
    private String locationWorld;
    private String groupName;
    private String skinOwner;
    private boolean skinOwnerName;


    public NPCLocation() {
    }

    public NPCLocation(String npcUUID, Double locationPosX, Double locationPosY, Double locationPosZ, String locationWorld, String groupName, String leftClickAction, String rightClickAction, String skinOwner, boolean skinOwnerName) {
        this.npcUUID = npcUUID;
        this.locationPosX = locationPosX;
        this.locationPosY = locationPosY;
        this.locationPosZ = locationPosZ;
        this.locationWorld = locationWorld;
        this.groupName = groupName;
        this.skinOwner = skinOwner;
        this.skinOwnerName = skinOwnerName;
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


    public String getSkinOwner() {
        return skinOwner;
    }

    public boolean isSkinOwnerName() {
        return skinOwnerName;
    }
}
