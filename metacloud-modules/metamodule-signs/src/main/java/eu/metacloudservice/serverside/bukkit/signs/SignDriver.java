package eu.metacloudservice.serverside.bukkit.signs;


import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.serverside.bukkit.utils.SignWorker;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.UUID;

public class SignDriver {

    private final ArrayList<CloudSign> cloudSigns;
    private final SignWorker worker;

    public SignDriver() {
        cloudSigns = new ArrayList<>();
        worker = new SignWorker();
        worker.setDaemon(true);
        worker.start();
    }

    public ArrayList<CloudSign> getCloudSigns() {
        return cloudSigns;
    }

    public boolean isOnSign(String server){
        return cloudSigns.stream().parallel().anyMatch(cloudSign -> cloudSign.getServer().equalsIgnoreCase(server));
    }

    public CloudSign getSign(String server){
        return cloudSigns.stream().parallel().filter(cloudSign -> cloudSign.getServer().equalsIgnoreCase(server)).findFirst().orElse(null);
    }

    public void handleCreateSign(CloudSign cloudSign){
        if (cloudSigns.parallelStream().noneMatch(cloudSign1 -> cloudSign1.getUuid().equals(cloudSign.getUuid()))){
            cloudSigns.add(cloudSign);
        }
    }

    public void handleSignRemove(UUID  uuid){
        if (cloudSigns.parallelStream().anyMatch(cloudSign1 -> cloudSign1.getUuid().equals(uuid))){
            cloudSigns.removeIf(cloudSign -> cloudSign.getUuid().equals(uuid));
        }
    }

    public void updateSign(UUID uuid, String server){
        if (cloudSigns.parallelStream().anyMatch(cloudSign1 -> cloudSign1.getUuid().equals(uuid))){
          cloudSigns.parallelStream().filter(cloudSign -> cloudSign.getUuid().equals(uuid)).findFirst().get().setServer(server);
        }
    }

    private @Nullable String getFreeServer(String group){
        CloudService service = CloudAPI.getInstance().getServicePool().getServicesByGroup(group).parallelStream()
                .filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                .filter(cloudService -> cloudSigns.stream().parallel().noneMatch(cloudSign -> cloudSign.getServer().equalsIgnoreCase(cloudService.getName()))).findFirst().orElse(null);
        return service != null ? service.getName() : null;
    }

    public CloudSign findFreeSign(String group){
        return cloudSigns.parallelStream()
                .filter(cloudSign -> cloudSign.getGroup().equalsIgnoreCase(group))
                .filter(cloudSign -> cloudSign.getServer().isEmpty())
                .findFirst().orElse(null);
    }

    public UUID getSignID(Location location){
        return getSign(location).getUuid();
    }

    public CloudSign getSign(Location location){
        return cloudSigns.parallelStream().filter(cloudSign -> cloudSign.getSignPosition().getWorld().equals(location.getWorld())
                && cloudSign.getSignPosition().getX() == location.getX()
                && cloudSign.getSignPosition().getY() == location.getY()
                && cloudSign.getSignPosition().getZ() == location.getZ()).findFirst().orElse(null);
    }


}
