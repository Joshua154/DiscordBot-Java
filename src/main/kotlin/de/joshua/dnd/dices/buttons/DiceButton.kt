package de.joshua.dnd.dices.buttons

import de.joshua.api.buttons.Button
import de.joshua.api.buttons.Buttons
import de.joshua.dnd.dices.Dice
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.ItemComponent
import java.time.Instant

@Buttons
class DiceButton : Button {
    override fun id(): String {
      return "joshibot-dnd-button-dice"
    }

    override fun getButton(): ItemComponent {
      return net.dv8tion.jda.api.interactions.components.buttons.Button.success(id(), "Dice")
    }

    override fun onExecute(event: ButtonInteractionEvent) {
      val user = event.user

      val roll = Dice.rollDice()

      val embed = EmbedBuilder()
        .setAuthor(
          user.name,
          null,
          user.avatarUrl
        )
        .setTitle(roll.toString())
        .setTimestamp(Instant.now())

      event.replyEmbeds(embed.build()).setActionRow(Dice.getActionRow()).queue()
    }
}
