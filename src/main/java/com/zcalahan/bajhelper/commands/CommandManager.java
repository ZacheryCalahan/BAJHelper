package com.zcalahan.bajhelper.commands;

import com.zcalahan.bajhelper.BotConfiguration;
import com.zcalahan.bajhelper.commands.voicehubcommands.SetUserlimitCommand;
import com.zcalahan.bajhelper.commands.voicehubcommands.RenameChannelCommand;
import com.zcalahan.bajhelper.commands.voicehubcommands.SetWhitelistCommand;
import com.zcalahan.bajhelper.commands.voicehubcommands.WhitelistCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/** This class oversees each individual command, and streamlines the command addition process and the execution process. */

public class CommandManager extends ListenerAdapter {
    HashMap<String, CommandBase> commands = new HashMap<String, CommandBase>();
    JDA api;


    /** The function of this constructor is to create each new command. Each command is a seperate class, which overrides the abstract class CommandBase. */
    public CommandManager(JDA api) {
        this.api = api;

        // General Commands
        commands.put("ping", new PingCommand("ping"));
        //commands.put("print", new ServerPrintCommand("print"));

        // Hub Commands
        commands.put("set-whitelist", new SetWhitelistCommand("set-whitelist"));
        commands.put("whitelist", new WhitelistCommand("whitelist"));
        commands.put("rename-channel", new RenameChannelCommand("rename-channel"));
        commands.put("set-userlimit", new SetUserlimitCommand("set-userlimit"));
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
