package script.ui

import xlitekt.cache.provider.config.kit.KitEntryType
import xlitekt.cache.provider.config.kit.KitEntryTypeProvider
import xlitekt.game.actor.player.renderAppearance
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.Render.Appearance.Gender
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.BodyPartColor
import xlitekt.game.content.ui.InterfaceEvent
import xlitekt.game.content.ui.UserInterface.PlayerAppearanceDesigner
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarBit
import xlitekt.game.event.impl.Events
import xlitekt.game.event.onEvent
import xlitekt.shared.inject
import xlitekt.shared.toIntInv

/**
 * @author Jordan Abraham
 */
val kits by inject<KitEntryTypeProvider>()

onInterface<PlayerAppearanceDesigner> {
    onCreate {
        repeat(69) {
            setEvent(it, 0..68, InterfaceEvent.CLICK_OPTION_1)
        }
    }

    onClick("*") {
        val childId = it.childId
        val design = 12..37
        val color = 43..60
        val designing = childId in design
        val backwards = childId % 2 == designing.toIntInv()
        when (childId) {
            in design -> when (childId) {
                12, 13 -> BodyPart.Head
                16, 17 -> if (appearance.gender == Gender.Male) BodyPart.Jaw else null
                20, 21 -> BodyPart.Torso
                24, 25 -> BodyPart.Arms
                28, 29 -> BodyPart.Hands
                32, 33 -> BodyPart.Legs
                36, 37 -> BodyPart.Feet
                else -> null
            }?.let { bodyPart -> appearance.changeBodyPart(bodyPart, backwards) }
            in color -> when (childId) {
                43, 44 -> BodyPartColor.Hair
                47, 48 -> BodyPartColor.Torso
                51, 52 -> BodyPartColor.Legs
                55, 56 -> BodyPartColor.Feet
                59, 60 -> BodyPartColor.Skin
                else -> null
            }?.let { bodyPartColor -> appearance.changeBodyColor(bodyPartColor, backwards) }
            65, 66 -> {
                val nextGender = if (backwards) Gender.Male else Gender.Female
                if (nextGender == appearance.gender) return@onClick
                appearance.bodyParts.forEach { parts ->
                    val bodyPart = parts.key
                    val nextKits = getKitsFromBodyPart(bodyPart, nextGender)
                    val current = getKitsFromBodyPart(bodyPart, appearance.gender).indexOf(parts.value)
                    appearance.bodyParts[bodyPart] = if (current != -1 && nextKits.isNotEmpty()) {
                        if (current < nextKits.size) nextKits[current] else nextKits[0]
                    } else parts.value
                }
                appearance.gender = nextGender
                vars.flip { VarBit.AppearanceDesignerGenderSelect }
            }
            68 -> {
                interfaces.closeModal()
                brandNew = false
            }
        }
        renderAppearance()
    }
}

onEvent<Events.OnLoginEvent> {
    if (player.brandNew) {
        player.interfaces += PlayerAppearanceDesigner
    }
}

fun Render.Appearance.changeBodyPart(bodyPart: BodyPart, backwards: Boolean) {
    val kits = getKitsFromBodyPart(bodyPart, gender)
    val current = kits.indexOf(bodyParts[bodyPart]!!)
    bodyParts[bodyPart] = when {
        backwards && current - 1 < 0 -> kits.last()
        !backwards && current + 1 >= kits.size -> kits.first()
        else -> if (backwards) kits[current - 1] else kits[current + 1]
    }
}

fun Render.Appearance.changeBodyColor(bodyPartColor: BodyPartColor, backwards: Boolean) {
    val current = bodyPartColors[bodyPartColor]!!
    bodyPartColors[bodyPartColor] = when {
        backwards && current - 1 < 0 -> bodyPartColor.colorLength
        !backwards && current + 1 >= bodyPartColor.colorLength -> 0
        else -> if (backwards) current - 1 else current + 1
    }
}

fun getKitsFromBodyPart(bodyPart: BodyPart, gender: Gender) = kits
    .entries()
    .filter { !it.nonSelectable }
    .filter { it.bodyPartId == bodyPart.packBodyPart(gender) }
    .map(KitEntryType::id)
    .toIntArray()
