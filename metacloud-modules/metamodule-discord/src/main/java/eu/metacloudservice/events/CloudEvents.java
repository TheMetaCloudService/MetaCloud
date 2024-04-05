package eu.metacloudservice.events;

import eu.metacloudservice.DiscordModule;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.group.CloudGroupCreateEvent;
import eu.metacloudservice.events.listeners.group.CloudGroupDeleteEvent;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.events.listeners.player.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.events.listeners.services.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class CloudEvents implements ICloudListener {


    @Subscribe
    public void handel(CloudPlayerConnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0x28de37)
                .setTitle("◣ CONNECT ○ CLOUD-PLAYER ◥")
                .setDescription("\n\n\n\nName: "+event.getName()+"\nUUID: "+event.getUniqueId()+"\nOn proxy: "+event.getProxy()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById( DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Subscribe
    public void handel(CloudPlayerDisconnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ DISCONNECT ○ CLOUD-PLAYER ◥")
                .setDescription("\n\n\n\nName: "+event.getName()+"\nUUID: "+event.getUniqueId()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Subscribe
    public void handel(CloudProxyConnectedEvent event) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0x28de37)
                .setTitle("◣ CONNECT ○ CLOUD-PROXY ◥")
                .setDescription("\n\n\n\nService: " + event.getName() + "\nUsed Node: " + event.getNode() + "\nUsed Group: " + event.getGroup() + "\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")) {
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        } else {
            Guild guild = DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel = guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Subscribe
    public void handel(CloudServiceChangeStateEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ CHANGE-STATE ○ CLOUD-SERVICE ◥")
                .setDescription("\n\n\n\ngroup: "+event.getName()+"\nstate: "+event.getState()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Subscribe
    public void handel(CloudGroupDeleteEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ DELETE ○ CLOUD-GROUP ◥")
                .setDescription("\n\n\n\ngroup: "+event.getGroupname()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Subscribe
    public void handel(CloudGroupCreateEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ CREATE ○ CLOUD-GROUP ◥")
                .setDescription("\n\n\n\ngroup: "+event.getGroupname()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Subscribe
    public void handel(CloudProxyDisconnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ DISCONNECT ○ CLOUD-PROXY ◥")
                .setDescription("\n\n\n\nService: "+event.getName()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }

    }

    @Subscribe
    public void handel(CloudServiceConnectedEvent event) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0x28de37)
                .setTitle("◣ CONNECT ○ CLOUD-SERVICE ◥")
                .setDescription("\n\n\n\nService: "+event.getName()+"\nUsed Node: "+event.getNode()+"\nUsed Group: "+event.getGroup()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Subscribe
    public void handel(CloudServiceDisconnectedEvent event){
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xdb2727)
                .setTitle("◣ DISCONNECT ○ CLOUD-SERVICE  ◥")
                .setDescription("\n\n\n\nService: "+event.getName()+"\n\n\n\n")
                .setFooter(DiscordModule.getConfiguration().getFooter(), DiscordModule.getConfiguration().getLogo());
        if (DiscordModule.getConfiguration().getDiscordGuild().equalsIgnoreCase("")){
            TextChannel channel = DiscordModule.getJda().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }else {
            Guild guild =  DiscordModule.getJda().getGuildById(DiscordModule.getConfiguration().getDiscordGuild());
            TextChannel channel =guild.getJDA().getTextChannelById(DiscordModule.getConfiguration().getChannelID());
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

}
