package eu.metacloudservice.events;

import eu.metacloudservice.DiscordModule;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.events.listeners.player.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudProxyConnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudProxyDisconnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceConnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceDisconnectedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class CloudEvents implements ICloudListener {

    @Subscribe
    public void handel(CloudPlayerConnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0x28de37)
                .setTitle("◣ UPDATE ○ CLOUDPLAYER ◥")
                .setDescription("\n\n\n\nName: "+event.getName()+"\nUUID: "+event.getUniqueId()+"\nOn proxy: "+event.getProxy()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Subscribe
    public void handel(CloudPlayerDisconnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ UPDATE ○ CLOUDPLAYER ◥")
                .setDescription("\n\n\n\nName: "+event.getName()+"\nUUID: "+event.getUniqueId()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Subscribe
    public void handel(CloudProxyConnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0x28de37)
                .setTitle("◣ UPDATE ○ CLOUDSERVICE ◥")
                .setDescription("\n\n\n\nService: "+event.getName()+"\nUsed Node: "+event.getNode()+"\nUsed Group: "+event.getGroup()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Subscribe
    public void handel(CloudProxyDisconnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ UPDATE ○ CLOUDSERVICE ◥")
                .setDescription("\n\n\n\nService: "+event.getName()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
        channel.sendMessageEmbeds(builder.build()).queue();

    }

    @Subscribe
    public void handel(CloudServiceConnectedEvent event) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0x28de37)
                .setTitle("◣ UPDATE ○ CLOUDSERVICE ◥")
                .setDescription("\n\n\n\nService: "+event.getName()+"\nUsed Node: "+event.getNode()+"\nUsed Group: "+event.getGroup()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Subscribe
    public void handel(CloudServiceDisconnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ UPDATE ○ CLOUDSERVICE ◥")
                .setDescription("\n\n\n\nService: "+event.getName()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
        channel.sendMessageEmbeds(builder.build()).queue();
    }


}
