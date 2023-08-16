package com.zcalahan.bajhelper.commands.voicehubcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.*;

/** Command that sets a Hub Channel to a whitelist only state. */
public class SetWhitelistCommand extends HubCommandBase {

    public SetWhitelistCommand(String name) {
        super(name);
        ephemeral = false;
        commandDescription = "Determines whether your voice channel is set to whitelist only";
        botChannelOnly = false;
}

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Check that all options are filled out.
        List<OptionMapping> validator = new ArrayList<>();
        validator.add(event.getOption("iswhitelist"));

        if (validateOptions(validator)) {
            event.deferReply().setEphemeral(ephemeral).queue();
            Map<VoiceChannel, TextChannel> channels = voiceAdmins.get(event.getMember());
            // Get VoiceChannel (yes this code sucks. it's what I got.
            VoiceChannel voiceChannel = null;
            for (VoiceChannel channel : channels.keySet()) {
                voiceChannel = channel;
                break;
            }

            // Check that channel belongs to user, and is in the text chat.
            if (voiceAdmins.containsKey(event.getMember()) && event.getChannel().equals(channels.get(voiceChannel))) {
                if (Objects.requireNonNull(event.getOption("iswhitelist")).getAsBoolean()) {
                    // Run if set to whitelist only
                    assert voiceChannel != null;
                    voiceChannel.getManager().putPermissionOverride(Objects.requireNonNull(event.getGuild()).getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL)).queue();
                    event.getHook().sendMessage("Set to whitelist only.").queue();
                } else {
                    assert voiceChannel != null;
                    voiceChannel.getManager().putPermissionOverride(Objects.requireNonNull(event.getGuild()).getPublicRole(), EnumSet.of(Permission.VIEW_CHANNEL), null).queue();
                    event.getHook().sendMessage("Set to public.").queue();
                }
            } else {
                event.getHook().sendMessage("You do not have permission to use this.").queue();
            }
        } else {
            event.reply("The command you sent was not properly filled out.").setEphemeral(true).queue();
        }
    }

    @Override
    public SlashCommandData updateCommand() {
        return Commands.slash(name, commandDescription)
                .addOption(OptionType.BOOLEAN, "iswhitelist", "Set channel to whitelist only.");
    }
}
