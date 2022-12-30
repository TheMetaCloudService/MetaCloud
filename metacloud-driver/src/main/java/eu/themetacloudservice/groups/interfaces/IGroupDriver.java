package eu.themetacloudservice.groups.interfaces;

import eu.themetacloudservice.groups.dummy.Group;

import java.util.ArrayList;

public interface IGroupDriver {

    Group load(String name);
    boolean find(String name);
    void create(Group group);
    void delete(String group);
    ArrayList<Group> getAll();
    ArrayList<Group> getByNode(String node);

}
