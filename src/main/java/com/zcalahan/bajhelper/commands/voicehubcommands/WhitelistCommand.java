package com.zcalahan.bajhelper.commands.voicehubcommands;

import com.zcalahan.bajhelper.listeners.VoiceHubListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.*;

/** Command that allows/disallows users to interact with a Hub Channel */
public class WhitelistCommand extends HubCommandBase {
    Map<Member, Map<VoiceChannel, TextChannel>> voiceAdmins = VoiceHubListener.voiceAdmin;
    public WhitelistCommand(String name) {
        super(name);
        commandDescription = "Command to add/remove a user from your whitelist.";
    }

    @Override
    public boolean execute(SlashCommandInteractionEvent event) {
        // Checks that there are channels to edit. Prevents exception thrown when run out of context.
        if (!super.execute(event)) return false;

        // Check that all options are filled out.
        List<OptionMapping> validator = new ArrayList<>();
        validator.add(event.getOption("isadded"));
        validator.add(event.getOption("username"));


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
                // Get Options
                Member member = Objects.requireNonNull(event.getOption("username")).getAsMember();


                if (Objects.requireNonNull(event.getOption("isadded")).getAsBoolean()) {
                    // Run if set to whitelist only
                    assert voiceChannel != null;
                    assert member != null;
                    voiceChannel.getManager().putPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null).queue();
                    event.getHook().sendMessage("Added user:" + member.getUser().getGlobalName()).queue();
                } else {
                    assert voiceChannel != null;
                    assert member != null;
                    voiceChannel.getManager().putPermissionOverride(member, null, EnumSet.of(Permission.VIEW_CHANNEL)).queue();
                    voiceChannel.getManager().putPermissionOverride(member, null, EnumSet.of(Permission.VOICE_CONNECT)).queue();
                    event.getHook().sendMessage("Removed user:" + member.getUser().getGlobalName()).queue();

                }
            } else {
                event.getHook().sendMessage("You do not have permission to use this.").queue();
            }
        } else {
            event.reply("The command you sent was not properly filled out.").setEphemeral(true).queue();
        }


        return false;
    }

    @Override
    public SlashCommandData updateCommand() {
        return Commands.slash(name, commandDescription)
                .addOption(OptionType.BOOLEAN, "isadded", "Add/remove a user from your whitelist.")
                .addOption(OptionType.USER, "username", "The user to perform this action on.");
    }
}
