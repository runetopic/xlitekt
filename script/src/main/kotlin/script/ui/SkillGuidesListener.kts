import xlitekt.game.actor.player.script
import xlitekt.game.content.ui.InterfaceEvent
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarBit

private val skillBits = mapOf(
    1 to 1,
    2 to 2,
    3 to 5,
    4 to 3,
    5 to 7,
    6 to 4,
    7 to 12,
    8 to 22,
    9 to 6,
    10 to 8,
    11 to 9,
    12 to 10,
    13 to 11,
    14 to 19,
    15 to 20,
    16 to 23,
    17 to 13,
    18 to 14,
    19 to 15,
    20 to 16,
    21 to 17,
    22 to 18,
    23 to 21
)

onInterface<UserInterface.SkillGuide> {
    onOpen {
        setEvent(8, 0..99, InterfaceEvent.NONE)
    }
}

onInterface<UserInterface.Skills> {
    onClick {
        // TODO fix camera zoom when skill interface is open
//        println("${it.interfaceId.packInterface(it.childId)}")
        val guide = skillBits[it.childId] ?: return@onClick

        script(2524, -1, -1)
        vars[VarBit.SkillGuide] = guide
        interfaces += UserInterface.SkillGuide
    }
}
