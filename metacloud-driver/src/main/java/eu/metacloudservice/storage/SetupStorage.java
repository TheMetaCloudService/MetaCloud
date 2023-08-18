package eu.metacloudservice.storage;

import java.util.HashMap;

public final class SetupStorage {


    public Integer step;
    public HashMap<String, Object> storage;

    public SetupStorage() {
        storage = new HashMap<>();
        step = 0;
    }
}
