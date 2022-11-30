package de.joshua.api.roles

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

object Roles {

    val setup: Long = 1040932067464196116L
    val player: Long = 1040932323140567090L

    fun toRole(id: Long, guild: Guild): Role {
        return guild.getRoleById(id)!!
    }
}
