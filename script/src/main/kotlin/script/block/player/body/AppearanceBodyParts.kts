package script.block.player.body

import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.Render.Appearance.Gender
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.onBodyPart
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */

/**
 * The head.
 */
onBodyPart(index = 0) {
    equipmentSlot {
        Equipment.SLOT_HEAD
    }

    bodyPart {
        writeByte { kit }
    }
}

/**
 * The back.
 */
onBodyPart(index = 1) {
    equipmentSlot {
        Equipment.SLOT_BACK
    }

    bodyPart {
        writeByte { it }
    }
}

/**
 * The neck.
 */
onBodyPart(index = 2) {
    equipmentSlot {
        Equipment.SLOT_NECK
    }

    bodyPart {
        writeByte { kit }
    }
}

/**
 * The main-hand.
 */
onBodyPart(index = 3) {
    equipmentSlot {
        Equipment.SLOT_MAINHAND
    }

    bodyPart {
        writeByte { kit }
    }
}

/**
 * The torso.
 */
onBodyPart(index = 4, BodyPart.TORSO) {
    equipmentSlot {
        Equipment.SLOT_TORSO
    }

    bodyPart {
        writeShort { 0x100 + kit }
    }
}

/**
 * The off-hand.
 */
onBodyPart(index = 5) {
    equipmentSlot {
        Equipment.SLOT_OFFHAND
    }

    bodyPart {
        writeByte { kit }
    }
}

/**
 * The arms.
 */
onBodyPart(index = 6, BodyPart.ARMS) {
    equipmentSlot {
        Equipment.SLOT_TORSO
    }

    bodyPart {
        writeShort { 0x100 + kit }
    }
}

/**
 * The legs.
 */
onBodyPart(index = 7, BodyPart.LEGS) {
    equipmentSlot {
        Equipment.SLOT_LEG
    }

    bodyPart {
        writeShort { 0x100 + kit }
    }
}

/**
 * The hair.
 */
onBodyPart(index = 8, BodyPart.HEAD) {
    equipmentSlot {
        Equipment.SLOT_HEAD
    }

    bodyPart {
        writeShort { 0x100 + kit }
    }
}

/**
 * The hands.
 */
onBodyPart(index = 9, BodyPart.HANDS) {
    equipmentSlot {
        Equipment.SLOT_HANDS
    }

    bodyPart {
        writeShort { 0x100 + kit }
    }
}

/**
 * The feet.
 */
onBodyPart(index = 10, BodyPart.FEET) {
    equipmentSlot {
        Equipment.SLOT_FEET
    }

    bodyPart {
        writeShort { 0x100 + kit }
    }
}

/**
 * The jaw.
 */
onBodyPart(index = 11, BodyPart.JAW) {
    equipmentSlot {
        if (gender == Gender.MALE) Equipment.SLOT_HEAD else Equipment.SLOT_TORSO
    }

    bodyPart {
        when (gender) {
            Gender.MALE -> writeShort { 0x100 + kit }
            else -> writeByte(0)
        }
    }
}
