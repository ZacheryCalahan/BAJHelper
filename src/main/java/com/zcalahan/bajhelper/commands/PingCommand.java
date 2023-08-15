package com.zcalahan.bajhelper.commands;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * This is an example command that uses the default CommandBase implementation to demonstrate it works.
 */
public class PingCommand extends CommandBase {

    public PingCommand(String name) {
        super(name);
        ephemeral = true;
        commandDescription = "Tells the bot to play Ping Pong!";
        botChannelOnly = true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(ephemeral).queue();
        event.getHook().sendMessage("Pong!").queue();
    }
}
