package de.joshua.api.modals

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

interface Modal {

    fun id(): String
    fun onExecute(event: ModalInteractionEvent)
}
