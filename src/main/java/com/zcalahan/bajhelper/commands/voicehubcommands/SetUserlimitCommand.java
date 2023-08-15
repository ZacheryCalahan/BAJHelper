package com.zcalahan.bajhelper.commands.voicehubcommands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import java.util.Map;
import java.util.Objects;

/** Command that sets the user limit in a Hub Channel */
public class SetUserlimitCommand extends HubCommandBase {
    public SetUserlimitCommand(String name) {
        super(name);
        commandDescription = "Set how many users can join this channel.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
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
            int userlimit = Objects.requireNonNull(event.getOption("userlimit")).getAsInt();
            System.out.println(userlimit);
            if (userlimit > 0 || userlimit < 99) {
                assert voiceChannel != null;
                voiceChannel.getManager().setUserLimit(userlimit).queue();
                event.getHook().sendMessage("User limit set to: " + userlimit).queue();
            } else {
                event.getHook().sendMessage("User limit must be between 0 and 99.").queue();
            }
        } else {
            event.getHook().sendMessage("You do not have permission to use this.").queue();
        }
    }



    @Override
    public SlashCommandData updateCommand() {
        return Commands.slash(name, commandDescription)
                .addOption(OptionType.INTEGER, "userlimit", "How many users can join this channel (0 to reset, up to 99).");
    }
}
