package eu.metacloudservice.module;

import eu.metacloudservice.Driver;
import eu.metacloudservice.module.loader.ModuleLoader;
import eu.metacloudservice.terminal.enums.Type;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleDriver {

    private final ArrayList<ModuleLoader> loadedModules;


    public ModuleDriver() {
        loadedModules = new ArrayList<>();
    }

    public ArrayList<ModuleLoader> getLoadedModules() {
        return loadedModules;
    }

    public void load() {
        try {
            ArrayList<String> modules = getModules();

            if (modules.isEmpty()) {
                Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-folder-is-empty"));
            }
            modules.forEach(s -> {
                ModuleLoader loader = new ModuleLoader(s);
                loader.load();
                loadedModules.add(loader);
            });
        }catch (Exception e){

        }
    }

    public void unload() {
        loadedModules.forEach(ModuleLoader::unload);

        loadedModules.clear();
    }

    public void reload() {
        loadedModules.forEach(ModuleLoader::reload);
        getModules().stream().filter(s -> {
            boolean notFound = true;

            for (ModuleLoader loader : loadedModules) {
                if (loader.getJarName().equalsIgnoreCase(s)) {
                    notFound = false;
                    break;
                }
            }

            return notFound;
        }).forEach(s -> {
            ModuleLoader loader = new ModuleLoader(s);
            loader.load();
            loadedModules.add(loader);
        });
    }

    private ArrayList<String> getModules() {
        File file = new File("./modules/");
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i != Objects.requireNonNull(files).length; i++) {
            String FirstFilter = files[i].getName();
            if (FirstFilter.contains(".jar")) {
                String group = FirstFilter.split(".jar")[0];
                modules.add(group);
            }

        }
        return modules;
    }

    public List<String> getAvailableModuleNames() {
        List<String> availableModules = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://metacloudservice.eu/rest/?type=modules"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject modules = new JSONObject(response.body()).getJSONObject("modules");

            for (String key : modules.keySet()) {
                availableModules.add(modules.getString(key).replaceFirst("https://metacloudservice.eu/download/modules/", "").replace(".jar", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableModules;
    }

    public void downloadModule(String moduleName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://metacloudservice.eu/download/modules/" + moduleName + ".jar"))
                .build();

        try {
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            File moduleFile = new File("./modules/" + moduleName + ".jar");

            if (moduleFile.exists()) {
                Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-already-exists"));
                return;
            }

            moduleFile.createNewFile();
            moduleFile.setWritable(true);
            moduleFile.setReadable(true);

            Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-downloading"));

            try (InputStream in = response.body(); OutputStream out = new FileOutputStream(moduleFile)) {
                byte[] buffer = new byte[1024];
                int lengthRead;
                while ((lengthRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, lengthRead);
                }
            }

            Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-downloaded"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
