package com.runetopic.xlitekt.game.actor

enum class HitType(
    val id: Int
) {
    MISSED(12),
    REGULAR_DAMAGE(16),
    RANGE_DAMAGE(-1),
    MAGIC_DAMAGE(-1),
    POISON_DAMAGE(2),
    DISEASE(4),
    HEAL(6),
    VENOM_DAMAGE(5),
    NIGHTMARE_TEMPOROSS_HIT(18),
    ZALCANO_ARMOUR_HIT(20),
    CHARGE_DAMAGE_NIGHTMARE(22);
}
