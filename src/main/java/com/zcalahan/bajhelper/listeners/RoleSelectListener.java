package com.zcalahan.bajhelper.listeners;

import ch.qos.logback.core.net.SocketConnector;
import com.zcalahan.bajhelper.BotConfiguration;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okio.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Listener designed to listen to reactions to various emojis. Changes roles based on reaction. */
public class RoleSelectListener extends ListenerAdapter {
    /** Where roles are stored */
    public static HashMap<String, String> roles = new HashMap<>();

    /** Default roles and respective emojis go here. */
    public RoleSelectListener() {

    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        //saveRoles(event.getGuild());
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        roleSelect(true, event);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        roleSelect(false, event);
    }

    /** Changes user roles based on type of reaction. */
    public void roleSelect(boolean addRole, GenericMessageReactionEvent event) {
        roles = loadRoles(event.getGuild());

        // Get info
        UserSnowflake user = event.retrieveUser().complete();
        String emoji = event.getEmoji().toString();

        // If role isn't mapped to the reaction, or the channel in question is not the role react channel, do nothing.
        if (!roles.containsKey(emoji) || !event.getChannel().getName().equals(BotConfiguration.roleReactChannel)) return;

        // Get the roles associated with each reaction
        List<Role> role = event.getGuild().getRolesByName(roles.get(emoji), true); // This keeps being empty. god save us all

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

    /** Loads the saved hashmap. Returns an empty hashmap if not found. */
    @Nullable
    public static HashMap<String, String> loadRoles(Guild guild) {
        try {
            File file = new File("./" + guild.getId()).getAbsoluteFile();
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            @SuppressWarnings("unchecked") // love this!!!
            HashMap<String, String> mapInFile = (HashMap<String, String>) ois.readObject();

            ois.close();
            fis.close();
            return mapInFile;
        } catch (Exception e) {
            HashMap<String, String> temp = new HashMap<>();
            return temp;

        }


    }

    /** Saves the hashmap to the running directory with the guild id with the file name roles.ser */
    public static void saveRoles(Guild guild)  {
        try {

            File fileOne = new File("./" + guild.getId()).getAbsoluteFile();
            fileOne.createNewFile();
            FileOutputStream fos = new FileOutputStream(fileOne);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(roles);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
