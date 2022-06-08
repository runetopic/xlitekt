package script.block.player.body

import xlitekt.game.actor.render.Render.Appearance.Gender
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.onBodyPart
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemInfoMap

/**
 * @author Jordan Abraham
 */

val itemInfoMap by inject<ItemInfoMap>()

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
onBodyPart(index = Equipment.SLOT_TORSO, BodyPart.Torso) {
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
onBodyPart(index = 6, BodyPart.Arms) {
    bodyPart {
        val torso = equipment.torso ?: return@bodyPart writeShort { 0x100 + kit }
        val info = itemInfoMap[torso.id]?.equipment ?: return@bodyPart writeShort { 0x100 + kit }

        if (info.hideArms == true) {
            writeByte { 0 } // Hide arms.
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The legs.
 */
onBodyPart(index = Equipment.SLOT_LEGS, BodyPart.Legs) {
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
onBodyPart(index = 8, BodyPart.Head) {
    bodyPart {
        val head = equipment.head ?: return@bodyPart writeShort { 0x100 + kit }
        val itemInfo = itemInfoMap[head.id]?.equipment ?: return@bodyPart writeByte { 0 } // Hide hair.

        if (itemInfo.hideHair == true) {
            writeByte { 0 } // Hide hair.
        } else {
            writeShort { 0x100 + kit }
        }
    }
}

/**
 * The hands.
 */
onBodyPart(index = Equipment.SLOT_HANDS, BodyPart.Hands) {
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
onBodyPart(index = Equipment.SLOT_FEET, BodyPart.Feet) {
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
onBodyPart(index = 11, BodyPart.Jaw) {
    bodyPart {
        val slot = if (gender == Gender.Male) Equipment.SLOT_HEAD else Equipment.SLOT_TORSO

        when (gender) {
            Gender.Male -> if (equipment[slot] != null) {
                val head = equipment.head ?: return@bodyPart writeShort { 0x100 + kit }
                val itemInfo = itemInfoMap[head.id]?.equipment ?: return@bodyPart writeByte { 0 } // Hide hair.

                if (itemInfo.showBeard == false) {
                    writeByte { 0 } // Hide beard.
                } else {
                    writeShort { 0x100 + kit }
                }
            }
            else -> writeByte(0)
        }
    }
}
