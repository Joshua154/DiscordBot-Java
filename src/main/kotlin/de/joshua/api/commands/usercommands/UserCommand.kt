package de.joshua.api.commands.usercommands

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface UserCommand {

    fun commandData(): CommandData
    fun onExecute(event: UserContextInteractionEvent)
}
