package me.JordanPlayz158.FasterMusic;

import me.JordanPlayz158.Utils.loadJson;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.JordanPlayz158.Utils.copyFile.copyFile;
import static me.JordanPlayz158.Utils.initiateLog.initiateLog;

public class Main {
	public static List<String> tracks = new ArrayList<>();

    static {
        try {
            copyFile("songs.txt", "songs.txt");
            File file = new File("songs.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String string;

            while((string = br.readLine()) != null) {
                tracks.add(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, LoginException, InterruptedException {
        // Initiates the log
        initiateLog();

        // Copy config
        copyFile("config.json", "config.json");

        String token = loadJson.value("config.json", "token");

        // Checks if the Token is 1 character or less and if so, tell the person they need to provide a token
        if(token.length() <= 1) {
            System.out.println("You have to provide a token in your config file!");
            System.exit(1);
        }
        
        JDABuilder.createLight(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(new Music())
                .addEventListeners(new ReadyEvent())
                .setActivity(Activity.streaming("24/7 Faster Stream", "https://www.youtube.com/watch?v=nOOHjGIsoPg"))
                .build()
                .awaitReady();
    }
}