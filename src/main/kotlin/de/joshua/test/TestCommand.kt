package de.joshua.test

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import de.joshua.dnd.dices.Dice
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl

@SlashCommands
class TestCommand : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("test", "Test")
            .addOptions(OptionData(OptionType.INTEGER, "first", "Test"))
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        println(Dice.getColor(((event.getOption("first")?.asDouble ?: 0) as Double)))

        event.replyEmbeds(
            EmbedBuilder().setColor(Dice.getColor(((event.getOption("first")?.asDouble ?: 0) as Double)))
                .setTitle("Test").build()
        ).queue()
    }
}
