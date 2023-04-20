package eu.metacloudservice.moduleside.drivers;

import eu.metacloudservice.config.groups.GroupConfiguration;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.config.player.entry.GivenGroup;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.moduleside.MetaModule;
import eu.metacloudservice.storage.UUIDDriver;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PermissionDriver implements IPermissionDriver {

    public PermissionDriver() {}
    @Override
    public void createGroup(GroupEntry groupEntry) {
        if (getGroup(groupEntry.getName()) == null){
            GroupConfiguration configuration = getGroupConfig();
            configuration.getGroups().add(groupEntry);
            new ConfigDriver("./modules/permissions/config.json").save(configuration);
            MetaModule.update();
        }
    }

    @Override
    public void deleteGroup(String name) {
        if (getGroup(name) != null){
            GroupConfiguration configuration = getGroupConfig();
            configuration.getGroups().removeIf(groupEntry -> groupEntry.getName().equals(name));
            new ConfigDriver("./modules/permissions/config.json").save(configuration);
            MetaModule.update();
        }
    }

    @Override
    public void updateGroup(GroupEntry groupEntry) {
        if (getGroup(groupEntry.getName()) != null){
            GroupConfiguration configuration = getGroupConfig();
            configuration.getGroups().removeIf(groupEntry1 -> groupEntry1.getName().equals(groupEntry.getName()));
            configuration.getGroups().add(groupEntry);
            new ConfigDriver("./modules/permissions/config.json").save(configuration);
            MetaModule.update();
        }

    }

    @Override
    public boolean isDefaultGroup(String group) {
        return getGroup(group).isDefaultGroup();
    }

    @Override
    public void checkGroups() {
        ArrayList<GroupEntry> entrys =  getGroupConfig().getGroups();
        Boolean finde =entrys.stream().noneMatch(groupEntry -> {
            HashMap<String, String> includes = groupEntry.getInclude();
            ArrayList<String> needRemoved = new ArrayList<>();
            includes.forEach((s, s2) -> {
                if (s2.equalsIgnoreCase("LIFETIME")) return;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                LocalDateTime dateTimeA = LocalDateTime.parse(s2, dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                if (dateTimeA.isBefore(currentDateTime)) {
                    needRemoved.add(s);
                }
            });
            return !needRemoved.isEmpty();
        });
        if (!finde)return;

        List<GroupEntry> entries = entrys.stream().filter(groupEntry -> {
            HashMap<String, String> includes = groupEntry.getInclude();
            ArrayList<String> needRemoved = new ArrayList<>();
            includes.forEach((s, s2) -> {
                if (s2.equalsIgnoreCase("LIFETIME")) return;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                LocalDateTime dateTimeA = LocalDateTime.parse(s2, dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                if (dateTimeA.isBefore(currentDateTime)) {
                    needRemoved.add(s);
                }
            });
            return !needRemoved.isEmpty();
        }).toList();

        entrys.forEach(groupEntry -> {
            List<String> mustBeRemove = new ArrayList<>();
            groupEntry.getInclude().forEach((s, s2) -> {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
                LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                LocalDateTime dateTimeA = LocalDateTime.parse(s2, dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt
                if (dateTimeA.isBefore(currentDateTime)) {
                    mustBeRemove.add(s);
                }
            });
            mustBeRemove.forEach(s -> {
                groupEntry.getInclude().remove(s);
            });
            updateGroup(groupEntry);
        });
    }

    @Override
    public void changeDefault(String gtoup) {
        GroupEntry groupEntry = getGroup(gtoup);
        if (isDefaultGroup(gtoup)){
            groupEntry.setDefaultGroup(false);
            updateGroup(groupEntry);
        }else {
            groupEntry.setDefaultGroup(true);
            updateGroup(groupEntry);
        }
    }

    @Override
    public void addInclude(String group, String include, int days) {
        if (!isInclude(group, include)){
            String formattedDateTime;
            if (days != -1){
                LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
                LocalDateTime calculatedDateTime = currentDateTime.plusDays(days); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                formattedDateTime = calculatedDateTime.format(dateTimeFormatter);
            }else {
                formattedDateTime = "LIFETIME";
            }

            GroupEntry entry = getGroup(group);
            entry.getInclude().put(include, formattedDateTime);
            updateGroup(entry);
        }
    }

    @Override
    public boolean isInclude(String group, String include) {
        return getGroup(group).getInclude().containsKey(include);
    }

    @Override
    public void removeInclude(String group, String include) {
        if (!isInclude(group, include)){
            GroupEntry entry = getGroup(group);
            entry.getInclude().remove(include);
            updateGroup(entry);
        }
    }

    @Override
    public void setGroupPower(String group, int power) {
        if (getGroup(group) != null){
            GroupEntry entry = getGroup(group);
            entry.setTagPower(power);
            updateGroup(entry);
        }
    }

    @Override
    public boolean hasGroupPermission(String group, String permission) {
        return getGroup(group).getPermissions().getOrDefault(permission, false);
    }

    @Override
    public void addPermissionToGroup(String group, String permission, boolean set) {
        if (!hasGroupPermission(group,permission)){
            GroupEntry entry = getGroup(group);
            entry.getPermissions().put(permission, set);
           updateGroup(entry);
        }
    }

    @Override
    public void removePermissionFromGroup(String group, String permission) {
        if (hasGroupPermission(group,permission)){
            GroupEntry entry = getGroup(group);
            entry.getPermissions().remove(permission);
            updateGroup(entry);
        }
    }

    @Override
    public GroupEntry getGroup(String name) {
        return ((GroupConfiguration)new ConfigDriver("./modules/permissions/config.json").read(GroupConfiguration.class)).getGroups()
                .parallelStream()
                .filter(groupEntry -> groupEntry.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<GroupEntry> getDefaults() {
        return  ((GroupConfiguration)new ConfigDriver("./modules/permissions/config.json").read(GroupConfiguration.class)).getGroups()
                .parallelStream().filter(GroupEntry::isDefaultGroup).collect(Collectors.toList());
    }

    @Override
    public GroupConfiguration getGroupConfig() {
        return ((GroupConfiguration)new ConfigDriver("./modules/permissions/config.json").read(GroupConfiguration.class));
    }

    @Override
    public void updateRanks(String player) {
        if (player == null) return;
        List<GivenGroup> ranks = getPlayer(player).getGroups().stream().filter(givenGroup -> !givenGroup.getCancellationAt().equalsIgnoreCase("LIFETOME")).toList();
        if (ranks.isEmpty()) return;
        List<String> mustBeRemoved = new ArrayList<>();
        PlayerConfiguration config = getPlayer(player);


        ranks.forEach(givenGroup -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // das Format des Datums und der Uhrzeit, z.B. '01.02.2022 20:00'
            LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
            LocalDateTime dateTimeA = LocalDateTime.parse(givenGroup.getCancellationAt(), dateTimeFormatter); // konvertiere das Datum und die Uhrzeit A in ein LocalDateTime-Objekt

            if (dateTimeA.isBefore(currentDateTime)){
                mustBeRemoved.add(givenGroup.getGroup());
            }

        });

        mustBeRemoved.forEach(s -> {
            config.getGroups().removeIf(givenGroup -> givenGroup.getGroup().equalsIgnoreCase(s));
        });
        updatePlayer(config);
    }

    @Override
    public void addRankToPlayer(String player, String rank, int days) {
        String formattedDateTime;
        if (days != -1){
            LocalDateTime currentDateTime = LocalDateTime.now(); // das aktuelle Datum und die aktuelle Uhrzeit
            LocalDateTime calculatedDateTime = currentDateTime.plusDays(days); // berechne das Datum und die Uhrzeit, indem Tage zum aktuellen Datum und zur aktuellen Uhrzeit hinzugefügt werden

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            formattedDateTime = calculatedDateTime.format(dateTimeFormatter);
        }else {
            formattedDateTime = "LIFETIME";
        }

         // formatiere das berechnete Datum und die berechnete Uhrzeit als Zeichenkette im gewünschten Format
        PlayerConfiguration config = getPlayer(player);
        if (config.getGroups().stream().noneMatch(givenGroup -> givenGroup.getGroup().equalsIgnoreCase(rank))){
            GivenGroup givenGroup = new GivenGroup();
            givenGroup.setGroup(rank);
            givenGroup.setCancellationAt(formattedDateTime);
            config.getGroups().add(givenGroup);
        }else {
            config.getGroups().stream().filter(givenGroup -> givenGroup.getGroup().equalsIgnoreCase(rank)).findFirst().get().setCancellationAt(formattedDateTime);
        }
        updatePlayer(config);
    }


    @Override
    public void createPlayer(String player) {
        if (getPlayer(player) == null){
            PlayerConfiguration configuration = new PlayerConfiguration();
            ArrayList<GivenGroup> groups  = new ArrayList<>();

            getDefaults().forEach(groupEntry -> {
                GivenGroup givenGroup = new GivenGroup();
                givenGroup.setGroup(groupEntry.getName());
                givenGroup.setCancellationAt("LIFETIME");
                groups.add(givenGroup);
            });
            configuration.setGroups(groups);
            configuration.setPermissions(new HashMap<>());
            configuration.setUUID(UUIDDriver.getUUID(player));
            MetaModule.update();
        }
    }

    @Override
    public void updatePlayer(PlayerConfiguration update) {
        if (getPlayer(UUIDDriver.getUsername(update.getUUID())) != null){
            PlayerConfiguration configuration = new PlayerConfiguration();
            configuration.setGroups(update.getGroups());
            configuration.setPermissions(update.getPermissions());
            configuration.setUUID(update.getUUID());
            new ConfigDriver("./modules/permissions/cloudplayers/" + update.getUUID() + ".json").save(configuration);
            MetaModule.update();
        }
    }

    @Override
    public PlayerConfiguration getPlayer(String name) {
        return (PlayerConfiguration) new ConfigDriver("./modules/permissions/cloudplayers/" + UUIDDriver.getUUID(name) + ".json").read(PlayerConfiguration.class);
    }

    @Override
    public List<PlayerConfiguration> getPlayers() {
        File file = new File("./modules/permissions/cloudplayers/");
        File[] files = file.listFiles();
        List<PlayerConfiguration> players = new ArrayList<>();
        for (int i = 0; i != files.length; i++) {
            String FirstFilter = files[i].getName();
            if (FirstFilter.contains(".json")) {
                players.add((PlayerConfiguration) new ConfigDriver("./modules/permissions/cloudplayers/" +FirstFilter).read(PlayerConfiguration.class));
            }
        }
        return players;
    }

    @Override
    public void addPermissionToPlayer(String player, String permission, boolean set) {
        if (!hasPermission(player, permission)){
            PlayerConfiguration playerConfiguration = getPlayer(player);
            playerConfiguration.getPermissions().put(permission, set);
            updatePlayer(playerConfiguration);
        }
    }

    @Override
    public void removePermissionToPlayer(String player, String permission) {
        if (hasPermission(player, permission)){
            PlayerConfiguration playerConfiguration = getPlayer(player);
            playerConfiguration.getPermissions().remove(permission);
            updatePlayer(playerConfiguration);
        }
    }

    @Override
    public boolean playerHasGroup(String player,String group) {
        getPlayer(player).getGroups().stream().filter(givenGroup -> givenGroup.getGroup().equals(group)).findFirst().get();
        return true;
    }

    @Override
    public boolean hasPermission(String player, String permission) {
        return getPlayer(player).getPermissions().getOrDefault(permission, false);
    }

}