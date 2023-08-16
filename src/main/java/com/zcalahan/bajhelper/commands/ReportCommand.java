package com.zcalahan.bajhelper.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class ReportCommand extends CommandBase {
    public ReportCommand(String name) {
        super(name);
        ephemeral = true;
        commandDescription = "Report a user.";
        botChannelOnly = false;
    }

    @Override
    public boolean execute(SlashCommandInteractionEvent event) {
        // Create the modal
        TextInput subject = TextInput.create("subject", "Rule Broken", TextInputStyle.SHORT)
                .setPlaceholder("Who broke what rule")
                .setMinLength(0)
                .setMaxLength(100)
                .build();

        TextInput body = TextInput.create("body", "Description", TextInputStyle.PARAGRAPH)
                .setPlaceholder("What were the circumstances of this report? Please add the users that are in question.")
                .setMinLength(30)
                .setMaxLength(1000)
                .build();

        Modal modal = Modal.create("reportPrompt", "Report Menu")
                .addComponents(ActionRow.of(subject), ActionRow.of(body))
                .build();

        // Send Modal
        event.replyModal(modal).queue();
        return false;
    }



    @Override
    public SlashCommandData updateCommand() {
        return super.updateCommand();
    }
}
