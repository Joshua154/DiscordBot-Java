package de.joshua.dnd.campaign

import de.joshua.api.commands.slashcommands.SlashCommand
import de.joshua.api.commands.slashcommands.SlashCommands
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.sql.Timestamp

@SlashCommands
class createCampaign : SlashCommand {
    override fun commandData(): CommandDataImpl {
        return CommandDataImpl("create_campaign", "creates a new Campaign")
            .addOptions(
                OptionData(OptionType.STRING, "name", "Name of the campaign", true),
                OptionData(OptionType.USER, "user", "Owner of the campaign")
            )
    }

    override fun onExecute(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val campaignName = event.getOption("name")?.asString + " - Campaign"
        val campaignOwner = event.getOption("user")?.asUser ?: return

        if (campaignName == null) {
            return event.replyEmbeds(
                EmbedBuilder()
                    .setTitle("Error")
                    .setDescription("campaign Name is required")
                    .setColor(Color.RED)
                    .build()
            ).queue()
        }

        if (guild.getCategoriesByName(campaignName, true).size > 0) {
            return event.replyEmbeds(
                EmbedBuilder()
                    .setTitle("Error")
                    .setDescription("A campaign with this name already exists")
                    .setColor(Color.RED)
                    .build()
            ).setEphemeral(true).queue()
        }
        if (guild.getRolesByName(campaignName, true).size > 0) {
            return event.replyEmbeds(
                EmbedBuilder()
                    .setTitle("Error")
                    .setDescription("A campaign role with this name already exists")
                    .setColor(Color.RED)
                    .build()
            ).setEphemeral(true).queue()
        }

        guild.createRole()
            .setName(campaignName)
            .queue()

        Thread.sleep(500)

        val role = guild.roles.find { campaignName == it.name } ?: return event.replyEmbeds(
            EmbedBuilder()
                .setTitle("Error")
                .setDescription("Role not found")
                .setColor(Color.RED)
                .build()
        ).setEphemeral(true).queue()

        val everyone = guild.roles.find { "@everyone" == it.name } ?: return event.replyEmbeds(
            EmbedBuilder()
                .setTitle("Error")
                .setDescription("Everyone Role not found")
                .setColor(Color.RED)
                .build()
        ).setEphemeral(true).queue()

        guild.addRoleToMember(campaignOwner, role).queue()


        guild.createCategory(campaignName)
            .addRolePermissionOverride(role.idLong, listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), listOf())
            .addRolePermissionOverride(
                everyone.idLong,
                listOf(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND)
            )
            .queue()

        Thread.sleep(500)

        val category = guild.categories.find { campaignName == it.name } ?: return event.replyEmbeds(
            EmbedBuilder()
                .setTitle("Error")
                .setDescription("category not found")
                .setColor(Color.RED)
                .build()
        ).setEphemeral(true).queue()

        category.createTextChannel("main").queue()
        category.createTextChannel("character-sheets").queue()
        category.createTextChannel("dice").queue()


//        val campaigns = readCsv(FileInputStream("campaings.csv"))
//        campaigns.add(Campaign(category.idLong, role.idLong, campaignOwner.idLong, campaignName))
//        FileOutputStream("campaigns.csv").apply { writeCsv(campaigns) }


        event.replyEmbeds(
            EmbedBuilder()
                .setTitle("Success")
                .setDescription("Campaign **$campaignName** created")
                .setColor(Color.GREEN)
                .build()
        ).setEphemeral(true).queue()
    }

    fun OutputStream.writeCsv(campaigns: List<Campaign>) {
        val writer = bufferedWriter()
        writer.write("""categoryId, roleId, ownerId, name""")
        writer.newLine()
        campaigns.forEach {
            writer.write("${it.categoryId}, ${it.roleId}, ${it.ownerId}, ${it.name}")
            writer.newLine()
        }
        writer.flush()
    }
    private fun readCsv(inputStream: InputStream): MutableList<Campaign> {
        val reader = inputStream.bufferedReader()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (categoryId, roleId, ownerId, name) = it.split(',', ignoreCase = false, limit = 3)
                Campaign(categoryId.trim().toLong(), roleId.trim().toLong(), ownerId.trim().toLong(), name)
            }.toMutableList()
    }
}