package eu.metacloudservice.commands.translate;

public class Translator {

    public Translator() {}

    public String translate(String raw){
        return raw
                .replaceAll("§1", "<dark_blue>")
                .replaceAll("&1", "<dark_blue>")
                .replaceAll("§2", "<dark_green>")
                .replaceAll("&2", "<dark_green>")
                .replaceAll("§3", "<dark_aqua>")
                .replaceAll("&3", "<dark_aqua>")
                .replaceAll("§4", "<dark_red>")
                .replaceAll("&4", "<dark_red>")
                .replaceAll("§5", "<dark_purple>")
                .replaceAll("&5", "<dark_purple>")
                .replaceAll("§6", "<gold>")
                .replaceAll("&6", "<gold>")
                .replaceAll("§7", "<gray>")
                .replaceAll("&7", "<gray>")
                .replaceAll("§8", "<dark_gray>")
                .replaceAll("&8", "<dark_gray>")
                .replaceAll("§9", "<blue>")
                .replaceAll("&9", "<blue>")
                .replaceAll("§a", "<green>")
                .replaceAll("&a", "<green>")
                .replaceAll("§b", "<aqua>")
                .replaceAll("&b", "<aqua>")
                .replaceAll("§c", "<red>")
                .replaceAll("&c", "<red>")
                .replaceAll("§d", "<light_purple>")
                .replaceAll("&d", "<light_purple>")
                .replaceAll("§e", "<yellow>")
                .replaceAll("&e", "<yellow>")
                .replaceAll("§f", "<white>")
                .replaceAll("&f", "<white>")
                .replaceAll("§l", "<bold>")
                .replaceAll("&l", "<bold>")
                .replaceAll("§r", "<reset>")
                .replaceAll("&r", "<reset>")
                .replaceAll("§o", "<italic>")
                .replaceAll("&o", "<italic>")
                .replaceAll("§n", "<underlined>")
                .replaceAll("&n", "<underlined>")
                .replaceAll("§m", "<strikethrough>")
                .replaceAll("&m", "<strikethrough>")
                .replaceAll("§k", "<obfuscated>")
                .replaceAll("&k", "<obfuscated>")
                .replaceAll("§A", "<green>")
                .replaceAll("&A", "<green>")
                .replaceAll("§B", "<aqua>")
                .replaceAll("&B", "<aqua>")
                .replaceAll("§C", "<red>")
                .replaceAll("&C", "<red>")
                .replaceAll("§D", "<light_purple>")
                .replaceAll("&D", "<light_purple>")
                .replaceAll("§E", "<yellow>")
                .replaceAll("&E", "<yellow>")
                .replaceAll("§F", "<white>")
                .replaceAll("&F", "<white>")
                .replaceAll("§L", "<bold>")
                .replaceAll("&L", "<bold>")
                .replaceAll("§R", "<reset>")
                .replaceAll("&R", "<reset>")
                .replaceAll("§O", "<italic>")
                .replaceAll("&O", "<italic>")
                .replaceAll("§N", "<underlined>")
                .replaceAll("&N", "<underlined>")
                .replaceAll("§M", "<strikethrough>")
                .replaceAll("&M", "<strikethrough>")
                .replaceAll("§K", "<obfuscated>")
                .replaceAll("&K", "<obfuscated>");
    }
}
