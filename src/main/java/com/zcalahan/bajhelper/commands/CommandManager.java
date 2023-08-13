package com.zcalahan.bajhelper.commands;

import com.zcalahan.bajhelper.BotConfiguration;
import com.zcalahan.bajhelper.commands.utils.ServerPrintCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/** This class links the commands and discord in an orderly fashion. */

public class CommandManager extends ListenerAdapter {
    HashMap<String, CommandBase> commands = new HashMap<String, CommandBase>();
    JDA api;


    /** The function of this constructor is to create each new command. Each command is a seperate class, which overrides the abstract class CommandBase. */
    public CommandManager(JDA api) {
        this.api = api;

        commands.put("ping", new PingCommand("ping"));    // This is a test command. Comment out when not in use.
        commands.put("print", new ServerPrintCommand("print"));
        uploadCommands();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (commands.containsKey(event.getName())) {
            CommandBase command = commands.get(event.getName());

            if (command.botChannelOnly && !event.getChannel().getName().equals(BotConfiguration.botCommandChannel)) {
                event.deferReply().setEphemeral(true).queue();
                event.getHook().sendMessage("You can't use this command in this channel. Please use the " + BotConfiguration.botCommandChannel + " channel.").queue();
                return;
            }

            command.execute(event);
        }
    }

    public void uploadCommands() {
        //List<Command> currentCommandList = api.retrieveCommands().complete(); // Asks bot for command list
        List<CommandData> slashCommands = new ArrayList<>();
        List<String> commandKeys = new ArrayList<>(commands.keySet());
        for (String key:commandKeys) {
            slashCommands.add(commands.get(key).updateCommand());
        }

        api.updateCommands().addCommands(slashCommands).queue();
    }


}
