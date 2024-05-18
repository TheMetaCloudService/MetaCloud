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


    public static void main(String[] args) {

        new Driver();

        if (!new File("./service.json").exists()  && !new File("./nodeservice.json").exists()){
            Driver.getInstance().getMessageStorage().language = "ENGLISH";
        }else {
            if (new File("./nodeservice.json").exists()){
                NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
                Driver.getInstance().getMessageStorage().language = config.getLanguage();
            }   if (new File("./service.json").exists()) {
                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                Driver.getInstance().getMessageStorage().language = config.getLanguage();
            }
        }

        Driver.getInstance().setLanguageDriver(new LanguageDriver());
        Driver.getInstance().getMessageStorage().eventDriver = new EventDriver();
        Driver.getInstance().setTerminalDriver(new TerminalDriver());
        Driver.getInstance().getTerminalDriver().clearScreen();
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, Driver.getInstance().getMessageStorage().getAsciiArt());
        String version = System.getProperty("java.version");
        int majorVersion = Integer.parseInt(version.split("\\.")[0]);
        if (majorVersion < 17) {
            System.out.println("Pleas user Java 17 or higher");
            System.exit(0);
            return;
        }

        if (isRootUser(System.getProperty("user.name"))){
            Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("no-root-running"));
        }
        if (new File("./OLD.jar").exists()){
            new File("./OLD.jar").delete();
        }
        Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-check-if-setup-has-finished"));


       if (!new File("./service.json").exists() && !new File("./nodeservice.json").exists()){
            Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-first-starting-cloud"));
            try {Thread.sleep(2000);} catch (InterruptedException ignored) {}
            waitForFinishSetup();
            Driver.getInstance().getTerminalDriver().joinSetup();
        }else {
            Driver.getInstance().getTerminalDriver().log(Type.INFO, Driver.getInstance().getLanguageDriver().getLang().getMessage("setup-has-already-finished"));
            runClient();
        }

    }


    private static boolean hasSufficientMemory(long availableMemory) {
        // Vergleichen des verfügbaren Speichers mit 3 GB (in Bytes)
        long requiredMemory = 3L * 1024 * 1024 * 1024; // 3 GB in Bytes
        return availableMemory >= requiredMemory;
    }

    private static boolean isRootUser(String username) {
        // Vergleichen des Benutzernamens mit "root" oder anderen bekannten Namen für den Root-Benutzer
        return username.equalsIgnoreCase("root");

}

    @SneakyThrows
    public static void runClient(){
        boolean autoUpdate = false;

        if (new File("./service.json").exists()){
            String[] paths = {"./local/GLOBAL/EVERY/plugins/",
                    "./local/GLOBAL/EVERY_SERVER/plugins/",
                    "./local/GLOBAL/EVERY_PROXY/plugins/",
                    "./local/GLOBAL/EVERY_LOBBY/plugins/",
                    "./local/storage/",
                    "./local/groups/",
                    "./local/templates/"
            };


            //CREATE STORAGE FOR MESSAGES
            if (!new File("./local/storage/messages.storage").exists()){
                new ConfigDriver("./local/storage/messages.storage").save(new LanguageConfig(Driver.getInstance().getLanguageDriver().getLang().getMessages()));
            }

            for (String path : paths) {
                File directory = new File(path);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }
            autoUpdate = ((ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class)).isAutoUpdate();
        }else {
            String[] paths = {"./local/GLOBAL/EVERY/plugins/",
                    "./local/GLOBAL/EVERY_SERVER/plugins/",
                    "./local/GLOBAL/EVERY_PROXY/plugins/",
                    "./local/GLOBAL/EVERY_LOBBY/plugins/",
                    "./local/templates/"
            };

            for (String path : paths) {
                File directory = new File(path);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }
            autoUpdate = ((NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class)).isAutoUpdate();
        }


        if (!new File("./dependency/").exists()){
            Path folder = Paths.get("./dependency/");
            Files.createDirectory(folder);
            Files.setAttribute(folder, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        }

        if (!new File("./dependency/runnable-manager.jar").exists() && !new File("./dependency/runnable-node.jar").exists()) {
            if (new File("./service.json").exists()){
                try (BufferedInputStream in = new BufferedInputStream(new URL(Driver.getInstance().getMessageStorage().getGeneralConfigFromWeb().getConfig().get("cloud-manager")).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream("./dependency/runnable-manager.jar")) {
                    byte[] dataBuffer = new byte[1024];

                    int bytesRead;

                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (!new File("./service.json").exists()){
                try (BufferedInputStream in = new BufferedInputStream(new URL(Driver.getInstance().getMessageStorage().getGeneralConfigFromWeb().getConfig().get("cloud-node")).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream("./dependency/runnable-node.jar")) {
                    byte[] dataBuffer = new byte[1024];

                    int bytesRead;

                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }

        if (autoUpdate){
            new AutoUpdater();
            if (new File("./modules/").exists()){
                new File("./modules/").mkdirs();

                new ModuleLoader().downloadAllModules();
            }
        }
        if (!new File("./modules/").exists()){
            new File("./modules/").mkdirs();

            new ModuleLoader().downloadAllModules();
        }

        if (new File("./service.json").exists()){
            new InstanceLoader(new File("./dependency/runnable-manager.jar"));
        }else {
            new InstanceLoader(new File("./dependency/runnable-node.jar"));
        }
    }

    public static void  waitForFinishSetup(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               if (!Driver.getInstance().getTerminalDriver().isInSetup()){
                   runClient();
                   timer.cancel();
               }
            }
        }, 1000, 1000);
    }
}
