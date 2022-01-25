package com.runetopic.xlitekt.network.packet.assembler.block.player.kit

import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.impl.* // ktlint-disable no-unused-imports

enum class PlayerIdentityKit(
    val info: BodyPartInfo,
    val bodyPart: BodyPart? = null
) {
    HAIR(HairInfo(), BodyPart.HEAD),
    JAW(JawInfo(), BodyPart.JAW),
    TORSO(TorsoInfo(), BodyPart.TORSO),
    ARMS(ArmInfo(), BodyPart.ARMS),
    HANDS(HandInfo(), BodyPart.HANDS),
    LEGS(LegInfo(), BodyPart.LEGS),
    FEET(FootInfo(), BodyPart.FEET),
    SHIELD(ShieldInfo()),
    HEAD(HeadInfo()),
    CAPE(CapeInfo()),
    NECK(NeckInfo()),
    WEAPON(WeaponInfo());
}
