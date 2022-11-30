package de.joshua.api.buttons

import de.joshua.api.unused.Unused
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.reflections.Reflections

class ButtonHandler : ListenerAdapter() {

    private val buttons = ArrayList<Button>()
    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        buttons.forEach {
            if (it.id() == event.button.id) {
                it.onExecute(event)
            }
        }
    }

    fun registerButtons() {
        val reflections = Reflections("de.joshua")
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(Buttons::class.java)
        typesAnnotatedWith.filter { !it.isAnnotationPresent(Unused::class.java) }.forEach {
            val instance = it.getDeclaredConstructor().newInstance() as Button
            buttons.add(instance)
        }
    }
}
