package eu.themetacloudservice.storage;

import com.google.common.io.BaseEncoding;
import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.restapi.UpdateConfig;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MessageStorage {
    public String version = "SANDSTORM-1.0.0";
    public String language;
    public Integer canUseMemory = 0;
    public PacketLoader packetLoader;
    public boolean shutdownAccept;
    public boolean finishSetup;
    public String setuptype = "";

    public MessageStorage() {
        packetLoader = new PacketLoader();
        shutdownAccept = false;
    }

    public String getAsciiArt(){

        if (Driver.getInstance().getTerminalDriver().isInSetup()){
            if (language.equalsIgnoreCase("DE")){
                return
                        "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                                "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                                "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                                "     <§b!§r> zum verlassen des Screens, gebe `§bleave§r` in die Console ein\n" +
                                "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";

            }else return
                    "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                            "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                            "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                            "     <§b!§r> to leave this screen again, type `§bleave§r` in the console \n" +
                            "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";
        }else {
            if (language.equalsIgnoreCase("DE")){
                return
                        "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                                "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                                "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                                "     <§b!§r> Willkommen bei Metacloud, Ihrem intelligentes Cloudsystem für Minecraft§r\n" +
                                "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";

            }else return
                    "           _  _ ____ ___ ____ ____ _    ____ _  _ ___\n" +
                            "       §bTHE§7 |\\/| |___  |  |__| |    |    |  | |  | |  \\ \n" +
                            "           |  | |___  |  |  | |___ |___ |__| |__| |__/ [§b" + version + "§7]\n\n" +
                            "     <§b!§r> welcome to Metacloud your intelligent cloud system for minecraft§r\n" +
                            "     <§b!§r> support: §fmetacloudservice.eu §r(§b@TheMetaCloud§r)\n\n";
        }

    }


    public String[] dropFirstString(String[] input){
        String[] string = new String[input.length - 1];
        System.arraycopy(input, 1, string, 0, input.length - 1);
        return string;
    }


    @SneakyThrows
    public boolean checkAvailableUpdate() {

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudversion.php").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            UpdateConfig updateConfig = (UpdateConfig) new ConfigDriver().convert(rawJson, UpdateConfig.class);
            if (!updateConfig.getLatestReleasedVersion().equalsIgnoreCase(version)) {
                return true;
            }
        }


        return false;
    }

    @SneakyThrows
    public String  getNewVersionName() {

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudversion.php").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            UpdateConfig updateConfig = (UpdateConfig) new ConfigDriver().convert(rawJson, UpdateConfig.class);
            if (!updateConfig.getLatestReleasedVersion().equalsIgnoreCase(version)) {
                return updateConfig.getLatestReleasedVersion();
            }
        }
        return "";
    }
    @SneakyThrows
    public  String utf8ToUBase64(String utf8string){
        return BaseEncoding.base64().encode(utf8string.getBytes(StandardCharsets.UTF_8));
    }

    public  String base64ToUTF8(String base64Sting){
        byte[] contentInBytes = BaseEncoding.base64().decode(base64Sting);
        return new String(contentInBytes, StandardCharsets.UTF_8);
    }



    public String getSpigotConfiguration(){
        return "settings:\n" +
                "  allow-end: true\n" +
                "  warn-on-overload: true\n" +
                "  permissions-file: permissions.yml\n" +
                "  update-folder: update\n" +
                "  plugin-profiling: false\n" +
                "  connection-throttle: 0\n" +
                "  query-plugins: true\n" +
                "  deprecated-verbose: default\n" +
                "  shutdown-message: Server closed\n" +
                "spawn-limits:\n" +
                "  monsters: 70\n" +
                "  animals: 15\n" +
                "  water-animals: 5\n" +
                "  ambient: 15\n" +
                "chunk-gc:\n" +
                "  period-in-ticks: 600\n" +
                "  load-threshold: 0\n" +
                "ticks-per:\n" +
                "  animal-spawns: 400\n" +
                "  monster-spawns: 1\n" +
                "  autosave: 6000\n" +
                "aliases: now-in-commands.yml\n" +
                "database:\n" +
                "  username: bukkit\n" +
                "  isolation: SERIALIZABLE\n" +
                "  driver: org.sqlite.JDBC\n" +
                "  password: walrus\n" +
                "  url: jdbc:sqlite:{DIR}{NAME}.db\n";
    }

    public String getSpigotProperty(int Port){
        return "#Minecraft server properties\n" +
                "#Mon Jan 25 10:33:48 CET 2021\n" +
                "spawn-protection=0\n" +
                "generator-settings=\n" +
                "force-gamemode=false\n" +
                "allow-nether=true\n" +
                "gamemode=0\n" +
                "broadcast-console-to-ops=true\n" +
                "enable-query=false\n" +
                "player-idle-timeout=0\n" +
                "difficulty=1\n" +
                "spawn-monsters=true\n" +
                "op-permission-level=0\n" +
                "resource-pack-hash=\n" +
                "announce-player-achievements=true\n" +
                "pvp=true\n" +
                "snooper-enabled=true\n" +
                "level-type=DEFAULT\n" +
                "hardcore=false\n" +
                "enable-command-block=false\n" +
                "max-players=\n" +
                "network-compression-threshold=256\n" +
                "max-world-size=29999984\n" +
                "server-port="+Port+"\n" +
                "debug=false\n" +
                "server-ip=\n" +
                "spawn-npcs=true\n" +
                "allow-flight=false\n" +
                "level-name=world\n" +
                "view-distance=10\n" +
                "resource-pack=\n" +
                "spawn-animals=true\n" +
                "white-list=false\n" +
                "generate-structures=true\n" +
                "online-mode=false\n" +
                "max-build-height=256\n" +
                "level-seed=\n" +
                "enable-rcon=false\n" +
                "motd=\"§8| §bMetaCloud §8- §7Server Service\"\n";
    }
    public String getBungeeCordConfiguration(int port, int players, boolean useProtocol){
        return "server_connect_timeout: 5000\n" +
                "remote_ping_cache: -1\n" +
                "forge_support: true\n" +
                "player_limit: -1\n" +
                "permissions:\n" +
                "  default:\n" +
                "  - bungeecord.command.server\n" +
                "  - bungeecord.command.list\n" +
                "  admin:\n" +
                "  - bungeecord.command.alert\n" +
                "  - bungeecord.command.end\n" +
                "  - bungeecord.command.ip\n" +
                "  - bungeecord.command.reload\n" +
                "  - metacloud.notify.services\n" +
                "timeout: 30000\n" +
                "log_commands: false\n" +
                "network_compression_threshold: 256\n" +
                "online_mode: true\n" +
                "disabled_commands:\n" +
                "- disabledcommandhere\n" +
                "servers:\n" +
                "  lobby:\n" +
                "    motd: '&1Just another Waterfall - Forced Host'\n" +
                "    address: localhost:25566\n" +
                "    restricted: false\n" +
                "listeners:\n" +
                "- query_port: "+port+"\n" +
                "  motd: '&bMetaCloud &7- proxy service'\n" +
                "  tab_list: GLOBAL_PING\n" +
                "  query_enabled: false\n" +
                "  proxy_protocol: false\n" +
                "  forced_hosts:\n" +
                "    pvp.md-5.net: pvp\n" +
                "  ping_passthrough: false\n" +
                "  priorities:\n" +
                "  - lobby\n" +
                "  bind_local_address: true\n" +
                "  host: 0.0.0.0:"+port+"\n" +
                "  max_players: "+players+"\n" +
                "  tab_size: 60\n" +
                "  force_default_server: false\n" +
                "ip_forward: false\n" +
                "remote_ping_timeout: 5000\n" +
                "prevent_proxy_connections: false\n" +
                "groups:\n" +
                "  RauchigesEtwas:\n" +
                "  - admin\n" +
                "connection_throttle: 4000\n" +
                "stats: ddace828-7313-4d44-a1e2-6736196a8ab5\n" +
                "connection_throttle_limit: 3\n" +
                "log_pings: true\n";
    }

}
