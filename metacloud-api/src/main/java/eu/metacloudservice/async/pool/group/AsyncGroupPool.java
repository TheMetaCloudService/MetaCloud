/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.async.pool.group;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInCreateGroup;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInDeleteGroup;
import eu.metacloudservice.networking.packet.packets.in.service.cloudapi.PacketInStopGroup;
import eu.metacloudservice.webserver.dummys.GroupList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AsyncGroupPool {

    public AsyncGroupPool() {}

    public CompletableFuture<ArrayDeque<String>> getGroupsByName(){
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        return CompletableFuture.supplyAsync(cech::getGroups);
    }
    public CompletableFuture<ArrayList<Group>> getGroups(){
        ArrayList<Group> groups = new ArrayList<>();
        GroupList cech = (GroupList) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/general"), GroupList.class);
        cech.getGroups().forEach(s -> {
            Group g = (Group) new ConfigDriver().convert(CloudAPI.getInstance().getRestDriver().get("/cloudgroup/" + s), Group.class);
            groups.add(g);
        });
        return CompletableFuture.supplyAsync(()->groups );
    }
    public boolean isGroupExists(String group){
        try {
            return getGroupsByName().get().stream().anyMatch(s -> s.equalsIgnoreCase(group));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public void createGroup(Group group){
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInCreateGroup(new ConfigDriver().convert(group)));
    }

    public void deleteGroup(String group){
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInDeleteGroup(group));
    }


    public void stopGroup(String group){
        CloudAPI.getInstance().sendPacketAsynchronous(new PacketInStopGroup(group));
    }

    public CompletableFuture<Group> getGroup(String group){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getGroups().get().stream().filter(group1 -> group1.getGroup().equalsIgnoreCase(group)).findFirst().orElse(null);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<List<Group>> getGroups(String[] group){
        return CompletableFuture.supplyAsync( ()-> {
            try {
                return getGroups().get().stream().filter(group1 -> Arrays.stream(group).anyMatch(s -> s.equalsIgnoreCase(group1.getGroup()))).collect(Collectors.toList());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
