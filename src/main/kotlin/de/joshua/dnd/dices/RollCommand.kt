package de.joshua.dnd.dices

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.time.Instant
import kotlin.random.Random

@SlashCommands
class RollCommand : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("roll", "roles a custom Dice")
            .addOptions(
                OptionData(OptionType.STRING, "custom", "custom dice like 1d6"),
                OptionData(OptionType.BOOLEAN, "private", "Roles a dice privately")
            )
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        val ephemeral = event.getOption("private") != null && event.getOption("private")?.asBoolean!!

        var user = event.user
        if(event.getOption("user") != null){
            user = event.getOption("user")?.asUser!!
        }

        val custom: String = event.getOption("custom")?.asString!!

        val embed = EmbedBuilder()
            .setAuthor(
                user.name,
                null,
                user.avatarUrl
            )
            .setTimestamp(Instant.now())
            .setTitle("Rolled $custom")
            .setDescription(Dice.rollDiceString(custom))

        event.replyEmbeds(embed.build()).setEphemeral(ephemeral).setActionRow(Dice.getActionRow()).queue()
    }
}
