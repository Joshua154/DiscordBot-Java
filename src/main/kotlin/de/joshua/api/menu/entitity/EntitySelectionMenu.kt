package de.joshua.api.menu.entitity

import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent

interface EntitySelectionMenu {

    fun id(): String
    fun onExecute(event: EntitySelectInteractionEvent)
}
