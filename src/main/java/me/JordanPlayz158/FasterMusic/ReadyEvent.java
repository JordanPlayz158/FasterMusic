package me.JordanPlayz158.FasterMusic;

import me.jordanplayz158.utils.LoadJson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ReadyEvent extends ListenerAdapter {
    private static List<Guild> guilds = new ArrayList<>();

    public void onReady(net.dv8tion.jda.api.events.ReadyEvent event) {
        getGuilds(event.getJDA());
    }

    public void getGuilds(JDA jda) {
        jda.getGuilds().forEach(guild -> {
            System.out.println(guild.getName() + " - " + guild.getId());

            guilds.add(guild);

            File guildFile = new File(guild.getId() + ".json");

            if(Files.exists(guildFile.toPath())) {
                guild.getAudioManager().openAudioConnection(
                        guild.getVoiceChannelById(LoadJson.value(guildFile, "voiceChannel")));
                Music.PlayMusic();
            }

        });
    }

    public static void getGuildsList(JDA jda) {
        guilds = jda.getGuilds();
    }

    public static List<Guild> getGuildsList() {
        return guilds;
    }
}
