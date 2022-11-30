package de.joshua.api.commands.messagecommands

import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface MessageCommand {

    fun commandData(): CommandData
    fun onExecute(event: MessageContextInteractionEvent)
}
