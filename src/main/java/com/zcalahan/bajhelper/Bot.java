package com.zcalahan.bajhelper;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Bot {

    public Bot() {
        //todo implement shards.
    }

    public static void main(String[] arguments) throws Exception {
        // Init JDA
        JDA api = JDABuilder.createDefault(System.getenv("BAJ_TOKEN")).build(); //todo implement environment variable for this token

        //Command Configuration
        api.updateCommands().addCommands(
                Commands.slash("ping", "Pings back when sent.")
                        .addOption(OptionType.BOOLEAN, "ephemeral", "Determines whether or not the message is only seen by the user.")
        ).queue();
        api.addEventListener(new MyListener());
    }
}

