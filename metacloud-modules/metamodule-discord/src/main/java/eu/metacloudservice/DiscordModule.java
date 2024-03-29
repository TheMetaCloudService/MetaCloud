package eu.metacloudservice;

import eu.metacloudservice.config.ActivityConfiguration;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.CloudEvents;
import eu.metacloudservice.module.extention.IModule;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;

public class DiscordModule implements IModule {

    @Getter
    private static JDA jda;
    @Getter
    private static Configuration configuration;

    @Override
    public void load() {
        if (!new File("./modules/discord/config.json").exists()) {
            new File("./modules/discord/").mkdirs();
            Configuration configuration = new Configuration();
            configuration.setDiscordToken("");

            ActivityConfiguration activityConfiguration = new ActivityConfiguration();
            activityConfiguration.setDiscordActivity("Download me ► https://metacloudservice.eu");
            activityConfiguration.setActivity(eu.metacloudservice.config.Activity.custom);
            configuration.setActivity(activityConfiguration);
            configuration.setLogo("https://i.ibb.co/VBZTQ6F/metacloud.png");
            configuration.setChannelID(0);
            configuration.setFooter("► metacloudservice • Ready for the Future");
            configuration.setDiscordGuild("");

            new ConfigDriver("./modules/discord/config.json").save(configuration);
        }

        configuration = (Configuration) new ConfigDriver("./modules/discord/config.json").read(Configuration.class);
            initialisationDiscordBotHook();
    }

    @Override
    public void unload() {
        if (!new File("./modules/discord/config.json").exists()) {
            new File("./modules/discord/").mkdirs();
            Configuration configuration = new Configuration();
            configuration.setDiscordToken("");

            ActivityConfiguration activityConfiguration = new ActivityConfiguration();
            activityConfiguration.setDiscordActivity("Download me ► https://metacloudservice.eu");
            activityConfiguration.setActivity(eu.metacloudservice.config.Activity.custom);
            configuration.setActivity(activityConfiguration);
            configuration.setLogo("https://i.ibb.co/VBZTQ6F/metacloud.png");
            configuration.setChannelID(0);
            configuration.setFooter("► metacloudservice • Ready for the Future");
            configuration.setDiscordGuild("");
            new ConfigDriver("./modules/discord/config.json").save(configuration);
        }
    }

    @Override
    public void reload() {
        if (!new File("./modules/discord/config.json").exists()) {
            new File("./modules/discord/").mkdirs();
            Configuration configuration = new Configuration();
            configuration.setDiscordToken("");

            ActivityConfiguration activityConfiguration = new ActivityConfiguration();
            activityConfiguration.setDiscordActivity("Download me ► https://metacloudservice.eu");
            activityConfiguration.setActivity(eu.metacloudservice.config.Activity.custom);
            configuration.setActivity(activityConfiguration);
            configuration.setLogo("https://i.ibb.co/VBZTQ6F/metacloud.png");
            configuration.setChannelID(0);
            configuration.setFooter("► metacloudservice • Ready for the Future");
            configuration.setDiscordGuild("");


            new ConfigDriver("./modules/discord/config.json").save(configuration);
        }
        configuration = (Configuration) new ConfigDriver("./modules/discord/config.json").read(Configuration.class);
        initialisationDiscordBotHook();
    }


    private void initialisationDiscordBotHook(){
        if (jda != null) return;
        if (configuration == null) return;
        if (configuration.getDiscordToken().equalsIgnoreCase("")) return;
        jda = JDABuilder
                .createDefault(configuration.getDiscordToken())
                .setAutoReconnect(true)
                .build();


        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new CloudEvents());
    }
}
