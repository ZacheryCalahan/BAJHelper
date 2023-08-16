package com.zcalahan.bajhelper;

import com.zcalahan.bajhelper.commands.CommandManager;
import com.zcalahan.bajhelper.listeners.ModalListener;
import com.zcalahan.bajhelper.listeners.RoleSelectListener;
import com.zcalahan.bajhelper.listeners.VoiceHubListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

/** Main class of Bot */
public class Bot {

    public Bot() {
        //todo implement shards.
    }
    /** Entrypoint of Bot */
    public static void main(String[] arguments) {
        // Init JDA
        JDA api = JDABuilder.createDefault(System.getenv("BAJ_TOKEN")).enableIntents(
                GatewayIntent.GUILD_MESSAGE_REACTIONS // Allows bot to see message reactions.
        ).build();

        api.getPresence().setStatus(OnlineStatus.ONLINE);
        api.getPresence().setActivity(Activity.playing("with ones and zeros"));

        // Enable command manager
        api.addEventListener(new CommandManager(api));
        api.addEventListener(new RoleSelectListener());
        api.addEventListener(new VoiceHubListener());
        api.addEventListener(new ModalListener());
    }
}

