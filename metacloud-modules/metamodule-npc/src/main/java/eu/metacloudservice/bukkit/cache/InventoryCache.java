/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit.cache;

import eu.metacloudservice.service.entrys.CloudService;

import java.util.HashMap;
import java.util.UUID;

public class InventoryCache {

    public UUID uuid;

    public HashMap<Integer, CloudService> serviceBySlot;

    public InventoryCache(UUID uuid, HashMap<Integer, CloudService> serviceBySlot) {
        this.uuid = uuid;
        this.serviceBySlot = serviceBySlot;
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<Integer, CloudService> getServiceBySlot() {
        return serviceBySlot;
    }
}
