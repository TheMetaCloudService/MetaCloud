package eu.metacloudservice;

import eu.metacloudservice.balance.LoadBalancer;
import eu.metacloudservice.balance.subgates.SubGate;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.ConnectionType;
import eu.metacloudservice.config.General;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.listeners.CloudListener;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.utils.ProxyData;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MetaModule implements IModule {

    private static MetaModule instance;
    private HashMap<String, ProxyData> proxyStorage;
    private LoadBalancer loadBalancer;

    public static MetaModule getInstance() {
        return instance;
    }

    @Override
    public void load() {
        instance = this;
        proxyStorage = new HashMap<>();


        General general = new General("balancer", "1.0.0", "RauchigesEtwas");
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/balancer/general", new ConfigDriver().convert(general)));
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);

        if (config.getBungeecordPort() == 25565){
            config.setBungeecordPort(7000);
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO,
                    "Der Proxy-Port wurde von §f25565 §rauf §f7000 §rgeändert.",
                    "The proxy port was changed from §f25565 §rto §f7000");
            new ConfigDriver("./service.json").save(config);
        }


        if (!new File("./modules/balancer/config.json").exists()){

            new File("./modules/balancer/").mkdirs();
            Configuration configuration = new Configuration();
            configuration.setConnectionType(ConnectionType.RANDOM);
            configuration.setConnectionPort(25565);
            new ConfigDriver("./modules/balancer/config.json").save(configuration);
        }
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new CloudListener());
        Configuration s = (Configuration) new ConfigDriver("./modules/balancer/config.json").read(Configuration.class);
        this.loadBalancer = new LoadBalancer(config.getManagerAddress(), s.getConnectionPort());
    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {

    }

    public HashMap<String, ProxyData> getProxyStorage() {
        return proxyStorage;
    }

    public SubGate getRandomSub(){
        Random generator = new Random();
        ArrayList<SubGate> subGates = new ArrayList<>();
        proxyStorage.forEach((s, proxyData) -> {
            subGates.add(proxyData.getSubGate());
        });

        if (subGates.isEmpty()){
            return new SubGate("test", "127.0.0.1", 2012);
        }

        return subGates.get(generator.nextInt(subGates.size()));
    }

}
