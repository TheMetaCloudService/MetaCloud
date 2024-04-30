/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.drivers;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.serverside.bukkit.entry.CloudSign;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SignDriver {

    private final Map<UUID, CloudSign> signs;

    public SignDriver() {
        this.signs = new ConcurrentHashMap<>();
    }

    // Methode zum Hinzufügen eines Signs
    public void addSign(UUID signUUID, String group, Location location) {
        CloudSign sign = new CloudSign(group, location);
        signs.put(signUUID, sign);
    }

    // Methode zum Entfernen eines Signs anhand seiner UUID
    public void removeSign(UUID signUUID) {
        signs.remove(signUUID);
    }

    // Methode zum Abrufen aller Signs (als unveränderliche Liste)
    public List<CloudSign> getAllSignsRed() {
        return Collections.unmodifiableList(new ArrayList<>(signs.values()));
    }

    public List<CloudSign> getAllSigns() {
        return List.copyOf(signs.values());
    }

    // Methode zum Abrufen eines Signs anhand seiner UUID
    public CloudSign getSignByUUID(UUID signUUID) {
        return signs.get(signUUID);
    }

    public UUID getUUIDByLocation(Location location) {

        for (UUID uuid : signs.keySet()) {
            if (signs.get(uuid).getLocation().equals(location)) {
                return uuid;
            }
        }

        return null;
    }

    public List<String> getFreeServers(String group) {
        return CloudAPI.getInstance().getServicePool().getServicesByGroup(group).stream()
                .filter(Objects::nonNull)
                .filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                .filter(cloudService -> getSignByService(cloudService.getName()) == null)
                .sorted(Comparator.comparing(CloudService::getID))
                .map(CloudService::getName).toList();
    }

    public List<UUID> getFreeSigns() {
        List<UUID> uuids = new ArrayList<>();
        for (UUID uuid : signs.keySet()) {
            if (signs.get(uuid).getService().equalsIgnoreCase("")) {
                uuids.add(uuid);
            }
        }
        return uuids;
    }


    public CloudSign getSignByService(String service) {
        for (CloudSign sign : signs.values()) {
            if (sign.getService().equals(service)) {
                return sign;
            }
        }
        return null; // Kein Sign mit dem service gefunden
    }
    public CloudSign getSignByLocation(Location locationToFind) {
        for (CloudSign sign : signs.values()) {
            if (sign.getLocation().equals(locationToFind)) {
                return sign;
            }
        }
        return null; // Kein Sign an dieser Location gefunden
    }



}
