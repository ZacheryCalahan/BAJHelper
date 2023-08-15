package com.zcalahan.bajhelper.commands.voicehubcommands;

import com.zcalahan.bajhelper.commands.CommandBase;
import com.zcalahan.bajhelper.listeners.VoiceHubListener;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import java.util.Map;

/** Abstract class representing a Hub Command */
public abstract class HubCommandBase extends CommandBase {
    Map<Member, Map<VoiceChannel, TextChannel>> voiceAdmins = VoiceHubListener.voiceAdmin;

    public HubCommandBase(String name) {
        super(name);
        ephemeral = false;
        commandDescription = "Default hub command with no implementation";
        botChannelOnly = false;
    }
}
