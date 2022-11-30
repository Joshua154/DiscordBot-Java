package de.joshua.dnd

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.awt.Color
import java.time.Instant

@SlashCommands
class TestPassedCommand : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("dice", "roles a d100 Test")
            .addOptions(
                OptionData(OptionType.INTEGER, "times", "Amount of roles"),
                OptionData(OptionType.USER, "user", "User to roll for"),
                OptionData(OptionType.BOOLEAN, "private", "Roles a dice privately")
            )
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        val user = event.user

        val emp = event.getOption("private") != null && event.getOption("private")?.asBoolean!!

        if (event.getOption("times") != null && event.getOption("times")?.asInt!! > 1) {
            val rolls = List(event.getOption("times")?.asInt!!) { Dice.rollDice(100) }

            val embed = EmbedBuilder()
                .setAuthor(
                    user.name,
                    null,
                    user.avatarUrl
                )
                .setTimestamp(Instant.now())
                .setTitle("Rolled ${rolls.size} times")
                .setDescription(rolls.joinToString { "\n" + (it + 1).toString() })

            event.replyEmbeds(embed.build()).setEphemeral(emp).queue()
        } else {
            val roll = Dice.rollDice(100)

            val embed = EmbedBuilder()
                .setAuthor(
                    user.name,
                    null,
                    user.avatarUrl
                )
                .setTitle((roll + 1).toString())
                .setTimestamp(Instant.now())

            if (Dice.isCrit(roll)) {
                embed.setColor(Color.RED)
            }

            event.replyEmbeds(embed.build()).setEphemeral(emp).queue()
        }
    }
}
