package me.JordanPlayz158.FasterMusic;

import com.google.gson.Gson;
import me.jordanplayz158.utils.LoadJson;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class Music extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith("-setVoiceChannel") || !Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR))
            return;

        String voiceChatID = event.getMessage().getContentRaw().substring(16);

        try {
            Long.parseLong(voiceChatID);

            Guild guild = event.getGuild();
            File guildFile = new File(guild.getId() + ".json");

            makeConfig(guildFile, voiceChatID);
            guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(LoadJson.value(guildFile, "voiceChannel")));
            PlayMusic();
        } catch (NumberFormatException e) {
            String errorMessage = "Could not convert String to Long, please ensure you put the ID of the voice channel in correctly";

            event.getChannel().sendMessage(errorMessage).queue();
            System.out.println(voiceChatID + " - " + errorMessage);
        }
    }

    public void onGuildJoin(GuildJoinEvent event) {
        ReadyEvent.getGuildsList(event.getJDA());
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        ReadyEvent.getGuildsList(event.getJDA());
    }

    public static void PlayMusic() {
        Collections.shuffle(Main.tracks);
        for(Guild guild : ReadyEvent.getGuildsList()) {
            PlayerManager.getInstance().loadAndPlay(guild, Main.tracks.get(0));
        }
    }

    public static void makeConfig(File file, String value) {
        String json = new Gson().toJson(new VoiceChannel(value));

        // Write JSON file
        try (FileWriter fFile = new FileWriter(file)) {
            fFile.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}