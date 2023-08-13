package com.zcalahan.bajhelper.listeners;


import com.zcalahan.bajhelper.BotConfiguration;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class RoleSelectListener extends ListenerAdapter {
    HashMap<String, String> roles = new HashMap<>();

    public RoleSelectListener() {
        roles.put("UnicodeEmoji(codepoints=U+1f5a5U+fe0f)", "Programmer");
        roles.put("UnicodeEmoji(codepoints=U+1f3ae)", "Player");
        roles.put("CustomEmoji:Osu(id=875159124730589194)", "Osu!");
        roles.put("CustomEmoji:DeadbyDaylight(id=872648891047247892)", "DBD");
        roles.put("CustomEmoji:Valorant(id=872646255803781159)", "Valorant");
        roles.put("CustomEmoji:Minecraft(id=924861929388904448)", "Minecraft");
    }
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        roleSelect(true, event);
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        roleSelect(false, event);
    }

    public void roleSelect(boolean addRole, GenericMessageReactionEvent event) {
        // Get info
        UserSnowflake user = event.retrieveUser().complete();
        String emoji = event.getEmoji().toString();

        // If role isn't mapped to the reaction, or the channel in question is not the role react channel, do nothing.
        if (!roles.containsKey(emoji) || !event.getChannel().getName().equals(BotConfiguration.roleReactChannel)) return;

        // Get the roles associated with each reaction
        List<Role> role = event.getGuild().getRolesByName(roles.get(emoji), true); // This keeps being empty. god save us all
        System.out.println(role.isEmpty());


        // Add or remove roles
        if (addRole) {
            for (Role temp:role) {
                event.getGuild().addRoleToMember(user, temp).queue();
            }
        } else {
            for (Role temp:role) {
                event.getGuild().removeRoleFromMember(user, temp).queue();
            }
        }
    }


}
