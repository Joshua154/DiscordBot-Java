package de.joshua.dnd

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.time.Instant

@SlashCommands
class Pedder : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("peter", "peter")
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        val user = event.user
        val embed = EmbedBuilder()
            .setAuthor(
                user.name,
                null,
                user.avatarUrl
            )
            .setTimestamp(Instant.now())
            .setDescription("Du bist ein Idiot")

        event.replyEmbeds(embed.build()).setActionRow(Dice.getActionRow()).queue()
    }
}