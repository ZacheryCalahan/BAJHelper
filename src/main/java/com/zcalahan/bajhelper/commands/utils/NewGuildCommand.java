package com.zcalahan.bajhelper.commands.utils;

import com.zcalahan.bajhelper.BotConfiguration;
import com.zcalahan.bajhelper.commands.CommandBase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import java.util.EnumSet;
import java.util.List;

/** This is a command meant to be solely run at the start of any discord guild. This command will automatically create everything you need to use the bot
 *  On run, it will create the following channels, #role-select, #user-reports. #bot-commands, #Hub (Join for Voice Channel)
 *  and a category called Hub Channels
 * */
public class NewGuildCommand extends CommandBase {

    public NewGuildCommand(String name) {
        super(name);
        ephemeral = true;
        commandDescription = "Run this command to get things started. Deletes command when run";
        botChannelOnly = false;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

            event.reply("Starting the configuration!").setEphemeral(true).queue();
            Guild guild = event.getGuild();

            // Create and configure channels

            // Role Select Channel
        assert guild != null;
        if (guild.getTextChannelsByName(BotConfiguration.roleReactChannel, true).isEmpty()) {
                TextChannel roleChannel = guild.createTextChannel(BotConfiguration.roleReactChannel).complete();
                roleChannel.getManager()
                        .putPermissionOverride(guild.getPublicRole(), EnumSet.of(Permission.VIEW_CHANNEL), EnumSet.of(Permission.MESSAGE_SEND))
                        .queue();
        }

        // User Reports Channel
        Role adminRole;
        if (guild.getRolesByName("admin", true).isEmpty()) {
            // Set admin role
            adminRole = guild.createRole().setName("Admin").complete();
        } else {
            // Find admin role
            adminRole = guild.getRolesByName("admin", true).get(0);
        }

        if (guild.getTextChannelsByName(BotConfiguration.reportsChannel, true).isEmpty()) {
            TextChannel reportsChannel = guild.createTextChannel(BotConfiguration.reportsChannel).complete();
            reportsChannel.getManager()
                    .putPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .putPermissionOverride(adminRole, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .queue();
        }

        // Bot Commands Channel
        if (guild.getTextChannelsByName(BotConfiguration.botCommandChannel, true).isEmpty()) {
            TextChannel roleChannel = guild.createTextChannel(BotConfiguration.botCommandChannel).complete();
            roleChannel.getManager().queue();
        }

        // Voice Hub Channel & Category
        Category voiceHubCategory;
        if (guild.getCategoriesByName(BotConfiguration.voiceHubCategory, true).isEmpty()) {
            voiceHubCategory = guild.createCategory(BotConfiguration.voiceHubCategory).complete();
        } else {
            voiceHubCategory = guild.getCategoriesByName(BotConfiguration.voiceHubCategory, true).get(0);
        }

        if (guild.getVoiceChannelsByName(BotConfiguration.voiceHubChannel, true).isEmpty()) {
            guild.createVoiceChannel(BotConfiguration.voiceHubChannel, voiceHubCategory).queue();
        }

        // Delete command after use
        List<Command> commandList = guild.retrieveCommands().complete();
        for (Command command : commandList) {
            if (command.getName().equals(name)) {
                guild.deleteCommandById(command.getIdLong()).queue();
            }
        }
    }

    @Override
    public SlashCommandData updateCommand() {
        return super.updateCommand();
    }
}
