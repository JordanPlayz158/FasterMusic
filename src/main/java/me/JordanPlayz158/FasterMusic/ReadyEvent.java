package me.JordanPlayz158.FasterMusic;

import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ReadyEvent extends ListenerAdapter {
    public static List<Guild> guilds = new ArrayList<>();

    public void onReady(net.dv8tion.jda.api.events.ReadyEvent event) {
        event.getJDA().getGuilds().forEach(guild -> {
            System.out.println(guild.getName() + " - " + guild.getId());

            guilds.add(guild);

            if(Files.exists(Path.of(guild.getId() + ".json"))) {
                guild.getAudioManager().openAudioConnection(
                        guild.getVoiceChannelById(loadJson.value(guild.getId() + ".json", "voiceChannel")));
                Music.PlayMusic();
            }

        });
    }
}
