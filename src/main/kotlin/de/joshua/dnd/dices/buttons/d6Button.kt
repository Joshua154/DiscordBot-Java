package de.joshua.dnd.dices.buttons

import de.joshua.api.buttons.Button
import de.joshua.api.buttons.Buttons
import de.joshua.dnd.dices.Dice
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.ItemComponent
import java.time.Instant

@Buttons
class d6Button : Button {
    override fun id(): String {
        return "joshibot-dnd-button-d6"
    }

    override fun getButton(): ItemComponent {
        return net.dv8tion.jda.api.interactions.components.buttons.Button.success(id(), "d6")
    }

    override fun onExecute(event: ButtonInteractionEvent) {
        val user = event.user

        val roll = Dice.rollDice(6)

        val embed = EmbedBuilder()
            .setAuthor(
                user.name,
                null,
                user.avatarUrl
            )
            .setDescription(roll.toString())
            .setTitle("1d6")
            .setTimestamp(Instant.now())

        event.replyEmbeds(embed.build()).setActionRow(Dice.getActionRow()).queue()
    }
}