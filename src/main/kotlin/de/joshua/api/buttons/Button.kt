package de.joshua.api.buttons

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

interface Button {

    fun id(): String
    fun onExecute(event: ButtonInteractionEvent)
}
