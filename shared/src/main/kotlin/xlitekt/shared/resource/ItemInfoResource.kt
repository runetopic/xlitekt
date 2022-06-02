package xlitekt.shared.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EquipmentSlot {
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
data class ItemInfoResource(
    @SerialName("itemId")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("equipable")
    val equipable: Boolean,
    @SerialName("destroyOption")
    val destroyOption: String,
    @SerialName("examine")
    val examine: String,
    @SerialName("weight")
    val weight: Double,
    @SerialName("attack_stab")
    val attackStab: Int,
    @SerialName("attack_slash")
    val attackSlash: Int,
    @SerialName("attack_crush")
    val attackCrush: Int,
    @SerialName("attack_magic")
    val attackMagic: Int,
    @SerialName("attack_ranged")
    val attackRanged: Int,
    @SerialName("defence_stab")
    val defenceStab: Int,
    @SerialName("defence_slash")
    val defenceSlash: Int,
    @SerialName("defence_crush")
    val defenceCrush: Int,
    @SerialName("defence_magic")
    val defenceMagic: Int,
    @SerialName("defence_ranged")
    val defenceRanged: Int,
    @SerialName("melee_strength")
    val meleeStrength: Int,
    @SerialName("magic_damage")
    val magicDamage: Int,
    @SerialName("prayer")
    val prayer: Int,
    @SerialName("equipmentSlot")
    val equipmentSlot: EquipmentSlot,
    @SerialName("attackSpeed")
    val attackSpeed: Int
)
