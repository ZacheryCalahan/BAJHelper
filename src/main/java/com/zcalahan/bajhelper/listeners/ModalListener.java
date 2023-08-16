package com.zcalahan.bajhelper.listeners;

import com.zcalahan.bajhelper.BotConfiguration;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Objects;

public class ModalListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("reportPrompt")) {
            String subject = Objects.requireNonNull(event.getValue("subject")).getAsString();
            String body = Objects.requireNonNull(event.getValue("body")).getAsString();

            List<TextChannel> reportChannel = Objects.requireNonNull(event.getGuild()).getTextChannelsByName(BotConfiguration.reportsChannel, true);
            for (TextChannel textChannel : reportChannel) {
                ThreadChannel threadChannel = textChannel.createThreadChannel(subject).complete();
                threadChannel.sendMessage(body).queue();
            }

            event.reply("Thank you for helping the community.").setEphemeral(true).queue();
        }
    }
}
