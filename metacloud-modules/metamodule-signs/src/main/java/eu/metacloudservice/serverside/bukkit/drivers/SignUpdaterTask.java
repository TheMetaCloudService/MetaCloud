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
        SignBootstrap.empty = incrementLayoutIndex(SignBootstrap.empty, signsAPI.getConfig().getEmpty().size());
        SignBootstrap.online = incrementLayoutIndex(SignBootstrap.online, signsAPI.getConfig().getOnline().size());
        SignBootstrap.full = incrementLayoutIndex(SignBootstrap.full, signsAPI.getConfig().getFull().size());
        SignBootstrap. maintenance = incrementLayoutIndex(SignBootstrap.maintenance, signsAPI.getConfig().getMaintenance().size());
        SignBootstrap.searching = incrementLayoutIndex(SignBootstrap.searching, signsAPI.getConfig().getSearching().size());

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