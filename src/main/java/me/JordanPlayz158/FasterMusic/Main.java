package me.JordanPlayz158.FasterMusic;

import static me.JordanPlayz158.Utils.copyFile.copyFile;
import static me.JordanPlayz158.Utils.initiateLog.initiateLog;
import static me.JordanPlayz158.Utils.loadConfig.loadConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
	
	public static JDA jda;
	
	public static List<String> tracks = Arrays.asList(new String[] 
			{"https://www.youtube.com/watch?v=zbYFX4bxKyg", 
					"https://www.youtube.com/watch?v=lGeTySE9ZAc", 
					"https://www.youtube.com/watch?v=y1NEl1BWgm0", 
					"https://www.youtube.com/watch?v=i3Ck2f9CWYs"
					});
	
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
        
        JDABuilder builder = JDABuilder.createDefault(token);
        
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES);
        builder.addEventListeners(new Music());
        
        builder.setActivity(Activity.streaming("24/7 Faster Stream", "https://www.youtube.com/channel/UC-SK9mJ7TCw_zjdi5AQJoEQ"));

        jda = builder.build();
        
        jda.awaitReady();
        
        jda.getGuilds().forEach(guild -> {
        	System.out.println(guild.getId());
        	
            if(Files.exists(Path.of(guild.getId() + ".json"))) {
                guild.getAudioManager().openAudioConnection(
                		guild.getVoiceChannelById(loadConfig(guild.getId() + ".json", "voiceChannel")));
            }
        });
    }
}