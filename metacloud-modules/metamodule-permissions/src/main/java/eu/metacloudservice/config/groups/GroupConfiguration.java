package eu.metacloudservice.config.groups;

import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.ArrayList;

public class GroupConfiguration implements IConfigAdapter {

     private ArrayList<GroupEntry> groups;

     public ArrayList<GroupEntry> getGroups() {
          return groups;
     }

     public void setGroups(ArrayList<GroupEntry> groups) {
          this.groups = groups;
     }
}
