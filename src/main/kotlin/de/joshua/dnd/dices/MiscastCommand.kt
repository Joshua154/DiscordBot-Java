package de.joshua.dnd.dices

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import de.joshua.DiscordBot
import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.io.StringReader
import java.time.Instant

@SlashCommands
class MiscastCommand : SlashCommand {
  override fun commandData(): CommandDataImpl {
    return CommandDataImpl("miscast", "miscast")
        .addSubcommands(
            SubcommandData("minor", "minor miscast")
                .addOptions(
                    OptionData(OptionType.INTEGER, "times", "Amount of roles"),
                    OptionData(OptionType.USER, "user", "User to roll for"),
                    OptionData(OptionType.BOOLEAN, "private", "Roles a dice privately")),
            SubcommandData("major", "major miscast")
                .addOptions(
                    OptionData(OptionType.INTEGER, "times", "Amount of roles"),
                    OptionData(OptionType.USER, "user", "User to roll for"),
                    OptionData(OptionType.BOOLEAN, "private", "Roles a dice privately")))
  }

  override fun onExecute(event: SlashCommandInteractionEvent) {
    val ephemeral = event.getOption("private") != null && event.getOption("private")?.asBoolean!!

    var user = event.user
    if (event.getOption("user") != null) {
      user = event.getOption("user")?.asUser!!
    }

    if (event.getOption("times") != null && event.getOption("times")?.asInt!! > 1) {
      val rolls = List(event.getOption("times")?.asInt!!) { getEntryMaxVal(getFileContent(event.subcommandName?:"minor"),
          Dice.rollDice(100)
      ) }

      val embed =
          EmbedBuilder()
              .setAuthor(user.name, null, user.avatarUrl)
              .setTimestamp(Instant.now())
              .setTitle("Rolled ${rolls.size} ${event.subcommandName?:"minor"} Miscasts")
              .setDescription(rolls.joinToString { "\n" + it.description })

      event
          .replyEmbeds(embed.build())
          .setEphemeral(ephemeral)
          .setActionRow(Dice.getActionRow())
          .queue()
    } else {
      val roll = getEntryMaxVal(getFileContent(event.subcommandName?:"minor"), Dice.rollDice(100))

      val embed =
          EmbedBuilder()
              .setAuthor(user.name, null, user.avatarUrl)
              .setTitle("${event.subcommandName?:"minor"} miscast")
              .setDescription(roll.description)
              .setTimestamp(Instant.now())

      event
          .replyEmbeds(embed.build())
          .setEphemeral(ephemeral)
          .setActionRow(Dice.getActionRow())
          .queue()
    }
  }

    private fun getFileContent(type: String): ArrayList<MiscastTableEntry> {
        val file = DiscordBot::class.java.getResource("/miscast/${type}.json")

        val result = arrayListOf<MiscastTableEntry>()
        val klaxon = Klaxon()
        if (file != null) {
            JsonReader(StringReader(file.readText())).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        klaxon.parse<MiscastTableEntry>(reader)?.let { result.add(it) }
                    }
                }
            }
        }
        return result
    }

    private fun getEntryMaxVal(result: ArrayList<MiscastTableEntry>, number: Int): MiscastTableEntry {
        var index = -1
        for (i in result.size - 1 downTo 0) {
            if (result.get(i).max <= number) {
                index = i
                break
            }
        }
        if(index == -1) index = 0
        return result[index]
    }

    private data class MiscastTableEntry(
        val max: Int,
        val description: String
    )
}
