/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.velocity.listener.CloudListener;
import eu.metacloudservice.webserver.RestDriver;

@Plugin(id = "metacloudmodulenotify", version = "1.1.1-RELEASE", name = "metacloud-notify", authors = "RauchigesEtwas", dependencies = {@Dependency(id = "metacloudapi"), @Dependency(id = "metacloudplugin")})
public class VelocityBootstrap {

    private static VelocityBootstrap instance;
    private LiveService liveService;
    private RestDriver restDriver;
    public  ProxyServer proxyServer;

    @Inject
    public VelocityBootstrap(ProxyServer proxyServer) {
        instance = this;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    private void handelInject(ProxyInitializeEvent event){
        liveService = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        restDriver = new RestDriver(liveService.getManagerAddress(), liveService.getRestPort());
        CloudAPI.getInstance().registerListener(new CloudListener(proxyServer));
    }


    public LiveService getLiveService() {
        return liveService;
    }

    public RestDriver getRestDriver() {
        return restDriver;
    }

    public static VelocityBootstrap getInstance() {
        return instance;
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }
}
