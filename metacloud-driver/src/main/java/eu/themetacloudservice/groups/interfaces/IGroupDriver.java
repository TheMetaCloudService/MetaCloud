package eu.themetacloudservice.groups;

import eu.themetacloudservice.groups.dummy.Group;

import java.util.ArrayList;

public interface IGroupDriver {

    Group load(String name);
    Group find(String name);
    void create(Group group);
    void delete(String string);
    void handleExecute(String name, Integer amount);
    ArrayList<Group> getByNode(String node);

}
