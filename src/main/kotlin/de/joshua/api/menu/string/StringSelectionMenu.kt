package de.joshua.api.menu.string

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent

interface StringSelectionMenu {

    fun id(): String
    fun onExecute(event: StringSelectInteractionEvent)
}
