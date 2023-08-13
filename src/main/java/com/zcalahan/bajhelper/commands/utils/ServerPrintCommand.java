package com.zcalahan.bajhelper.commands.utils;

import com.zcalahan.bajhelper.commands.CommandBase;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Objects;

public class ServerPrintCommand extends CommandBase {
    public ServerPrintCommand(String name) {
        this.name = name;
        ephemeral = false;
        commandDescription = "Prints to the server console the emoji.toString() what you entered.";
        botChannelOnly = true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("I got it!").queue();
        System.out.println(Objects.requireNonNull(Emoji.fromFormatted(Objects.requireNonNull(event.getOption("message")).getAsString())));
    }

    @Override
    public SlashCommandData updateCommand() {
        return Commands.slash(name, commandDescription)
                .addOption(OptionType.STRING, "message", "The message to send to the console.");
    }
}
