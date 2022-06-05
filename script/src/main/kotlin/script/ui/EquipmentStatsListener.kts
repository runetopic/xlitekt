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
        fun formatBonus(input: Number) = if (input.toDouble() >= 0.0) "+$input" else "$input"

        setText(24, "Stab: ${formatBonus(bonuses.attackStab)}")
        setText(25, "Slash: ${formatBonus(bonuses.attackSlash)}")
        setText(26, "Crush: ${formatBonus(bonuses.attackCrush)}")
        setText(27, "Magic: ${formatBonus(bonuses.attackMagic)}")
        setText(27, "Range: ${formatBonus(bonuses.attackRanged)}")

        setText(30, "Stab: ${formatBonus(bonuses.defenceStab)}")
        setText(31, "Slash: ${formatBonus(bonuses.defenceSlash)}")
        setText(32, "Crush: ${formatBonus(bonuses.defenceCrush)}")
        setText(33, "Magic: ${formatBonus(bonuses.defenceMagic)}")
        setText(34, "Range: ${formatBonus(bonuses.defenceRanged)}")

        setText(36, "Melee strength: ${formatBonus(bonuses.meleeStrength)}")
        setText(37, "Ranged strength: ${formatBonus(bonuses.rangedStrength)}")
        setText(37, "Magic damage: ${formatBonus(bonuses.magicDamage)}%")
        setText(38, "Prayer: ${formatBonus(bonuses.prayer)}")

        setText(41, "Undead: ${formatBonus(bonuses.undead.toInt())}%") // Displaying integer versions of this
        setText(42, "Slayer: ${formatBonus(bonuses.slayer)}")
        interfaces += UserInterface.EquipmentInventory
    }

    onClose {
        interfaces.closeInventory()
    }
}
