package eu.themetacloudservice.module;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class ModuleDriver {

    public ArrayList<ModuleClassLoader> loadedModules = new ArrayList<>();
    public ArrayList<String> loadedModulesPerMame = new ArrayList<>();

    public void loadAllModules(){
        ArrayList<String> paths = getModules();
        if (paths.isEmpty()) {
            Driver.getInstance().getTerminalDriver().logSpeed(Type.MODULES, "es wurden keine §fModule§r gefunden", "no §fmodules§r were found");
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
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String FirstFilter = files[i].getName();
            String group = FirstFilter.split(".jar")[0];
            modules.add(group);
        }
        return modules;
    }


}
