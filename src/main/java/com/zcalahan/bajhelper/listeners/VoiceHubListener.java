package com.zcalahan.bajhelper.listeners;

import com.zcalahan.bajhelper.BotConfiguration;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

/** Class that listens for the joining of a specific hub channel, then creates a simple voice channel for that user. When empty, it deletes the channel. */
public class VoiceHubListener extends ListenerAdapter {

    Map<VoiceChannel, TextChannel> channelTies = new HashMap<>();
    public static Map<Member, Map<VoiceChannel, TextChannel>> voiceAdmin = new HashMap<>();

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Channel joinedChannel = event.getChannelJoined();
        Channel leftChannel = event.getChannelLeft();

        if (joinedChannel != null) {
            if(joinedChannel.getName().equals(BotConfiguration.voiceHubChannel)) {
                onChannelCreate(event);
            }
        }

        if (leftChannel != null) {
            if (Objects.requireNonNull(event.getChannelLeft()).asVoiceChannel().getMembers().isEmpty() && !event.getChannelLeft().asVoiceChannel().getName().equals(BotConfiguration.voiceHubChannel) && Objects.requireNonNull(event.getChannelLeft().getParentCategory()).getName().equals(BotConfiguration.voiceHubCategory)) {
                // If channel is empty and not the hub channel, run this.
                onChannelDelete(event);
            }
        }
    }
    /** Called when a user joins a voice channel */
    public void onChannelCreate(GuildVoiceUpdateEvent event) {
        // Inits
        Guild guild = event.getGuild();
        Member member = event.getMember();

        // Create voice channel and move member
        VoiceChannel voiceChannel = event.getGuild().createVoiceChannel(member.getUser().getGlobalName() + "'s Channel", event.getGuild().getCategoriesByName(BotConfiguration.voiceHubCategory, true).get(0)).complete();
        event.getGuild().moveVoiceMember(member, voiceChannel).queue();

        // Create text channel and send message
        Category category = guild.getCategoriesByName(BotConfiguration.voiceHubCategory, true).get(0); // We're assuming there is only one category with this name, which should be true.
        TextChannel textChannel = guild.createTextChannel(member.getUser().getGlobalName() + "'s Channel")
                .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .setParent(category).complete();
        textChannel.sendMessage("This is a channel for your Voice Channel's moderation. The available commands are: \n" +
                "```/rename-channel name\n/set-userlimit userlimit (0-99)\n/set-whitelist isWhitelist\n/whitelist idAdded (add/remove user) username.``` " +
                "Regardless of setting, admins may see the channel, and the text channel.").queue(); // This is never null, but nothing really happens if it is.

        // Tie Voice and Text channels together so they are disposed of together. Then tie a member to the channels so the user may use commands.
        channelTies.put(voiceChannel, textChannel);
        voiceAdmin.put(member, channelTies);
    }

    /** Called when a user leaves a voice channel */
    public void onChannelDelete(GuildVoiceUpdateEvent event) {
        VoiceChannel voiceChannel = Objects.requireNonNull(Objects.requireNonNull(event.getChannelLeft()).asVoiceChannel());

        // Remove voice channel
        voiceChannel.delete().queue();

        // Find and remove text channel
        channelTies.get(voiceChannel).delete().queue();

        // Remove permission mapping of channels and members
        Member member = null;
        for (Member admin : voiceAdmin.keySet()) {
            member = admin;
            break;
        }
        voiceAdmin.remove(member);
        channelTies.remove(voiceChannel);
    }
}