package com.zcalahan.bajhelper;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class MyListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            boolean tagEph = Objects.requireNonNull(event.getOption("ephemeral")).getAsBoolean();
            event.deferReply().setEphemeral(tagEph).queue(); // Tell discord we got the command. Tell user we're thinking.
            event.getHook().sendMessage("Pong!").queue();
        }
    }
}
