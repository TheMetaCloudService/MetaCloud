package eu.themetacloudservice.groups;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.groups.interfaces.IGroupDriver;
import eu.themetacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;
import java.io.File;
import java.util.ArrayList;

public class GroupDriver implements IGroupDriver {

    public GroupDriver() {}

    @Override
    public Group load(String name) {
        if (find(name)){
            return (Group) new ConfigDriver("./local/groups/" + name+ ".json").read(Group.class);
        }
        return null;
    }

    @Override
    public String loadJson(String name) {

        if (find(name)){
            return new ConfigDriver().convert(load(name));
        }
        return null;
    }


    @Override
    public boolean find(String name) {
        return new File("./local/groups/" + name + ".json").exists();
    }


    @Override
    public void create(Group group) {
        if (!find(group.getGroup())){
            if (!Driver.getInstance().getTemplateDriver().get().contains(group.getStorage().getTemplate())){
                boolean isProxyes = group.getGroupType().equalsIgnoreCase("PROXY");
                boolean isStatic = group.isRunStatic();

                if (group.getStorage().getRunningNode().equals("InternalNode")){
                    Driver.getInstance().getTemplateDriver().create(group.getGroup(), isProxyes, isStatic);
                }
            }
            new ConfigDriver("./local/groups/" + group.getGroup()+ ".json").save(group);
            Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS, "die Gruppe '§f"+group.getGroup()+"§r' wurde erfolgreich erstellt", "the group '§f"+group.getGroup()+"§r' was successfully created");

        }
    }

    @Override
    public void delete(String group) {
        if (find(group)){
            new File("./local/groups/" + group+ ".json").delete();
        }
    }

    public String getVersionByTemplate(String template){
        ArrayList<Group> g = getAll();
        for (int i = 0; i !=g.size(); i++) {
            Group group = g.get(i);
            if (group.getStorage().getTemplate().equalsIgnoreCase(template)){
                return group.getGroupType();
            }
        }

        return null;
    }

    @Override
    public ArrayList<Group> getAll() {
        ArrayList<Group> groups = new ArrayList<>();
        File file = new File("./local/groups/");
        File[] files = file.listFiles();
        for (int i = 0; i != (files != null ? files.length : 0); i++) {
            String first = files[i].getName();
            String group = first.split(".json")[0];
            groups.add(load(group));
        }
        return groups;
    }


    @Override
    public ArrayList<Group> getByNode(String node) {
        ArrayList<Group> groups = new ArrayList<>();
        getAll().forEach(group -> {
            if (group.getStorage().getRunningNode().equalsIgnoreCase(node)){
                groups.add(group);
            }
        });
        return groups;
    }

    @SneakyThrows
    @Override
    public void update(String name, Group group) {
        if (find(name)){
            new ConfigDriver("./local/groups/" + name+ ".json").save(group);
        }
    }
}
