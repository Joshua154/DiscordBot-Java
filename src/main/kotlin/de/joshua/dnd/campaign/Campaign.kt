package de.joshua.dnd.campaign

data class Campaign(
    val categoryId: Long,
    val roleId: Long,
    val ownerId: Long,
    val name: String
)
