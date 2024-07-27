package eu.metacloudservice.storage;

import lombok.NonNull;

import java.util.UUID;

public class UUIDStorage {

    private final String username;
    private final UUID uniqueID;
    public UUIDStorage(@NonNull final String username, @NonNull final UUID uniqueID) {
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
