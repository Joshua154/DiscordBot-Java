package de.joshua.api.buttons

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.ItemComponent

interface Button {

    fun id(): String
    fun onExecute(event: ButtonInteractionEvent)
    fun getButton(): ItemComponent
}
