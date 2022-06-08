package xlitekt.shared.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EquipmentSlot {
    @SerialName("")
    NONE,
    @SerialName("2h")
    TWO_HAND,
    @SerialName("weapon")
    WEAPON,
    @SerialName("head")
    HEAD,
    @SerialName("body")
    BODY,
    @SerialName("ammo")
    AMMO,
    @SerialName("neck")
    NECK,
    @SerialName("feet")
    FEET,
    @SerialName("legs")
    LEGS,
    @SerialName("ring")
    RING,
    @SerialName("hands")
    HANDS,
    @SerialName("cape")
    CAPE,
    @SerialName("shield")
    SHIELD
}

@Serializable
data class EquipmentInfoResource(
    @SerialName("attackStab")
    val attackStab: Int,
    @SerialName("attackSlash")
    val attackSlash: Int,
    @SerialName("attackCrush")
    val attackCrush: Int,
    @SerialName("attackMagic")
    val attackMagic: Int,
    @SerialName("attackRanged")
    val attackRanged: Int,
    @SerialName("defenceStab")
    val defenceStab: Int,
    @SerialName("defenceSlash")
    val defenceSlash: Int,
    @SerialName("defenceCrush")
    val defenceCrush: Int,
    @SerialName("defenceMagic")
    val defenceMagic: Int,
    @SerialName("defenceRanged")
    val defenceRanged: Int,
    @SerialName("strengthBonus")
    val meleeStrength: Int,
    @SerialName("rangedStrength")
    val rangedStrength: Int,
    @SerialName("magicDamage")
    val magicDamage: Double,
    @SerialName("prayer")
    val prayer: Int,
    @SerialName("equipmentSlot")
    val equipmentSlot: EquipmentSlot,
    @SerialName("attackSpeed")
    val attackSpeed: Int? = null,
    @SerialName("attackRange")
    val attackRange: Int? = null,
    @SerialName("hideArms")
    val hideArms: Boolean? = false,
    @SerialName("hideHair")
    val hideHair: Boolean? = false,
    @SerialName("showBeard")
    val showBeard: Boolean? = false
)

@Serializable
data class ItemInfoResource(
    @SerialName("itemId")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("equipable")
    val equipable: Boolean = false,
    @SerialName("weight")
    val weight: Double = 0.0,
    @SerialName("equipment")
    val equipment: EquipmentInfoResource? = null
)
