package de.joshua.api.commands

import de.joshua.api.commands.messagecommands.MessageCommand
import de.joshua.api.commands.messagecommands.MessageCommands
import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import de.joshua.api.commands.usercommands.UserCommand
import de.joshua.api.commands.usercommands.UserCommands
import de.joshua.api.unused.Unused
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.reflections.Reflections
import java.util.concurrent.TimeUnit

class CommandHandler() : ListenerAdapter() {

    private val slashCommands = ArrayList<SlashCommand>()
    private val userContext = ArrayList<UserCommand>()
    private val messageContext = ArrayList<MessageCommand>()
    private val reflections = Reflections("de.joshua")

    private fun addMessageCommands() {
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(MessageCommands::class.java)
        typesAnnotatedWith.filter { !it.isAnnotationPresent(Unused::class.java) }.forEach {
            val instance = it.getDeclaredConstructor().newInstance() as MessageCommand
            messageContext.add(instance)
        }
    }

    private fun addSlashCommands() {
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(SlashCommands::class.java)
        typesAnnotatedWith.filter { !it.isAnnotationPresent(Unused::class.java) }.forEach {
            val instance = it.getDeclaredConstructor().newInstance() as SlashCommand
            slashCommands.add(instance)
        }
    }

    private fun addUserCommands() {
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(UserCommands::class.java)
        typesAnnotatedWith.filter { !it.isAnnotationPresent(Unused::class.java) }.forEach {
            val instance = it.getDeclaredConstructor().newInstance() as UserCommand
            userContext.add(instance)
        }
    }

    fun sendCommands(guild: Guild) {
        addMessageCommands()
        addUserCommands()
        addSlashCommands()
        val updateCommands = guild.updateCommands()

        slashCommands.forEach {
            updateCommands.addCommands(it.commandData()).queueAfter(1, TimeUnit.SECONDS)
            println(it.commandData().name + " on " + guild.name)
        }

        userContext.forEach {
            updateCommands.addCommands(it.commandData()).queueAfter(1, TimeUnit.SECONDS)
            println(it.commandData().name + " on " + guild.name)
        }

        messageContext.forEach {
            updateCommands.addCommands(it.commandData()).queueAfter(1, TimeUnit.SECONDS)
            println(it.commandData().name + " on " + guild.name)
        }

        updateCommands.queueAfter(5, TimeUnit.SECONDS)
    }

    fun sendCommands(guilds: List<Guild>) {
        addMessageCommands()
        addUserCommands()
        addSlashCommands()
        for (guild in guilds) {
            val updateCommands = guild.updateCommands()

            slashCommands.forEach {
                updateCommands.addCommands(it.commandData()).queueAfter(1, TimeUnit.SECONDS)
                println(it.commandData().name + " on " + guild.name)
            }

            userContext.forEach {
                updateCommands.addCommands(it.commandData()).queueAfter(1, TimeUnit.SECONDS)
                println(it.commandData().name + " on " + guild.name)
            }

            messageContext.forEach {
                updateCommands.addCommands(it.commandData()).queueAfter(1, TimeUnit.SECONDS)
                println(it.commandData().name + " on " + guild.name)
            }

            updateCommands.queueAfter(5, TimeUnit.SECONDS)
        }
    }

    override fun onUserContextInteraction(event: UserContextInteractionEvent) {
        userContext.forEach {
            if (it.commandData().name == event.commandString) {
                it.onExecute(event)
            }
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        slashCommands.forEach {
            if (it.commandData().name == event.commandString.split(" ")[0].substring(1)) {
                it.onExecute(event)
            }
        }
    }

    override fun onMessageContextInteraction(event: MessageContextInteractionEvent) {
        messageContext.forEach {
            if (it.commandData().name == event.commandString.split(" ")[0].substring(1)) {
                it.onExecute(event)
            }
        }
    }
}
