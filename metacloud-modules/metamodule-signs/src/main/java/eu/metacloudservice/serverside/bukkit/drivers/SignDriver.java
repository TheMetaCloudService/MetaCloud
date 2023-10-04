/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.drivers;

import eu.metacloudservice.serverside.bukkit.entry.CloudSign;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SignDriver {

    private final List<CloudSign> signCache;
    private final List<String> queuedServer;

    public SignDriver() {
        this.signCache = new ArrayList<>();
        this.queuedServer = new ArrayList<>();
    }

    public String pullService(String group){
        String service = getQueuedServer().parallelStream().filter(s -> s.startsWith(group)).findFirst().orElse("");
        if (service.equals("")) return "";
        else {
            getQueuedServer().removeIf(s -> s.equals(service));
            return service;
        }
    }

    public CloudSign getSignFromLocation(Location location){
        return getSignCache().parallelStream().filter(sign -> sign.getSignPosition() == location).findFirst().orElse(null);
    }

    public boolean isOnLocationASign(Location location){
        return getSignCache().parallelStream().anyMatch(sign -> sign.getSignPosition() == location);
    }

    public CloudSign getSignFromService(String service){
        if (isServiceOnSign(service)) {
            return getSignCache().parallelStream().filter(sign -> sign.getService().equalsIgnoreCase(service)).findFirst().orElse(null);
        }
        return null;
    }

    public boolean isServiceOnSign(String service){
        return getSignCache().parallelStream().anyMatch(sign -> sign.getService().equalsIgnoreCase(service));
    }

    public void registerSign(CloudSign sign){
        if (!isSignExists(sign.getSignUUID())){
            getSignCache().add(sign);
        }
    }
    public void unregisterSign(UUID uuid){
        if (isSignExists(uuid)){
            getSignCache().removeIf(sign -> sign.getSignUUID() == uuid);
        }
    }

    private boolean isSignExists(UUID uuid){
        return getSignCache().parallelStream().anyMatch(cloudSign -> cloudSign.getSignUUID() == uuid);
    }

    public List<CloudSign> getSignCache() {
        return signCache;
    }

    public List<String> getQueuedServer() {
        return queuedServer;
    }
}
