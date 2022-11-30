package de.joshua.api.menu

import de.joshua.api.menu.entitity.EntitySelectionMenu
import de.joshua.api.menu.entitity.EntitySelectionMenus
import de.joshua.api.menu.string.StringSelectionMenu
import de.joshua.api.menu.string.StringSelectionMenus
import de.joshua.api.unused.Unused
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.reflections.Reflections

class SelectionMenuHandler : ListenerAdapter() {

    private val entityMenuLists = ArrayList<EntitySelectionMenu>()
    private val stringMenuLists = ArrayList<StringSelectionMenu>()

    fun registerStringMenus() {
        val reflections = Reflections("de.joshua")
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(StringSelectionMenus::class.java)
        typesAnnotatedWith.filter { !it.isAnnotationPresent(Unused::class.java) }.forEach {
            val instance = it.getDeclaredConstructor().newInstance() as StringSelectionMenu
            stringMenuLists.add(instance)
        }
    }

    fun registerEntityMenus() {
        val reflections = Reflections("de.joshua")
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(EntitySelectionMenus::class.java)
        typesAnnotatedWith.filter { !it.isAnnotationPresent(Unused::class.java) }.forEach {
            val instance = it.getDeclaredConstructor().newInstance() as EntitySelectionMenu
            entityMenuLists.add(instance)
        }
    }

    override fun onEntitySelectInteraction(event: EntitySelectInteractionEvent) {
        entityMenuLists.forEach {
            if (it.id() == event.selectMenu.id) {
                it.onExecute(event)
            }
        }
    }

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        stringMenuLists.forEach {
            if (it.id() == event.selectMenu.id) {
                it.onExecute(event)
            }
        }
    }
}
