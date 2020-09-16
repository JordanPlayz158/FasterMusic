package me.JordanPlayz158.FasterMusic;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static me.JordanPlayz158.Utils.copyFile.copyFile;
import static me.JordanPlayz158.Utils.initiateLog.initiateLog;
import static me.JordanPlayz158.Utils.loadConfig.loadConfig;

public class Main {
    public static void main(String[] args) throws IOException, LoginException, InterruptedException {
        // Initiates the log
        initiateLog();

        // Copy config
        copyFile("config.json", "config.json");

        String token = loadConfig("config.json", "token");

        // Checks if the Token is 1 character or less and if so, tell the person they need to provide a token
        if(token.length() <= 1) {
            System.out.println("You have to provide a token in your config file!");
            System.exit(1);
        }

        // Token is read from the config.json
        JDA jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(new Music())
                .build()
                .awaitReady();

        Guild guild = null;

        Object[] Guilds = jda.getGuilds().toArray();

        for(Object s : Guilds) {
            guild = (Guild) s;
            System.out.println(guild.getId());
            if(Files.exists(Path.of(guild.getId() + ".json"))) {
                guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(loadConfig(guild.getId() + ".json", "voiceChannel")));
            }
        }

        //Implement LavaPlayer here......
    }
}