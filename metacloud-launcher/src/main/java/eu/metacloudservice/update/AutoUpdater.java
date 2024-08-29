/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.update;

import eu.metacloudservice.Driver;
import eu.metacloudservice.storage.ModuleLoader;
import eu.metacloudservice.terminal.animation.AnimationDriver;
import eu.metacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutoUpdater {
    private static final String UPDATE_URL = "https://metacloudservice.eu/download/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    private static final String UPDATE_JAR_NAME = "UPDATE.jar";
    private static final String METACLOUD_PLUGIN_JAR = "metacloud-plugin.jar";
    private static final String METACLOUD_API_JAR = "metacloud-api.jar";
    private static final String LOCAL_EVERY_PLUGINS_PATH = "./local/GLOBAL/EVERY/plugins/";
    private static final String MODULE_PATH = "./modules/";
    private static final String DEPENDENCY_PATH = "./dependency/";
    private static final String LAUNCHER_JAR = "./Launcher.jar";
    private static final String OLD_JAR = "./OLD.jar";
    private static final String STORAGE_MESSAGES = "./local/storage/messages.storage";

    @SneakyThrows
    public AutoUpdater() {
        checkForUpdate();
    }

    public static void checkForUpdate() throws IOException {

        logInfo(Driver.getInstance().getLanguageDriver().getLang().getMessage("update-check-if-update-found"));
        if (Driver.getInstance().getMessageStorage().checkAvailableUpdate()) {
            String currentVersion = Driver.getInstance().getMessageStorage().version;
            String newVersionName = Driver.getInstance().getMessageStorage().getNewVersionName();

            logInfo(Driver.getInstance().getLanguageDriver().getLang().getMessage("update-is-found")
                    .replace("%current_version%", currentVersion)
                    .replace("%new_version%", newVersionName));

            downloadFile(UPDATE_URL + UPDATE_JAR_NAME, UPDATE_JAR_NAME);
            new AnimationDriver().play();

            deleteOnExit(LOCAL_EVERY_PLUGINS_PATH + METACLOUD_API_JAR);
            deleteOnExit(LOCAL_EVERY_PLUGINS_PATH + METACLOUD_PLUGIN_JAR);
            deleteOnExit(DEPENDENCY_PATH + "runnable-manager.jar");
            deleteOnExit(DEPENDENCY_PATH + "runnable-node.jar");

            logInfo("Update §f" + METACLOUD_PLUGIN_JAR + "...");
            downloadFile(Driver.getInstance().getMessageStorage().getGeneralConfigFromWeb().getConfig().get("cloud-plugin"), LOCAL_EVERY_PLUGINS_PATH + METACLOUD_PLUGIN_JAR);
            new AnimationDriver().play();

            logInfo("Update §f" + METACLOUD_API_JAR + "...");
            downloadFile(Driver.getInstance().getMessageStorage().getGeneralConfigFromWeb().getConfig().get("cloud-api"), LOCAL_EVERY_PLUGINS_PATH + METACLOUD_API_JAR);
            new AnimationDriver().play();

            //MODULE UPDATER
            List<String> foundedOnlineModule = new ArrayList<>();
            new ModuleLoader().getModules().getModules().forEach((s, s2) -> foundedOnlineModule.add(s2.replace("https://metacloudservice.eu/download/modules/", "")));
            getModules().forEach(s -> {
                if (foundedOnlineModule.contains(s)) {
                    logInfo("Update §f" + s + "...");
                    deleteOnExit(MODULE_PATH + s);
                    try {
                        downloadFile(UPDATE_URL +"modules/"+  s, MODULE_PATH + s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    new AnimationDriver().play();
                }

            });

            renameFile(LAUNCHER_JAR, OLD_JAR);
            renameFile(UPDATE_JAR_NAME, LAUNCHER_JAR);

            logInfo(Driver.getInstance().getLanguageDriver().getLang().getMessage("update-is-finished"));
            deleteOnExit(STORAGE_MESSAGES);
            System.exit(0);
        } else {
            logInfo(Driver.getInstance().getLanguageDriver().getLang().getMessage("update-no-update-was-found"));
        }
    }

    private static void downloadFile(String url, String target) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setRequestProperty("User-Agent", USER_AGENT);
        urlConnection.connect();
        Files.copy(urlConnection.getInputStream(), Paths.get(target));
    }

    private static void deleteOnExit(String path) {
        new File(path).delete();
    }

    private static void renameFile(String source, String target) throws IOException {
        Files.move(Paths.get(source), Paths.get(target));
    }

    private static void logInfo(String message) {
        Driver.getInstance().getTerminalDriver().log(Type.INFO, message);
    }


    private static ArrayList<String> getModules() {
        final File file = new File("./modules/");
        final File[] files = file.listFiles();
        final ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i != Objects.requireNonNull(files).length; i++) {
            final String FirstFilter = files[i].getName();
            if (FirstFilter.contains(".jar")) {
                modules.add(FirstFilter);
            }

        }
        return modules;
    }
}


