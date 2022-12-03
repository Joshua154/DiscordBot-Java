package de.joshua

import de.joshua.api.buttons.ButtonHandler
import de.joshua.api.commands.CommandHandler
import de.joshua.api.menu.SelectionMenuHandler
import de.joshua.api.modals.ModalHandler
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class DiscordBot {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DiscordBot()
        }
    }

    init {
        println("Starting Bot ...")
        val builder =
            JDABuilder.createDefault("NzA0NjY0MTc2MzUzNjA3NzIx.GzxUXT.CX7x9YyKCIbYjLcYzPYv5hnfXlasdGjEs_qJUo")
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.SCHEDULED_EVENTS)
        builder.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.SCHEDULED_EVENTS)

        val commandHandler = CommandHandler()
        val buttonHandler = ButtonHandler()
        val modalHandler = ModalHandler()
        val menuHandler = SelectionMenuHandler()

        builder.addEventListeners(commandHandler, buttonHandler, modalHandler, menuHandler)

        // builder.setActivity(Activity.playing("Test"))

        val jda = builder.build().awaitReady()

        val guild = jda.guilds.filter { it.id == "777522801368236043" }[0]

        //commandHandler.sendCommands(guild)
        commandHandler.sendCommands(jda.guilds)
        buttonHandler.registerButtons()
        modalHandler.registerModals()
        menuHandler.registerEntityMenus()
        menuHandler.registerStringMenus()

        println("Bot is running")
    }
}

