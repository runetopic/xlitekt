package xlitekt.game.actor.bonus

import xlitekt.game.actor.bonus.Bonuses.Companion.itemInfoMap
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.shared.inject
import xlitekt.shared.resource.EquipmentInfoResource
import xlitekt.shared.resource.ItemInfoMap

/**
 * This class represents all the bonuses an Actor can have (E.g: Combat bonuses and prayer).
 * @author Tyler Telis
 */
class Bonuses {
    /**
     * Attack bonuses
     */
    var attackStab: Int = 0
    var attackSlash: Int = 0
    var attackCrush: Int = 0
    var attackMagic: Int = 0
    var attackRanged: Int = 0

    /**
     * Defence bonuses
     */
    var defenceStab: Int = 0
    var defenceSlash: Int = 0
    var defenceCrush: Int = 0
    var defenceMagic: Int = 0
    var defenceRanged: Int = 0

    /**
     * Other bonuses
     */
    var meleeStrength: Int = 0
    var rangedStrength: Int = 0
    var magicDamage: Double = 0.0
    var prayer: Int = 0

    var undead: Double = 0.0
    var slayer: Double = 0.0

    fun setDefaultBonuses() {
        attackStab = 0
        attackSlash = 0
        attackCrush = 0
        attackMagic = 0
        attackRanged = 0
        defenceStab = 0
        defenceSlash = 0
        defenceCrush = 0
        defenceMagic = 0
        defenceRanged = 0
        meleeStrength = 0
        rangedStrength = 0
        magicDamage = 0.0
        prayer = 0
    }

    /**
     * Calculates all of the player's worn equipment.
     */
    fun calculateEquippedBonuses(equipment: Equipment) {
        setDefaultBonuses()

        equipment.filterNotNull().forEach { item ->
            val equipmentInfo = itemInfoMap[item.id]?.equipment ?: return

            addAttackBonuses(equipmentInfo)
            addDefenceBonuses(equipmentInfo)
            addOtherBonuses(equipmentInfo)
        }
    }

    /**
     * Add all the attack bonuses up from the equipment provided.
     */
    private fun addAttackBonuses(equipmentInfo: EquipmentInfoResource) {
        attackStab += equipmentInfo.attackStab
        attackSlash += equipmentInfo.attackSlash
        attackCrush += equipmentInfo.attackCrush
        attackMagic += equipmentInfo.attackMagic
        attackRanged += equipmentInfo.attackRanged
    }

    /**
     * Add all the defence bonuses up from the equipment provided.
     */
    private fun addDefenceBonuses(equipmentInfo: EquipmentInfoResource) {
        defenceStab += equipmentInfo.defenceStab
        defenceSlash += equipmentInfo.defenceSlash
        defenceCrush += equipmentInfo.defenceCrush
        defenceMagic += equipmentInfo.defenceMagic
        defenceRanged += equipmentInfo.defenceRanged
    }

    /**
     * Add all the other bonuses up from the equipment provided.
     */
    private fun addOtherBonuses(equipmentInfo: EquipmentInfoResource) {
        meleeStrength += equipmentInfo.meleeStrength
        rangedStrength += equipmentInfo.rangedStrength
        magicDamage += equipmentInfo.magicDamage
        prayer += equipmentInfo.prayer
    }

    override fun toString(): String {
        return "Bonuses(attackStab=$attackStab, attackSlash=$attackSlash, attachCrush=$attackCrush, attackMagic=$attackMagic, attackRanged=$attackRanged,defenceStab=$defenceStab,defenceSlash=$defenceSlash,defenceCrush=$defenceCrush,defenceMagic=$defenceMagic,defenceRanged=$defenceRanged,meleeStrength=$meleeStrength,rangedStrength=$rangedStrength,magicDamage=$magicDamage,prayer=$prayer)"
    }

    private companion object {
        val itemInfoMap by inject<ItemInfoMap>()
    }
}
