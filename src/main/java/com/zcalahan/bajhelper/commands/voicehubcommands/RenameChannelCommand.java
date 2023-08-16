package com.zcalahan.bajhelper.commands.voicehubcommands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Command that renames a Hub Channel */
public class RenameChannelCommand extends HubCommandBase {
    public RenameChannelCommand(String name) {
        super(name);
        ephemeral = false;
        commandDescription = "Rename the Voice Channel";
        botChannelOnly = false;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        // Check that all options are filled out.
        List<OptionMapping> validator = new ArrayList<>();
        validator.add(event.getOption("newname"));

        if (validateOptions(validator)) {
            event.deferReply().setEphemeral(ephemeral).queue();

            // Get VoiceChannel (yes this code sucks. it's what I got.
            Map<VoiceChannel, TextChannel> channels = voiceAdmins.get(event.getMember());
            VoiceChannel voiceChannel = null;
            for (VoiceChannel channel : channels.keySet()) {
                voiceChannel = channel;
                break;
            }

            // Check that channel belongs to user, and is in the text chat.
            if (voiceAdmins.containsKey(event.getMember()) && event.getChannel().equals(channels.get(voiceChannel))) {
                // Get Option
                String name = Objects.requireNonNull(event.getOption("newname")).getAsString();

                assert voiceChannel != null;
                voiceChannel.getManager().setName(name).complete();
                event.getHook().sendMessage("Name has been set to:" + name).queue();
            } else {
                event.getHook().sendMessage("You do not have permission to use this.").queue();
            }
        } else {
            event.reply("The command you sent was not properly filled out.").setEphemeral(true).queue();
        }
    }

    @Override
    public SlashCommandData updateCommand() {
        return super.updateCommand()
                .addOption(OptionType.STRING, "newname", "New name of the channel");
    }
}
