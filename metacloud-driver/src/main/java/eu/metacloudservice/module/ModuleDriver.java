package eu.metacloudservice.module;

import eu.metacloudservice.Driver;
import eu.metacloudservice.module.loader.ModuleLoader;
import eu.metacloudservice.terminal.enums.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ModuleDriver {

    private ArrayList<ModuleLoader> loadedModules;


    public ModuleDriver() {
        loadedModules = new ArrayList<>();
    }

    public ArrayList<ModuleLoader> getLoadedModules() {
        return loadedModules;
    }

    public void load() {
        ArrayList<String> modules = getModules();

        if (modules.isEmpty()) {
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Ordner modules ist leer, d.h. es kann nichts geladen werden", "The modules folder is empty, that means nothing can be loaded");
        }
        modules.forEach(s -> {
            ModuleLoader loader = new ModuleLoader(s);
            loader.load();
            loadedModules.add(loader);
        });
    }

    public void unload() {
        loadedModules.forEach(moduleLoader -> {
            moduleLoader.unload();
        });

        loadedModules.clear();
    }

    public void reload() {
        loadedModules.forEach(moduleLoader -> {
            moduleLoader.reload();
        });
        getModules().stream().filter(s -> {
            boolean notFound = true;

            for (ModuleLoader loader : loadedModules) {
                if (loader.getJarName().equalsIgnoreCase(s)) {
                    notFound = false;
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
        for (int i = 0; i != files.length; i++) {
            String FirstFilter = files[i].getName();
            if (FirstFilter.contains(".jar")) {
                String group = FirstFilter.split(".jar")[0];
                modules.add(group);
            }

        }
        return modules;
    }
}
