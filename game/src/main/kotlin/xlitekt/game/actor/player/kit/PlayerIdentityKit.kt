package xlitekt.game.actor.player.kit

import xlitekt.game.actor.player.kit.impl.ArmInfo
import xlitekt.game.actor.player.kit.impl.CapeInfo
import xlitekt.game.actor.player.kit.impl.FootInfo
import xlitekt.game.actor.player.kit.impl.HairInfo
import xlitekt.game.actor.player.kit.impl.HandInfo
import xlitekt.game.actor.player.kit.impl.HeadInfo
import xlitekt.game.actor.player.kit.impl.JawInfo
import xlitekt.game.actor.player.kit.impl.LegInfo
import xlitekt.game.actor.player.kit.impl.NeckInfo
import xlitekt.game.actor.player.kit.impl.ShieldInfo
import xlitekt.game.actor.player.kit.impl.TorsoInfo
import xlitekt.game.actor.player.kit.impl.WeaponInfo

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
