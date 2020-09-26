package me.JordanPlayz158.FasterMusic;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import static me.JordanPlayz158.Utils.loadConfig.loadConfig;

public class Music extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith("-setVoiceChannel"))
            return;

        if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            String voiceChatIDString = event.getMessage().getContentRaw().replaceAll("-setVoiceChannel| ", "");

            try {
                long voiceChatID = Long.parseLong(voiceChatIDString);

                Guild guild = event.getGuild();

                makeConfig(guild.getId() + ".json", "voiceChannel", String.valueOf(voiceChatID));
                guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(loadConfig(guild.getId() + ".json", "voiceChannel")));
            } catch (NumberFormatException e) {
                String errorMessage = "Could not convert String to BigInteger, please ensure you put the ID of the voice channel in correctly";

                event.getChannel().sendMessage(errorMessage).queue();
                System.out.println(errorMessage);
            }

            PlayMusic();
            }
        }

    public static void PlayMusic() {
        Collections.shuffle(Main.tracks);
        for(Guild guild : ReadyEvent.guilds) {
            PlayerManager.getInstance().loadAndPlay(guild, Main.tracks.get(0));
        }
    }


    // Function I have made for other projects that I put into this one for simple json file/data appending
    @SuppressWarnings("unchecked")
    public static void makeConfig(String file, String key, String value) {
        // Making the JSONObject and putting the key and value into it
        JSONObject append = new JSONObject();
        append.put(key, value);

        // Write JSON file
        try (FileWriter Jfile = new FileWriter(file)) {
            Jfile.write(append.toJSONString());
            Jfile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}