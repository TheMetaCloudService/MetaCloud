package eu.metacloudservice.storage;

import com.google.common.io.BaseEncoding;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.restapi.UpdateConfig;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MessageStorage {
    public String version = "BETA-1.0.0";
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


    public String getVelocityToml(int port, int maxPlayers, boolean useProtocol){
        return "# Config version. Do not change this\n" +
                "config-version = \"2.5\"\n" +
                "\n" +
                "# What port should the proxy be bound to? By default, we'll bind to all addresses on port 25577.\n" +
                "bind = \"0.0.0.0:"+port+"\"\n" +
                "\n" +
                "# What should be the MOTD? This gets displayed when the player adds your server to\n" +
                "# their server list. Legacy color codes and JSON are accepted.\n" +
                "motd = \"Metacloud - Bungeecord\"\n" +
                "\n" +
                "# What should we display for the maximum number of players? (Velocity does not support a cap\n" +
                "# on the number of players online.)\n" +
                "show-max-players = "+maxPlayers+"\n" +
                "\n" +
                "# Should we authenticate players with Mojang? By default, this is on.\n" +
                "online-mode = true\n" +
                "\n" +
                "# Should the proxy enforce the new public key security standard? By default, this is on.\n" +
                "force-key-authentication = true\n" +
                "\n" +
                "# If client's ISP/AS sent from this proxy is different from the one from Mojang's\n" +
                "# authentication server, the player is kicked. This disallows some VPN and proxy\n" +
                "# connections but is a weak form of protection.\n" +
                "prevent-client-proxy-connections = false\n" +
                "\n" +
                "# Should we forward IP addresses and other data to backend servers?\n" +
                "# Available options:\n" +
                "# - \"none\":        No forwarding will be done. All players will appear to be connecting\n" +
                "#                  from the proxy and will have offline-mode UUIDs.\n" +
                "# - \"legacy\":      Forward player IPs and UUIDs in a BungeeCord-compatible format. Use this\n" +
                "#                  if you run servers using Minecraft 1.12 or lower.\n" +
                "# - \"bungeeguard\": Forward player IPs and UUIDs in a format supported by the BungeeGuard\n" +
                "#                  plugin. Use this if you run servers using Minecraft 1.12 or lower, and are\n" +
                "#                  unable to implement network level firewalling (on a shared host).\n" +
                "# - \"modern\":      Forward player IPs and UUIDs as part of the login process using\n" +
                "#                  Velocity's native forwarding. Only applicable for Minecraft 1.13 or higher.\n" +
                "player-info-forwarding-mode = \"NONE\"\n" +
                "\n" +
                "# If you are using modern or BungeeGuard IP forwarding, configure a file that contains a unique secret here.\n" +
                "# The file is expected to be UTF-8 encoded and not empty.\n" +
                "forwarding-secret-file = \"forwarding.secret\"\n" +
                "\n" +
                "# Announce whether or not your server supports Forge. If you run a modded server, we\n" +
                "# suggest turning this on.\n" +
                "# \n" +
                "# If your network runs one modpack consistently, consider using ping-passthrough = \"mods\"\n" +
                "# instead for a nicer display in the server list.\n" +
                "announce-forge = false\n" +
                "\n" +
                "# If enabled (default is false) and the proxy is in online mode, Velocity will kick\n" +
                "# any existing player who is online if a duplicate connection attempt is made.\n" +
                "kick-existing-players = false\n" +
                "\n" +
                "# Should Velocity pass server list ping requests to a backend server?\n" +
                "# Available options:\n" +
                "# - \"disabled\":    No pass-through will be done. The velocity.toml and server-icon.png\n" +
                "#                  will determine the initial server list ping response.\n" +
                "# - \"mods\":        Passes only the mod list from your backend server into the response.\n" +
                "#                  The first server in your try list (or forced host) with a mod list will be\n" +
                "#                  used. If no backend servers can be contacted, Velocity won't display any\n" +
                "#                  mod information.\n" +
                "# - \"description\": Uses the description and mod list from the backend server. The first\n" +
                "#                  server in the try (or forced host) list that responds is used for the\n" +
                "#                  description and mod list.\n" +
                "# - \"all\":         Uses the backend server's response as the proxy response. The Velocity\n" +
                "#                  configuration is used if no servers could be contacted.\n" +
                "ping-passthrough = \"DISABLED\"\n" +
                "\n" +
                "# If not enabled (default is true) player IP addresses will be replaced by <ip address withheld> in logs\n" +
                "enable-player-address-logging = true\n" +
                "\n" +
                "[servers]\n" +
                "# Configure your servers here. Each key represents the server's name, and the value\n" +
                "# represents the IP address of the server to connect to.\n" +
                "lobby = \"127.0.0.1:30066\"\n" +
                "factions = \"127.0.0.1:30067\"\n" +
                "minigames = \"127.0.0.1:30068\"\n" +
                "\n" +
                "# In what order we should try servers when a player logs in or is kicked from a server.\n" +
                "try = [\n" +
                "    \"lobby\"\n" +
                "]\n" +
                "\n" +
                "[forced-hosts]\n" +
                "# Configure your forced hosts here.\n" +
                "\"lobby.example.com\" = [\n" +
                "    \"lobby\"\n" +
                "]\n" +
                "\"factions.example.com\" = [\n" +
                "    \"factions\"\n" +
                "]\n" +
                "\"minigames.example.com\" = [\n" +
                "    \"minigames\"\n" +
                "]\n" +
                "\n" +
                "[advanced]\n" +
                "# How large a Minecraft packet has to be before we compress it. Setting this to zero will\n" +
                "# compress all packets, and setting it to -1 will disable compression entirely.\n" +
                "compression-threshold = 256\n" +
                "\n" +
                "# How much compression should be done (from 0-9). The default is -1, which uses the\n" +
                "# default level of 6.\n" +
                "compression-level = -1\n" +
                "\n" +
                "# How fast (in milliseconds) are clients allowed to connect after the last connection? By\n" +
                "# default, this is three seconds. Disable this by setting this to 0.\n" +
                "login-ratelimit = 3000\n" +
                "\n" +
                "# Specify a custom timeout for connection timeouts here. The default is five seconds.\n" +
                "connection-timeout = 5000\n" +
                "\n" +
                "# Specify a read timeout for connections here. The default is 30 seconds.\n" +
                "read-timeout = 30000\n" +
                "\n" +
                "# Enables compatibility with HAProxy's PROXY protocol. If you don't know what this is for, then\n" +
                "# don't enable it.\n" +
                "haproxy-protocol = "+useProtocol+"\n" +
                "\n" +
                "# Enables TCP fast open support on the proxy. Requires the proxy to run on Linux.\n" +
                "tcp-fast-open = false\n" +
                "\n" +
                "# Enables BungeeCord plugin messaging channel support on Velocity.\n" +
                "bungee-plugin-message-channel = true\n" +
                "\n" +
                "# Shows ping requests to the proxy from clients.\n" +
                "show-ping-requests = false\n" +
                "\n" +
                "# By default, Velocity will attempt to gracefully handle situations where the user unexpectedly\n" +
                "# loses connection to the server without an explicit disconnect message by attempting to fall the\n" +
                "# user back, except in the case of read timeouts. BungeeCord will disconnect the user instead. You\n" +
                "# can disable this setting to use the BungeeCord behavior.\n" +
                "failover-on-unexpected-server-disconnect = true\n" +
                "\n" +
                "# Declares the proxy commands to 1.13+ clients.\n" +
                "announce-proxy-commands = true\n" +
                "\n" +
                "# Enables the logging of commands\n" +
                "log-command-executions = false\n" +
                "\n" +
                "# Enables logging of player connections when connecting to the proxy, switching servers\n" +
                "# and disconnecting from the proxy.\n" +
                "log-player-connections = true\n" +
                "\n" +
                "[query]\n" +
                "# Whether to enable responding to GameSpy 4 query responses or not.\n" +
                "enabled = false\n" +
                "\n" +
                "# If query is enabled, on what port should the query protocol listen on?\n" +
                "port = "+port+"\n" +
                "\n" +
                "# This is the map name that is reported to the query services.\n" +
                "map = \"Velocity\"\n" +
                "\n" +
                "# Whether plugins should be shown in query response by default or not\n" +
                "show-plugins = false\n";
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

    public String  getSoigotYML(){
        return "# This is the main configuration file for Spigot.\n" +
                "# As you can see, there's tons to configure. Some options may impact gameplay, so use\n" +
                "# with caution, and make sure you know what each option does before configuring.\n" +
                "# For a reference for any variable inside this file, check out the Spigot wiki at\n" +
                "# http://www.spigotmc.org/wiki/spigot-configuration/\n" +
                "#\n" +
                "# If you need help with the configuration or have any questions related to Spigot,\n" +
                "# join us at the Discord or drop by our forums and leave a post.\n" +
                "#\n" +
                "# Discord: https://www.spigotmc.org/go/discord\n" +
                "# Forums: http://www.spigotmc.org/\n" +
                "\n" +
                "settings:\n" +
                "  debug: false\n" +
                "  sample-count: 12\n" +
                "  timeout-time: 60\n" +
                "  restart-on-crash: false\n" +
                "  restart-script: ./start.sh\n" +
                "  bungeecord: true\n" +
                "  player-shuffle: 0\n" +
                "  user-cache-size: 1000\n" +
                "  save-user-cache-on-stop-only: false\n" +
                "  moved-wrongly-threshold: 0.0625\n" +
                "  moved-too-quickly-multiplier: 10.0\n" +
                "  netty-threads: 5\n" +
                "  attribute:\n" +
                "    maxHealth:\n" +
                "      max: 2048.0\n" +
                "    movementSpeed:\n" +
                "      max: 2048.0\n" +
                "    attackDamage:\n" +
                "      max: 2048.0\n" +
                "  log-villager-deaths: true\n" +
                "  log-named-deaths: true\n" +
                "messages:\n" +
                "  whitelist: You are not whitelisted on this server!\n" +
                "  unknown-command: Unknown command. Type \"/help\" for help.\n" +
                "  server-full: The server is full!\n" +
                "  outdated-client: Outdated client! Please use {0}\n" +
                "  outdated-server: Outdated server! I'm still on {0}\n" +
                "  restart: Server is restarting\n" +
                "advancements:\n" +
                "  disable-saving: false\n" +
                "  disabled:\n" +
                "  - minecraft:story/disabled\n" +
                "commands:\n" +
                "  spam-exclusions:\n" +
                "  - /skill\n" +
                "  silent-commandblock-console: false\n" +
                "  replace-commands:\n" +
                "  - setblock\n" +
                "  - summon\n" +
                "  - testforblock\n" +
                "  - tellraw\n" +
                "  log: true\n" +
                "  tab-complete: 0\n" +
                "  send-namespaced: true\n" +
                "players:\n" +
                "  disable-saving: false\n" +
                "world-settings:\n" +
                "  default:\n" +
                "    below-zero-generation-in-existing-chunks: true\n" +
                "    hanging-tick-frequency: 100\n" +
                "    merge-radius:\n" +
                "      item: 2.5\n" +
                "      exp: 3.0\n" +
                "    item-despawn-rate: 6000\n" +
                "    view-distance: default\n" +
                "    simulation-distance: default\n" +
                "    thunder-chance: 100000\n" +
                "    enable-zombie-pigmen-portal-spawns: true\n" +
                "    wither-spawn-sound-radius: 0\n" +
                "    arrow-despawn-rate: 1200\n" +
                "    trident-despawn-rate: 1200\n" +
                "    zombie-aggressive-towards-villager: true\n" +
                "    nerf-spawner-mobs: false\n" +
                "    mob-spawn-range: 8\n" +
                "    end-portal-sound-radius: 0\n" +
                "    growth:\n" +
                "      cactus-modifier: 100\n" +
                "      cane-modifier: 100\n" +
                "      melon-modifier: 100\n" +
                "      mushroom-modifier: 100\n" +
                "      pumpkin-modifier: 100\n" +
                "      sapling-modifier: 100\n" +
                "      beetroot-modifier: 100\n" +
                "      carrot-modifier: 100\n" +
                "      potato-modifier: 100\n" +
                "      wheat-modifier: 100\n" +
                "      netherwart-modifier: 100\n" +
                "      vine-modifier: 100\n" +
                "      cocoa-modifier: 100\n" +
                "      bamboo-modifier: 100\n" +
                "      sweetberry-modifier: 100\n" +
                "      kelp-modifier: 100\n" +
                "      twistingvines-modifier: 100\n" +
                "      weepingvines-modifier: 100\n" +
                "      cavevines-modifier: 100\n" +
                "      glowberry-modifier: 100\n" +
                "    entity-activation-range:\n" +
                "      animals: 32\n" +
                "      monsters: 32\n" +
                "      raiders: 48\n" +
                "      misc: 16\n" +
                "      water: 16\n" +
                "      villagers: 32\n" +
                "      flying-monsters: 32\n" +
                "      wake-up-inactive:\n" +
                "        animals-max-per-tick: 4\n" +
                "        animals-every: 1200\n" +
                "        animals-for: 100\n" +
                "        monsters-max-per-tick: 8\n" +
                "        monsters-every: 400\n" +
                "        monsters-for: 100\n" +
                "        villagers-max-per-tick: 4\n" +
                "        villagers-every: 600\n" +
                "        villagers-for: 100\n" +
                "        flying-monsters-max-per-tick: 8\n" +
                "        flying-monsters-every: 200\n" +
                "        flying-monsters-for: 100\n" +
                "      villagers-work-immunity-after: 100\n" +
                "      villagers-work-immunity-for: 20\n" +
                "      villagers-active-for-panic: true\n" +
                "      tick-inactive-villagers: true\n" +
                "      ignore-spectators: false\n" +
                "    entity-tracking-range:\n" +
                "      players: 48\n" +
                "      animals: 48\n" +
                "      monsters: 48\n" +
                "      misc: 32\n" +
                "      other: 64\n" +
                "    ticks-per:\n" +
                "      hopper-transfer: 8\n" +
                "      hopper-check: 1\n" +
                "    hopper-amount: 1\n" +
                "    hopper-can-load-chunks: false\n" +
                "    dragon-death-sound-radius: 0\n" +
                "    seed-village: 10387312\n" +
                "    seed-desert: 14357617\n" +
                "    seed-igloo: 14357618\n" +
                "    seed-jungle: 14357619\n" +
                "    seed-swamp: 14357620\n" +
                "    seed-monument: 10387313\n" +
                "    seed-shipwreck: 165745295\n" +
                "    seed-ocean: 14357621\n" +
                "    seed-outpost: 165745296\n" +
                "    seed-endcity: 10387313\n" +
                "    seed-slime: 987234911\n" +
                "    seed-nether: 30084232\n" +
                "    seed-mansion: 10387319\n" +
                "    seed-fossil: 14357921\n" +
                "    seed-portal: 34222645\n" +
                "    seed-ancientcity: 20083232\n" +
                "    seed-buriedtreasure: 10387320\n" +
                "    seed-mineshaft: default\n" +
                "    seed-stronghold: default\n" +
                "    hunger:\n" +
                "      jump-walk-exhaustion: 0.05\n" +
                "      jump-sprint-exhaustion: 0.2\n" +
                "      combat-exhaustion: 0.1\n" +
                "      regen-exhaustion: 6.0\n" +
                "      swim-multiplier: 0.01\n" +
                "      sprint-multiplier: 0.1\n" +
                "      other-multiplier: 0.0\n" +
                "    max-tnt-per-tick: 100\n" +
                "    max-tick-time:\n" +
                "      tile: 50\n" +
                "      entity: 50\n" +
                "    verbose: false\n" +
                "config-version: 12\n" +
                "stats:\n" +
                "  disable-saving: false\n" +
                "  forced-stats: {}";
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
                "  - metacloud.command.use\n" +
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
                "  proxy_protocol: "+useProtocol+"\n" +
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
                "ip_forward: true\n" +
                "remote_ping_timeout: 5000\n" +
                "prevent_proxy_connections: false\n" +
                "groups:\n" +
                "  RauchigesEtwas:\n" +
                "  - admin\n" +
                "connection_throttle: 4000\n" +
                "stats: ddace828-7313-4d44-a1e2-6736196a8ab5\n" +
                "connection_throttle_limit: 5\n" +
                "log_pings: true\n";
    }

}
