package eu.themetacloudservice.groups;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.groups.interfaces.IGroupDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

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
    public boolean find(String name) {
        if (new File("./local/groups/" + name + ".json").exists()) {
            return true;
        }
        return false;
    }

    @Override
    public void create(Group group) {
        if (!find(group.getGroup())){
            if (!Driver.getInstance().getTemplateDriver().get().contains(group.getStorage().getTemplate())){
                boolean isProxyes = group.getGroupType().equalsIgnoreCase("PROXY");

                Driver.getInstance().getTemplateDriver().create(group.getGroup(), isProxyes);
            }
            new ConfigDriver("./local/groups/" + group.getGroup()+ ".json").save(group);
        }
    }

    @Override
    public void delete(String group) {
        if (find(group)){
            new File("./local/groups/" + group+ ".json").delete();
            //todo: shutdown all running processes
        }
    }

    @Override
    public ArrayList<Group> getAll() {
        ArrayList<Group> groups = new ArrayList<>();
        File file = new File("./local/groups/");
        File[] files = file.listFiles();
        for (int i = 0; i != files.length; i++) {
            String Group = files[i].getName();
            groups.add(load(Group));
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
}
