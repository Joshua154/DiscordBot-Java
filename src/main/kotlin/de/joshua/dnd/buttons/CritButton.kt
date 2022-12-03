package de.joshua.dnd.buttons

import de.joshua.api.buttons.Button
import de.joshua.api.buttons.Buttons
import de.joshua.dnd.CritEntry
import de.joshua.dnd.Dice
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.ItemComponent
import java.time.Instant

@Buttons
class CritButton : Button {
    override fun id(): String {
        return "joshibot-dnd-button-crit"
    }

    override fun getButton(): ItemComponent {
        return net.dv8tion.jda.api.interactions.components.buttons.Button.success(id(), "Crit")
    }

    override fun onExecute(event: ButtonInteractionEvent) {
        val user = event.user

        val crit: CritEntry = Dice.getCrit()
        val embed = EmbedBuilder()
            .setAuthor(
                user.name,
                null,
                user.avatarUrl
            )
            .setTitle("${crit.description} (${crit.intensity})")
            .addField("Location", "${Dice.getBodyPart(crit.location)} (${crit.location})", false)
            .addField("Wounds", crit.wounds, false)
            .addField("Additional Effects", crit.additionalEffects, false)
            .setTimestamp(Instant.now())
        event.replyEmbeds(embed.build()).setActionRow(Dice.getActionRow()).queue()
    }
}