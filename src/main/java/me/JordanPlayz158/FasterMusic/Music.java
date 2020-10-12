package me.JordanPlayz158.FasterMusic;

import com.google.gson.Gson;
import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class Music extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith("-setVoiceChannel") || !event.getMember().hasPermission(Permission.ADMINISTRATOR))
            return;

        String voiceChatIDString = event.getMessage().getContentRaw().replaceAll("-setVoiceChannel| ", "");

        try {
            long voiceChatID = Long.parseLong(voiceChatIDString);

            Guild guild = event.getGuild();

            makeConfig(guild.getId() + ".json", voiceChatID);
            guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(loadJson.value(guild.getId() + ".json", "voiceChannel")));
            PlayMusic();
        } catch (NumberFormatException e) {
            String errorMessage = "Could not convert String to Long, please ensure you put the ID of the voice channel in correctly";

            event.getChannel().sendMessage(errorMessage).queue();
            System.out.println(voiceChatIDString + " - " + errorMessage);
        }
        }

    public static void PlayMusic() {
        Collections.shuffle(Main.tracks);
        for(Guild guild : ReadyEvent.guilds) {
            PlayerManager.getInstance().loadAndPlay(guild, Main.tracks.get(0));
        }
    }

    public static void makeConfig(String file, Long value) {
        String json = new Gson().toJson(new VoiceChannel(value));

        // Write JSON file
        try (FileWriter Jfile = new FileWriter(file)) {
            Jfile.write(json);
            Jfile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}