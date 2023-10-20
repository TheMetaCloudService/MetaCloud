package eu.metacloudservice.groups;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.listeners.group.CloudGroupCreateEvent;
import eu.metacloudservice.events.listeners.group.CloudGroupDeleteEvent;
import eu.metacloudservice.events.listeners.group.CloudGroupUpdateEditEvent;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.groups.interfaces.IGroupDriver;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.out.service.group.PacketOutGroupCreate;
import eu.metacloudservice.networking.out.service.group.PacketOutGroupDelete;
import eu.metacloudservice.networking.out.service.group.PacketOutGroupEdit;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.dummys.GroupList;
import eu.metacloudservice.webserver.entry.RouteEntry;
import lombok.SneakyThrows;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.TimerTask;

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
            Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudGroupCreateEvent(group.getGroup()));
                if ( NettyDriver.getInstance() != null ) NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutGroupCreate(group.getGroup()));

            if (   Driver.getInstance().getWebServer() != null){
                GroupList groupList = (GroupList) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudgroup/general"), GroupList.class);
                groupList.getGroups().add(group.getGroup());
                Driver.getInstance().getWebServer().updateRoute("/cloudgroup/general", new ConfigDriver().convert(groupList));
                Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudgroup/" + group.getGroup(), new ConfigDriver().convert(group)));

            }
            new ConfigDriver("./local/groups/" + group.getGroup()+ ".json").save(group);
      Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS, "die Gruppe '§f"+group.getGroup()+"§r' wurde erfolgreich erstellt", "the group '§f"+group.getGroup()+"§r' was successfully created");
        }
    }

    @Override
    public void delete(String group) {
        if (find(group)){

            GroupList groupList = (GroupList) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudgroup/general"), GroupList.class);
            groupList.getGroups().removeIf(s -> s.equalsIgnoreCase(group));
            Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudGroupDeleteEvent(group));
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutGroupDelete(group));
            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/general", new ConfigDriver().convert(groupList));
            Driver.getInstance().getWebServer().removeRoute("/cloudgroup/" + group);
            TimerBase base = new TimerBase();
            base.schedule(new TimerTask() {
                @Override
                public void run() {
                    new File("./local/groups/" + group+ ".json").delete();
                }
            }, 10, TimeUtil.SECONDS);
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
    public ArrayDeque<String> getAllStrings() {
        ArrayDeque<String> groups = new ArrayDeque<>();
        File file = new File("./local/groups/");
        File[] files = file.listFiles();
        for (int i = 0; i != (files != null ? files.length : 0); i++) {
            String first = files[i].getName();
            String group = first.split(".json")[0];
            groups.add(group);
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
            Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudGroupUpdateEditEvent(group.getGroup()));
            NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutGroupEdit(group.getGroup()));
            new ConfigDriver("./local/groups/" + name+ ".json").save(group);
        }
    }
}