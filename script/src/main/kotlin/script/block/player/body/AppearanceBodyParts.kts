package script.block.player.body

import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.render.Render.Appearance.Gender
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.BodyPartBlockListener
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.shared.inject
import xlitekt.shared.insert
import xlitekt.shared.resource.ItemInfoMap

/**
 * @author Jordan Abraham
 */

val itemInfoMap by inject<ItemInfoMap>()

/**
 * The head.
 */
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_HEAD) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_BACK) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_NECK) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_MAINHAND) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_TORSO, BodyPart.Torso) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_OFFHAND) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = 6, BodyPart.Arms) {
    bodyPart {
        itemInfoMap[equipment.torso?.id]?.equipment?.let {
            if (it.hideArms == true) {
                writeByte(0) // Hide arms.
            } else writeShort((0x100 + kit).toShort())
        } ?: writeShort((0x100 + kit).toShort())
    }
}

/**
 * The legs.
 */
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_LEGS, BodyPart.Legs) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = 8, BodyPart.Head) {
    bodyPart {
        itemInfoMap[equipment.head?.id]?.equipment?.let {
            if (it.hideHair == true) {
                writeByte(0) // Hide hair.
            } else writeShort((0x100 + kit).toShort())
        } ?: writeShort((0x100 + kit).toShort())
    }
}

/**
 * The hands.
 */
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_HANDS, BodyPart.Hands) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = Equipment.SLOT_FEET, BodyPart.Feet) {
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
insert<BodyPartBlockListener>().bodyPartBlock(index = 11, BodyPart.Jaw) {
    bodyPart {
        when (gender) {
            Gender.Male -> itemInfoMap[equipment.head?.id]?.equipment?.let {
                if (it.showBeard == false) {
                    writeByte(0) // Hide beard.
                } else writeShort((0x100 + kit).toShort())
            } ?: writeShort((0x100 + kit).toShort())
            else -> writeByte(0)
        }
    }
}
