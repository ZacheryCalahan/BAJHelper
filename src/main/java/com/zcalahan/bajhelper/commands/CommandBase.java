package com.zcalahan.bajhelper.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import java.util.List;

/**
 * Abstract class representing what each command requires to be used.
 */
public abstract class CommandBase extends ListenerAdapter {
    /** Name of the command. */
    protected String name;

    /** A breif description of the command. */
    protected String commandDescription;

    /** Determines if the resulting reply is visible to solely the user. */
    protected boolean ephemeral;

    /** Determines whether or not the command is locked to a #bot-commands channel */
    protected boolean botChannelOnly;

    /** Initializes necessary fields required to sucessfully create a command. */
    public CommandBase(String name) {
        this.name = name;
        ephemeral = false;
        commandDescription = "Default command with no implementation";
        botChannelOnly = true;
    }

    /**
     * The code that is run when the command is called.
     */
    public boolean execute(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(ephemeral).queue();
        event.getHook().sendMessage("This command has no implementation.").queue();
        return false;
    }

    /** Determines how the command is used by Discord */
    public SlashCommandData updateCommand() {
        return Commands.slash(name, commandDescription);
    }

    /** Checks to make sure options aren't null on call of execute(). */

    public boolean validateOptions(List<OptionMapping> listOfOptions) {
        for (OptionMapping map : listOfOptions) {
            if (map == null) {
                return false;
            }
        }
        return true;
    }
}
