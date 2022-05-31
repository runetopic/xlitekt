import xlitekt.game.actor.overheadChat
import xlitekt.game.event.EventBus
import xlitekt.game.event.impl.Events
import xlitekt.shared.inject
import java.util.Random
import java.util.Timer
import kotlin.concurrent.timerTask

/**
 * @author Justin Kenney
 */
val eventBus by inject<EventBus>()

eventBus.onEvent<Events.NPCSpawnEvent>().filter {
    this.npc.entry?.name == "Duck"
}.use {
    quack()
}

fun Events.NPCSpawnEvent.quack() {
    val npc = this.npc
    Timer().scheduleAtFixedRate(
        timerTask {
            npc.overheadChat {
                "Quack!"
            }
        },
        5000, Random().nextLong(5_000, 10_000)
    )
}
