package script.block.player.body

import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.render.Render.Appearance.Gender
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.onBodyPart
import xlitekt.game.content.container.equipment.Equipment
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
            writeShort((0x200 + equipment.head!!.id).toShort())
        } else {
            writeByte(kit.toByte())
        }
    }
}

/**
 * The back.
 */
onBodyPart(index = Equipment.SLOT_BACK) {
    bodyPart {
        if (equipment.back != null) {
            writeShort((0x200 + equipment.back!!.id).toShort())
        } else {
            writeByte(kit.toByte())
        }
    }
}

/**
 * The neck.
 */
onBodyPart(index = Equipment.SLOT_NECK) {
    bodyPart {
        if (equipment.neck != null) {
            writeShort((0x200 + equipment.neck!!.id).toShort())
        } else {
            writeByte(kit.toByte())
        }
    }
}

/**
 * The main-hand.
 */
onBodyPart(index = Equipment.SLOT_MAINHAND) {
    bodyPart {
        if (equipment.mainhand != null) {
            writeShort((0x200 + equipment.mainhand!!.id).toShort())
        } else {
            writeByte(kit.toByte())
        }
    }
}

/**
 * The torso.
 */
onBodyPart(index = Equipment.SLOT_TORSO, BodyPart.Torso) {
    bodyPart {
        if (equipment.torso != null) {
            writeShort((0x200 + equipment.torso!!.id).toShort())
        } else {
            writeShort((0x100 + kit).toShort())
        }
    }
}

/**
 * The off-hand.
 */
onBodyPart(index = Equipment.SLOT_OFFHAND) {
    bodyPart {
        if (equipment.offhand != null) {
            writeShort((0x200 + equipment.offhand!!.id).toShort())
        } else {
            writeByte(kit.toByte())
        }
    }
}

/**
 * The arms.
 */
onBodyPart(index = 6, BodyPart.Arms) {
    bodyPart {
        if (equipment.torso == null) {
            writeShort((0x100 + kit).toShort())
            return@bodyPart
        }
        val torso = equipment.torso!!
        if (itemInfoMap[torso.id]?.equipment == null) {
            writeShort((0x100 + kit).toShort())
            return@bodyPart
        }
        val itemInfo = itemInfoMap[torso.id]?.equipment!!

        if (itemInfo.hideArms == true) {
            writeByte(0) // Hide arms.
        } else {
            writeShort((0x100 + kit).toShort())
        }
    }
}

/**
 * The legs.
 */
onBodyPart(index = Equipment.SLOT_LEGS, BodyPart.Legs) {
    bodyPart {
        if (equipment.legs != null) {
            writeShort((0x200 + equipment.legs!!.id).toShort())
        } else {
            writeShort((0x100 + kit).toShort())
        }
    }
}

/**
 * The hair.
 */
onBodyPart(index = 8, BodyPart.Head) {
    bodyPart {
        if (equipment.head == null) {
            writeShort((0x100 + kit).toShort())
            return@bodyPart
        }
        val head = equipment.head!!
        if (itemInfoMap[head.id]?.equipment == null) {
            writeShort((0x100 + kit).toShort())
            return@bodyPart
        }
        val itemInfo = itemInfoMap[head.id]?.equipment!!

        if (itemInfo.hideHair == true) {
            writeByte(0) // Hide hair.
        } else {
            writeShort((0x100 + kit).toShort())
        }
    }
}

/**
 * The hands.
 */
onBodyPart(index = Equipment.SLOT_HANDS, BodyPart.Hands) {
    bodyPart {
        if (equipment.hands != null) {
            writeShort((0x200 + equipment.hands!!.id).toShort())
        } else {
            writeShort((0x100 + kit).toShort())
        }
    }
}

/**
 * The feet.
 */
onBodyPart(index = Equipment.SLOT_FEET, BodyPart.Feet) {
    bodyPart {
        if (equipment.feet != null) {
            writeShort((0x200 + equipment.feet!!.id).toShort())
        } else {
            writeShort((0x100 + kit).toShort())
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
            Gender.Male -> {
                if (equipment.head == null) {
                    writeShort((0x100 + kit).toShort())
                    return@bodyPart
                }
                val head = equipment.head!!
                if (itemInfoMap[head.id]?.equipment == null) {
                    writeShort((0x100 + kit).toShort())
                    return@bodyPart
                }
                val itemInfo = itemInfoMap[head.id]?.equipment!!

                if (itemInfo.showBeard == false) {
                    writeByte(0) // Hide beard.
                } else {
                    writeShort((0x100 + kit).toShort())
                }
            }
            else -> writeByte(0)
        }
    }
}
