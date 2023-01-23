package eu.themetacloudservice.module;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public class ModuleDriver {

    public ArrayList<ModuleClassLoader> loadedModules = new ArrayList<>();
    public ArrayList<String> loadedModulesPerMame = new ArrayList<>();

    public void loadAllModules(){
        ArrayList<String> paths = getModules();
        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Es wird versucht, die Module aus dem '§fModulverzeichnis§r' zu laden.",
                "An attempt is made to load the modules in the '§fmodule directory§r'.");

        if (paths.isEmpty()) {
            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das '§fModulverzeichnis§r' ist leer oder nicht vorhanden. Es können keine Module geladen werden",
                    "The '§fmodule directory§r' is empty or does not exist. No modules can be loaded");
        return;
        }

        for (int i = 0; i != paths.size() ; i++) {
            if(!loadedModulesPerMame.contains(paths.get(i))){
                loadedModulesPerMame.add(paths.get(i));
                loadedModules.add(new ModuleClassLoader(paths.get(i)));
            }
        }
    }

    public void loadSideModule(String jar){

        if (loadedModules.contains(jar)) return;
        loadedModules.add(new ModuleClassLoader(jar));

    }

    public void disableAllModules(){
        for (int i = 0; i != loadedModules.size(); i++) {
            if(loadedModulesPerMame.contains(loadedModules.get(i).modulename)){
                loadedModulesPerMame.remove(loadedModules.get(i).modulename);
                loadedModules.get(i).disableModule();
            }
        }
    }

    public HashMap<String, Properties> getAllProperties(){
        HashMap<String, Properties> loadedProperties = new HashMap<>();
        this.getModules().forEach(s -> {
            ModuleClassLoader loadedModule = new ModuleClassLoader(s);
            this.loadedModules.add(loadedModule);
            Properties properties = loadedModule.readProperties();
            loadedProperties.put(s, properties);
        });

        return loadedProperties;
    }


    public void reloadAllModules(){
        for (int i = 0; i != loadedModules.size(); i++) {
            loadedModules.get(i).reloadModule();
        }
    }

    private ArrayList<String> getModules() {
        File file = new File("./modules/");
        if (!file.exists()){
            return new ArrayList<>();
        }
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            String FirstFilter = files[i].getName();
            String group = FirstFilter.split(".jar")[0];
            modules.add(group);
        }
        return modules;
    }


}
