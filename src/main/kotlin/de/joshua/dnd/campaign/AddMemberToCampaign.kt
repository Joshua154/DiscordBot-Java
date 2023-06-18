package de.joshua.dnd.campaign

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.awt.Color

@SlashCommands
class AddMemberToCampaign : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("add_member", "add a member to a campaign")
            .addOptions(
                OptionData(OptionType.ROLE, "role", "Role of the campaign", true),
                OptionData(OptionType.USER, "user", "User to add", true)
            )
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val role = event.getOption("role")?.asRole ?: return
        val user = event.getOption("user")?.asUser ?: return

        if (!role.name.contains(" - Campaign"))
            return event.replyEmbeds(
                EmbedBuilder()
                    .setTitle("Error")
                    .setDescription("The role is not a campaign role")
                    .setColor(Color.RED)
                    .build()
            ).setEphemeral(true).queue()

        guild.addRoleToMember(user, role).queue()

        event.replyEmbeds(
            EmbedBuilder()
                .setTitle("Success")
                .setDescription("<@${user.id}> has been added to <@&${role.id}>")
                .setColor(Color.GREEN)
                .build()
        ).setEphemeral(true).queue()
    }
}