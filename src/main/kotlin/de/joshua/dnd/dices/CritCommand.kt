package de.joshua.dnd.dices

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.time.Instant

@SlashCommands
class CritCommand : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("crit", "crit")
            .addOptions(
                OptionData(OptionType.USER, "user", "User to roll for"),
                OptionData(OptionType.BOOLEAN, "private", "Roles a crit privately"),
                OptionData(OptionType.STRING, "location", "location").addChoices(
                    Command.Choice("head", "head"),
                    Command.Choice("arm", "arm"),
                    Command.Choice("body", "body"),
                    Command.Choice("leg", "leg")
                ),
                OptionData(OptionType.INTEGER, "modifier", "Modifier to add to the roll")
            )
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        val ephemeral = event.getOption("private") != null && event.getOption("private")?.asBoolean!!
        val modifier: Int = event.getOption("modifier")?.asInt ?: 0

        var user = event.user
        if(event.getOption("user") != null){
            user = event.getOption("user")?.asUser!!
        }

        var crit: CritEntry? = Dice.getCrit(Dice.rollDice(), Dice.rollDice() + modifier)
        if(event.getOption("location") != null){
            crit = Dice.getCrit(getBodyPartNumber(event.getOption("location")?.asString!!), Dice.rollDice() + modifier)
        }
        if(crit ==  null){
            event.reply("Error")
            return
        }


        val embed = EmbedBuilder()
            .setAuthor(
                user.name,
                null,
                user.avatarUrl
            )
            .setTitle("${crit.description} (${crit.intensity - modifier} + ${modifier}mod)")
            .addField("Location", "${Dice.getBodyPart(crit.location)} (${crit.location})", false)
            .addField("Wounds", crit.wounds, false)
            .addField("Additional Effects", crit.additionalEffects, false)
            .setTimestamp(Instant.now())


        event.replyEmbeds(embed.build()).setEphemeral(ephemeral).setActionRow(Dice.getActionRow()).queue()
    }
}

/*

`**Location**:
${finalString} (_${roll}_)

**Wounds**:
${list[`${num}`].wounds}

**Additional Effects**:
${list[`${num}`].additionalEffects}`

*/