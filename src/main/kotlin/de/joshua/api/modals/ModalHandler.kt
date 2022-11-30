package de.joshua.api.modals

import de.joshua.api.unused.Unused
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.reflections.Reflections

class ModalHandler : ListenerAdapter() {

    private val modals = ArrayList<Modal>()

    fun registerModals() {
        val reflections = Reflections("de.joshua")
        val typesAnnotatedWith = reflections.getTypesAnnotatedWith(Modals::class.java)
        typesAnnotatedWith.filter { !it.isAnnotationPresent(Unused::class.java) }.forEach {
            val instance = it.getDeclaredConstructor().newInstance() as Modal
            modals.add(instance)
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        modals.forEach {
            if (it.id() == event.modalId) {
                it.onExecute(event)
            }
        }
    }
}
