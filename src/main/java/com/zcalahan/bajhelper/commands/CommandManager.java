package com.zcalahan.bajhelper.commands;

import com.zcalahan.bajhelper.BotConfiguration;
import com.zcalahan.bajhelper.commands.utils.NewGuildCommand;
import com.zcalahan.bajhelper.commands.utils.ServerPrintCommand;
import com.zcalahan.bajhelper.commands.voicehubcommands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/** This class oversees each individual command, and streamlines the command addition process and the execution process. */

public class CommandManager extends ListenerAdapter {
    HashMap<String, CommandBase> commands = new HashMap<>();
    HashMap<String, CommandBase> guildCommands = new HashMap<>();
    JDA api;


    /** The function of this constructor is to create each new command. Each command is a seperate class, which overrides the abstract class CommandBase. */
    public CommandManager(JDA api) {
        this.api = api;

        // General Commands
        commands.put("ping", new PingCommand("ping"));
        commands.put("print", new ServerPrintCommand("print"));
        commands.put("report-user", new ReportCommand("report-user"));

        // Hub Commands
        commands.put("set-whitelist", new SetWhitelistCommand("set-whitelist"));
        commands.put("whitelist", new WhitelistCommand("whitelist"));
        commands.put("rename-channel", new RenameChannelCommand("rename-channel"));
        commands.put("set-userlimit", new SetUserlimitCommand("set-userlimit"));
        uploadCommands();

        // Guild Commands
        guildCommands.put("init-channels", new NewGuildCommand("init-channels"));
    }

    /** The function of this event listener is to add guild commands on join. */
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        uploadGuildCommand(event.getGuild());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        System.out.println(event.getName());
        if (commands.containsKey(event.getName())) {
            CommandBase command = commands.get(event.getName());

            if (command.botChannelOnly && !event.getChannel().getName().equals(BotConfiguration.botCommandChannel)) {
                event.deferReply().setEphemeral(true).queue();
                event.getHook().sendMessage("You can't use this command in this channel. Please use the " + BotConfiguration.botCommandChannel + " channel.").queue();
                return;
            }

            command.execute(event);
        }

        if (guildCommands.containsKey(event.getName())) {
            CommandBase command = guildCommands.get(event.getName());

            if (command.botChannelOnly && !event.getChannel().getName().equals(BotConfiguration.botCommandChannel)) {
                event.deferReply().setEphemeral(true).queue();
                event.getHook().sendMessage("You can't use this command in this channel. Please use the " + BotConfiguration.botCommandChannel + " channel.").queue();
                return;
            }
            System.out.println(event.getName() + " has been run.");
            command.execute(event);
        }
    }

    /** Called only when global commands are added */
    public void uploadCommands() {
        List<CommandData> slashCommands = new ArrayList<>();
        List<String> commandKeys = new ArrayList<>(commands.keySet());
        for (String key:commandKeys) {
            slashCommands.add(commands.get(key).updateCommand());
        }

        api.updateCommands().addCommands(slashCommands).queue();
    }

    /** Called only when a guild specific command is added */
    public void uploadGuildCommand(Guild guild) {
        List<CommandData> slashCommands = new ArrayList<>();
        List<String> commandKeys = new ArrayList<>(guildCommands.keySet());
        for (String key:commandKeys) {
            slashCommands.add(guildCommands.get(key).updateCommand());
        }

        guild.updateCommands().addCommands(slashCommands).queue();
    }


}
