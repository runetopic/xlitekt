package script.ui

import xlitekt.game.actor.player.script
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.UserInterface.EquipmentStats
import xlitekt.game.content.ui.onInterface

/**
 * @author Tyler Telis
 */
onInterface<EquipmentStats> {
    onCreate {
        script(917, -1, -1)
    }

    onOpen {
        fun formatBonus(input: Number): String = if (input.toDouble() >= 0.0) "+${input}" else "$input"

        val attackStab = bonuses.attackStab
        val attackSlash = bonuses.attackSlash
        val attackCrush = bonuses.attackCrush
        val attackMagic = bonuses.attackMagic
        val attackRanged = bonuses.attackRanged

        val defenceStab = bonuses.defenceStab
        val defenceSlash = bonuses.defenceSlash
        val defenceCrush = bonuses.defenceCrush
        val defenceMagic = bonuses.defenceMagic
        val defenceRanged = bonuses.defenceRanged

        val meleeStrength = bonuses.meleeStrength
        val rangedStrength = bonuses.rangedStrength
        val magicDamage = bonuses.magicDamage
        val prayer = bonuses.prayer

        val undead = bonuses.undead
        val slayer = bonuses.slayer

        setText(24, "Stab: ${formatBonus(attackStab)}")
        setText(25, "Slash: ${formatBonus(attackSlash)}")
        setText(26, "Crush: ${formatBonus(attackCrush)}")
        setText(27, "Magic: ${formatBonus(attackMagic)}")
        setText(27, "Range: ${formatBonus(attackRanged)}")

        setText(30, "Stab: ${formatBonus(defenceStab)}")
        setText(31, "Slash: ${formatBonus(defenceSlash)}")
        setText(32, "Crush: ${formatBonus(defenceCrush)}")
        setText(33, "Magic: ${formatBonus(defenceMagic)}")
        setText(34, "Range: ${formatBonus(defenceRanged)}")

        setText(36, "Melee strength: ${formatBonus(meleeStrength)}")
        setText(37, "Ranged strength: ${formatBonus(rangedStrength)}")
        setText(37, "Magic damage: ${formatBonus(magicDamage)}%")
        setText(38, "Prayer: ${formatBonus(prayer)}")

        // Displaying integer versions of these
        setText(41, "Undead: ${formatBonus(undead.toInt())}%")
        setText(42, "Slayer: ${formatBonus(slayer)}")
        interfaces += UserInterface.EquipmentInventory
    }

    onClose {
        interfaces.closeInventory()
    }
}
