package de.joshua.api.commands.slashcommands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.internal.interactions.CommandDataImpl

interface SlashCommand {

    fun commandData(): CommandDataImpl
    fun onExecute(event: SlashCommandInteractionEvent)
}
