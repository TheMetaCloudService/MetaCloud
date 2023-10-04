/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.pool.groupe;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.in.service.cloudapi.PacketInCreateGroup;
import eu.metacloudservice.networking.in.service.cloudapi.PacketInDeleteGroup;
import eu.metacloudservice.networking.in.service.cloudapi.PacketInStopGroup;
import eu.metacloudservice.webserver.dummys.GroupList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupPool {

    public GroupPool() {}

    public ArrayDeque<String> getGroupsByName(){
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        return cech.getGroups();
    }
    public ArrayList<Group> getGroups(){
        ArrayList<Group> groups = new ArrayList<>();
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        cech.getGroups().forEach(s -> {
            Group g = (Group) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/" + s), Group.class);
            groups.add(g);
        });
        return groups;
    }


    public void createGroup(Group group){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInCreateGroup(new ConfigDriver().convert(group)));
    }

    public void deleteGroup(String group){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInDeleteGroup(group));
    }


    public void stopGroup(String group){
        CloudAPI.getInstance().sendPacketSynchronized(new PacketInStopGroup(group));
    }

    public Group getGroup(String group){
        return getGroups().stream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null);
    }

    public List<Group> getGroups(String[] group){
        return getGroups().stream().filter(group1 -> Arrays.stream(group).anyMatch(s -> s.equalsIgnoreCase(group1.getGroup()))).collect(Collectors.toList());
    }
}
