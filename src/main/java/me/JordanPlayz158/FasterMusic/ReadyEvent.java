package me.JordanPlayz158.FasterMusic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static me.JordanPlayz158.Utils.loadConfig.loadConfig;

public class ReadyEvent extends ListenerAdapter {
    public static List<Guild> guilds = new ArrayList<>();

    public void onReady(net.dv8tion.jda.api.events.ReadyEvent event) {
        event.getJDA().getGuilds().forEach(guild -> {
            System.out.println(guild.getId());

            guilds.add(guild);

            if(Files.exists(Path.of(guild.getId() + ".json"))) {
                guild.getAudioManager().openAudioConnection(
                        guild.getVoiceChannelById(loadConfig(guild.getId() + ".json", "voiceChannel")));
                Music.PlayMusic();
            }

        });
    }
}
