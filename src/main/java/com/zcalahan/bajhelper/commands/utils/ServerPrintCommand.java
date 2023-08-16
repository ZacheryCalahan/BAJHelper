package com.zcalahan.bajhelper.commands.utils;

import com.zcalahan.bajhelper.commands.CommandBase;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** This is a command with no other use than to get the string representation of a Discord Emoji. */
public class ServerPrintCommand extends CommandBase {
    public ServerPrintCommand(String name) {
        super(name);
        ephemeral = false;
        commandDescription = "Prints to the server console the emoji.toString() what you entered.";
        botChannelOnly = true;
    }

    @Override
    public boolean execute(SlashCommandInteractionEvent event) {
        // Check that all options are filled out.
        List<OptionMapping> validator = new ArrayList<>();
        validator.add(event.getOption("message"));

        if (validateOptions(validator)) {
            event.reply("I got it!").queue();
            System.out.println(Emoji.fromFormatted(Objects.requireNonNull(event.getOption("message")).getAsString()));
        } else {
            event.reply("The command you sent was not properly filled out.").setEphemeral(true).queue();
        }

        return false;
    }

    @Override
    public SlashCommandData updateCommand() {
        return Commands.slash(name, commandDescription)
                .addOption(OptionType.STRING, "message", "The message to send to the console.");
    }
}
