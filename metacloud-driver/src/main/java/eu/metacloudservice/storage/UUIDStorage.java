package eu.metacloudservice.storage;

public class UUIDStorage {

    private final String username;
    private final String uniqueID;
    public UUIDStorage(String username, String uniqueID) {
        this.username = username;
        this.uniqueID = uniqueID;
    }
    public String getUsername() {
        return username;
    }
    public String getUniqueID() {
        return uniqueID;
    }
}
