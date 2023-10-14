package eu.metacloudservice.serverside.bukkit.drivers;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.SignsAPI;
import eu.metacloudservice.serverside.bukkit.SignBootstrap;
import eu.metacloudservice.serverside.bukkit.entry.CloudSign;
import org.bukkit.scheduler.BukkitRunnable;

public class SignUpdaterTask extends BukkitRunnable {
    private final SignsAPI signsAPI;

    public SignUpdaterTask(SignsAPI signsAPI) {
        this.signsAPI = signsAPI;
    }

    @Override
    public void run() {
        if (signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null) == null) return;
        SignBootstrap.empty = incrementLayoutIndex(SignBootstrap.empty, signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getEmpty().size());
        SignBootstrap.online = incrementLayoutIndex(SignBootstrap.online, signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getOnline().size());
        SignBootstrap.full = incrementLayoutIndex(SignBootstrap.full, signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getFull().size());
        SignBootstrap. maintenance = incrementLayoutIndex(SignBootstrap.maintenance, signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getMaintenance().size());
        SignBootstrap.searching = incrementLayoutIndex(SignBootstrap.searching, signsAPI.getConfig().configurations.stream().filter(signConfig -> signConfig.getTargetGroup().equalsIgnoreCase(CloudAPI.getInstance().getCurrentService().getGroup())).findFirst().orElse(null).getSearching().size());

        if (!SignBootstrap.signDriver.getFreeSigns().isEmpty() && !SignBootstrap.signDriver.getAllSigns().isEmpty()) {
            SignBootstrap.signDriver.getFreeSigns().forEach(uuid -> {
                CloudSign cloudSign = SignBootstrap.signDriver.getSignByUUID(uuid);
                if (!CloudAPI.getInstance().getGroupPool().getGroup(cloudSign.getGroup()).isMaintenance() && !SignBootstrap.signDriver.getFreeServers(cloudSign.getGroup()).isEmpty()) {
                    cloudSign.setService(SignBootstrap.signDriver.getFreeServers(cloudSign.getGroup()).get(0));
                }
            });
        }
        SignBootstrap.signDriver.getAllSigns().forEach(cloudSign -> cloudSign.drawSign());
    }

    private int incrementLayoutIndex(int currentIndex, int maxSize) {
        return (currentIndex >= maxSize - 1) ? 0 : currentIndex + 1;
    }
}