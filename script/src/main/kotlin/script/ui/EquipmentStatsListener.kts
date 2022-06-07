package script.ui

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.script
import xlitekt.game.content.container.inventory.INVENTORY_CONTAINER_KEY
import xlitekt.game.content.ui.InterfaceEvent
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.UserInterface.EquipmentStats
import xlitekt.game.content.ui.UserInterfaceListener
import xlitekt.game.content.ui.onInterface

/**
 * @author Tyler Telis
 */
onInterface<EquipmentStats> { player ->
    onCreate {
        script(917, -1, -1)
    }

    onOpen {
        interfaces += UserInterface.EquipmentInventory
        sendEquipmentBonusInfo(this@onInterface, player)
    }

    onClose {
        interfaces.closeInventory()
    }
}

fun sendEquipmentBonusInfo(userInterfaceListener: UserInterfaceListener, player: Player) {
    fun formatBonus(input: Number) = if (input.toDouble() >= 0.0) "+$input" else "$input"

    userInterfaceListener.setText(24, "Stab: ${formatBonus(player.bonuses.attackStab)}")
    userInterfaceListener.setText(25, "Slash: ${formatBonus(player.bonuses.attackSlash)}")
    userInterfaceListener.setText(26, "Crush: ${formatBonus(player.bonuses.attackCrush)}")
    userInterfaceListener.setText(27, "Magic: ${formatBonus(player.bonuses.attackMagic)}")
    userInterfaceListener.setText(27, "Range: ${formatBonus(player.bonuses.attackRanged)}")

    userInterfaceListener.setText(30, "Stab: ${formatBonus(player.bonuses.defenceStab)}")
    userInterfaceListener.setText(31, "Slash: ${formatBonus(player.bonuses.defenceSlash)}")
    userInterfaceListener.setText(32, "Crush: ${formatBonus(player.bonuses.defenceCrush)}")
    userInterfaceListener.setText(33, "Magic: ${formatBonus(player.bonuses.defenceMagic)}")
    userInterfaceListener.setText(34, "Range: ${formatBonus(player.bonuses.defenceRanged)}")

    userInterfaceListener.setText(36, "Melee strength: ${formatBonus(player.bonuses.meleeStrength)}")
    userInterfaceListener.setText(37, "Ranged strength: ${formatBonus(player.bonuses.rangedStrength)}")
    userInterfaceListener.setText(37, "Magic damage: ${formatBonus(player.bonuses.magicDamage)}%")
    userInterfaceListener.setText(38, "Prayer: ${formatBonus(player.bonuses.prayer)}")

    userInterfaceListener.setText(41, "Undead: ${formatBonus(player.bonuses.undead.toInt())}%") // Displaying integer versions of this
    userInterfaceListener.setText(42, "Slayer: ${formatBonus(player.bonuses.slayer)}")
}
