package de.joshua.dnd.dices

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.time.Instant

@SlashCommands
class DiceCommand : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("dice", "roles a d100 Test")
            .addOptions(
                OptionData(OptionType.INTEGER, "times", "Amount of roles"),
                OptionData(OptionType.USER, "user", "User to roll for"),
                OptionData(OptionType.BOOLEAN, "private", "Roles a dice privately")
            )
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        val ephemeral = event.getOption("private") != null && event.getOption("private")?.asBoolean!!

        var user = event.user
        if (event.getOption("user") != null) {
            user = event.getOption("user")?.asUser!!
        }

        if (event.getOption("times") != null && event.getOption("times")?.asInt!! > 1) {
            val rolls = List(event.getOption("times")?.asInt!!) { Dice.rollDice() }

            val embed = EmbedBuilder()
                .setAuthor(
                    user.name,
                    null,
                    user.avatarUrl
                )
                .setTimestamp(Instant.now())
                .setTitle("Rolled ${rolls.size} times")
                .setDescription(rolls.joinToString { "\n" + it.toString() })

            event.replyEmbeds(embed.build()).setEphemeral(ephemeral).setActionRow(Dice.getActionRow()).queue()
        } else {
            val roll = Dice.rollDice(100)

            val embed = EmbedBuilder()
                .setAuthor(
                    user.name,
                    null,
                    user.avatarUrl
                )
                .setTitle(roll.toString())
                .setTimestamp(Instant.now())

            event.replyEmbeds(embed.build()).setEphemeral(ephemeral).setActionRow(Dice.getActionRow()).queue()
        }
    }
}
