package com.zcalahan.bajhelper.commands.utils;

import com.zcalahan.bajhelper.commands.CommandBase;
import com.zcalahan.bajhelper.listeners.RoleSelectListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Objects;

public class AddRoleCommand extends CommandBase {
    public AddRoleCommand(String name) {
        super(name);
        ephemeral = false;
        commandDescription = "Add a role and role selection choice.";
        botChannelOnly = true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String emoji = Objects.requireNonNull(event.getOption("emoji")).getAsString();
        emoji = Emoji.fromFormatted(emoji).toString();
        String rolename = Objects.requireNonNull(event.getOption("rolename")).getAsString();
        RoleSelectListener.roles.put(emoji, rolename);
        RoleSelectListener.saveRoles(Objects.requireNonNull(event.getGuild()));
        event.getGuild().createRole().setName(rolename).complete();
        event.reply("Done.").setEphemeral(ephemeral).queue();
    }

    @Override
    public SlashCommandData updateCommand() {
        return super.updateCommand()
                .addOption(OptionType.STRING, "emoji", "The emoji you wish to use for this role")
                .addOption(OptionType.STRING, "rolename", "The name of the Role you wish to add")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
}
