package eu.metacloudservice.storage;

import java.util.UUID;

public class UUIDStorage {

    private final String username;
    private final UUID uniqueID;
    public UUIDStorage(String username, UUID uniqueID) {
        this.username = username;
        this.uniqueID = uniqueID;
    }
    public String getUsername() {
        return username;
    }
    public UUID getUniqueID() {
        return uniqueID;
    }
}
