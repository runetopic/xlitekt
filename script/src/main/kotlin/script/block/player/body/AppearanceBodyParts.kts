package script.block.player.body

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
onBodyPart(index = Equipment.SLOT_HEAD) {
    bodyPart {
        if (equipment.head != null) {
            writeShort { 0x200 + equipment.head!!.id }
        } else {
            writeByte { kit }
        }
    }
}

/**
 * The back.
 */
onBodyPart(index = Equipment.SLOT_BACK) {
    bodyPart {
        if (equipment.back != null) {
            writeShort { 0x200 + equipment.back!!.id }
        } else {
            writeByte { kit }
        }
    }
}

/**
 * The neck.
 */
onBodyPart(index = Equipment.SLOT_NECK) {
    bodyPart {
        if (equipment.neck != null) {
            writeShort { 0x200 + equipment.neck!!.id }
        } else {
            writeByte { kit }
        }
    }
}

/**
 * The main-hand.
 */
onBodyPart(index = Equipment.SLOT_MAINHAND) {
    bodyPart {
        if (equipment.mainhand != null) {
            writeShort { 0x200 + equipment.mainhand!!.id }
        } else {
            writeByte { kit }
        }
    }
}

/**
 * The torso.
 */
onBodyPart(index = Equipment.SLOT_TORSO, BodyPart.TORSO) {
    bodyPart {
        if (equipment.torso != null) {
            writeShort { 0x200 + equipment.torso!!.id }
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The off-hand.
 */
onBodyPart(index = Equipment.SLOT_OFFHAND) {
    bodyPart {
        if (equipment.offhand != null) {
            writeShort { 0x200 + equipment.offhand!!.id }
        } else {
            writeByte { kit }
        }
    }
}

/**
 * The arms.
 */
onBodyPart(index = 6, BodyPart.ARMS) {
    bodyPart {
        if (equipment.torso != null) {
            writeByte { 0 } // Hide arms.
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The legs.
 */
onBodyPart(index = Equipment.SLOT_LEGS, BodyPart.LEGS) {
    bodyPart {
        if (equipment.legs != null) {
            writeShort { 0x200 + equipment.legs!!.id }
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The hair.
 */
onBodyPart(index = 8, BodyPart.HEAD) {
    bodyPart {
        if (equipment.head != null) {
            writeByte { 0 } // Hide hair.
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The hands.
 */
onBodyPart(index = Equipment.SLOT_HANDS, BodyPart.HANDS) {
    bodyPart {
        if (equipment.hands != null) {
            writeShort { 0x200 + equipment.hands!!.id }
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The feet.
 */
onBodyPart(index = Equipment.SLOT_FEET, BodyPart.FEET) {
    bodyPart {
        if (equipment.feet != null) {
            writeShort { 0x200 + equipment.feet!!.id }
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The jaw.
 */
onBodyPart(index = 11, BodyPart.JAW) {
    bodyPart {
        val slot = if (gender == Gender.MALE) Equipment.SLOT_HEAD else Equipment.SLOT_TORSO
        when (gender) {
            Gender.MALE -> if (equipment[slot] != null) {
                writeByte { 0 } // Hide beard.
            } else {
                writeShort { 0x100 + kit }
            }
            else -> writeByte(0)
        }
    }
}
