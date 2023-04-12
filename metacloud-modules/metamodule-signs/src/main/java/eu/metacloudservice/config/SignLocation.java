package eu.metacloudservice.config;

public class SignLocation {

    private String signUUID;
    private Double locationPosX;
    private Double locationPosY;
    private Double locationPosZ;
    private String locationWorld;
    private String groupName;

    public SignLocation(String signUUID, Double locationPosX, Double locationPosY, Double locationPosZ, String locationWorld, String groupName) {
        this.signUUID = signUUID;
        this.locationPosX = locationPosX;
        this.locationPosY = locationPosY;
        this.locationPosZ = locationPosZ;
        this.locationWorld = locationWorld;
        this.groupName = groupName;
    }

    public SignLocation() {}

    public String getSignUUID() {
        return signUUID;
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
