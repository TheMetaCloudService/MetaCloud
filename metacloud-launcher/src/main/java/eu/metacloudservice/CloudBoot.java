package eu.metacloudservice;

import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.events.EventDriver;
import eu.metacloudservice.language.LanguageDriver;
import eu.metacloudservice.language.entry.LanguageConfig;
import eu.metacloudservice.language.entry.LanguagePacket;
import eu.metacloudservice.loader.InstanceLoader;
import eu.metacloudservice.storage.ModuleLoader;
import eu.metacloudservice.terminal.TerminalDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.update.AutoUpdater;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class CloudBoot {


    private static final String SERVICE_CONFIG_FILE = "./service.json";
    private static final String NODE_SERVICE_CONFIG_FILE = "./nodeservice.json";
    private static final String MESSAGES_STORAGE_FILE = "./local/storage/messages.storage";
    private static final String REQUIRED_JAVA_VERSION = "17";
    private static final String ROOT_USERNAME = "root";
    private static final long REQUIRED_MEMORY = 3L * 1024 * 1024 * 1024; // 3 GB in Bytes
    private static final String CLOUD_MANAGER_URL_CONFIG_KEY = "cloud-manager";
    private static final String CLOUD_NODE_URL_CONFIG_KEY = "cloud-node";
    private static final String DEPENDENCY_FOLDER = "./dependency/";
    private static final String RUNNABLE_MANAGER_JAR = "./dependency/runnable-manager.jar";
    private static final String RUNNABLE_NODE_JAR = "./dependency/runnable-node.jar";
    private static final String MODULES_FOLDER = "./modules/";

    public static void main(String[] args) {

        new Driver();

        String language = determineLanguageFromConfig();
        Driver.getInstance().getMessageStorage().language = language;
        Driver.getInstance().setLanguageDriver(new LanguageDriver());
        Driver.getInstance().getMessageStorage().eventDriver = new EventDriver();
        Driver.getInstance().setTerminalDriver(new TerminalDriver());

        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());


        validateJavaVersion();

        if (isRootUser()) {
            Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("no-root-running"));
        }

        Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-first-starting-cloud"));


        if (isNewFile(SERVICE_CONFIG_FILE) && isNewFile(NODE_SERVICE_CONFIG_FILE)) {
            handleFirstStart();
        } else {
            runClient();
        }
    }

    private static void handleFirstStart() {
          try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        waitForFinishSetup();
        Driver.getInstance().getTerminalDriver().joinSetup();

    }

    private static void waitForFinishSetup() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!Driver.getInstance().getTerminalDriver().isInSetup()) {
                    runClient();
                    timer.cancel();
                }
            }
        }, 1000, 1000);

    }

    private static String determineLanguageFromConfig() {
        if (isNewFile(SERVICE_CONFIG_FILE)) {
            return "ENGLISH";
        } else if (!isNewFile(NODE_SERVICE_CONFIG_FILE)) {
            NodeConfig config = (NodeConfig) new ConfigDriver(NODE_SERVICE_CONFIG_FILE).read(NodeConfig.class);
            return config.getLanguage();
        } else {
            ManagerConfig config = (ManagerConfig) new ConfigDriver(SERVICE_CONFIG_FILE).read(ManagerConfig.class);
            return config.getLanguage();
        }
    }

    private static void validateJavaVersion() {
        String version = System.getProperty("java.version");
        int majorVersion = Integer.parseInt(version.split("\\.")[0]);
        if (majorVersion < Integer.parseInt(REQUIRED_JAVA_VERSION)) {
            System.out.println("Please use Java " + REQUIRED_JAVA_VERSION + " or higher");
            System.exit(0);
        }
    }

    private static boolean isRootUser() {
        return System.getProperty("user.name").equalsIgnoreCase(ROOT_USERNAME);
    }

    private static boolean hasSufficientMemory() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return memoryMXBean.getHeapMemoryUsage().getCommitted() >= REQUIRED_MEMORY;
    }

    private static boolean isNewFile(String filePath) {
        return !new File(filePath).exists();
    }


    @SneakyThrows
    public static void runClient()  {
        boolean autoUpdate;

        if (!isNewFile(SERVICE_CONFIG_FILE)) {
            createStorageDirectories();
            autoUpdate = readAutoUpdateFromManagerConfig();
        } else {
            createStorageDirectories();
            autoUpdate = readAutoUpdateFromNodeConfig();
        }

        createDependencyFolderIfMissing();
        downloadMissingDependencies();

        if (autoUpdate) {
            new AutoUpdater();
        }

        createModulesFolderIfMissing();


        if (!isNewFile(SERVICE_CONFIG_FILE)) {
            new InstanceLoader(new File(RUNNABLE_MANAGER_JAR));
        } else {
            new InstanceLoader(new File(RUNNABLE_NODE_JAR));
        }
    }

    private static void createStorageDirectories() {
        String[] paths = {
                "./local/GLOBAL/EVERY/plugins/",
                "./local/GLOBAL/EVERY_SERVER/plugins/",
                "./local/GLOBAL/EVERY_PROXY/plugins/",
                "./local/GLOBAL/EVERY_LOBBY/plugins/",
                "./local/storage/",
                "./local/groups/",
                "./local/templates/"
        };

        for (String path : paths) {
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }

        if (!isNewFile(MESSAGES_STORAGE_FILE)) {
            return;
        }

        new ConfigDriver(MESSAGES_STORAGE_FILE).save(new LanguageConfig(Driver.getInstance().getLanguageDriver().getLang().getMessages()));
    }

    private static void createDependencyFolderIfMissing() throws IOException {
        if (isNewFile(DEPENDENCY_FOLDER)) {
            Path folder = Paths.get(DEPENDENCY_FOLDER);
            Files.createDirectory(folder);
            Files.setAttribute(folder, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        }
    }

    private static void downloadMissingDependencies() throws IOException {
        if (isNewFile(RUNNABLE_MANAGER_JAR) && isNewFile(RUNNABLE_NODE_JAR)) {
            if (!isNewFile(SERVICE_CONFIG_FILE)) {
                downloadDependency(CLOUD_MANAGER_URL_CONFIG_KEY, RUNNABLE_MANAGER_JAR);
            } else {
                downloadDependency(CLOUD_NODE_URL_CONFIG_KEY, RUNNABLE_NODE_JAR);
            }
        }
    }

    private static void downloadDependency(String configKey, String targetFile) throws IOException {
        String url = Driver.getInstance().getMessageStorage().getGeneralConfigFromWeb().getConfig().get(configKey);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    private static void createModulesFolderIfMissing() {
        if (isNewFile(MODULES_FOLDER)) {
            new File(MODULES_FOLDER).mkdirs();
            new ModuleLoader().downloadAllModules();
        }
    }

    private static boolean readAutoUpdateFromManagerConfig() {
        return ((ManagerConfig) new ConfigDriver(SERVICE_CONFIG_FILE).read(ManagerConfig.class)).isAutoUpdate();
    }

    private static boolean readAutoUpdateFromNodeConfig() {
        return ((NodeConfig) new ConfigDriver(NODE_SERVICE_CONFIG_FILE).read(NodeConfig.class)).isAutoUpdate();
    }



}
