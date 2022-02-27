package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.Actor
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.npc.NPCCustomLevelBlock
import xlitekt.game.actor.render.block.npc.NPCFaceActorBlock
import xlitekt.game.actor.render.block.npc.NPCFaceTileBlock
import xlitekt.game.actor.render.block.npc.NPCForceMovementBlock
import xlitekt.game.actor.render.block.npc.NPCHitDamageBlock
import xlitekt.game.actor.render.block.npc.NPCOverheadChatBlock
import xlitekt.game.actor.render.block.npc.NPCRecolorBlock
import xlitekt.game.actor.render.block.npc.NPCSequenceBlock
import xlitekt.game.actor.render.block.npc.NPCSpotAnimationBlock
import xlitekt.game.actor.render.block.npc.NPCTransmogrificationBlock
import xlitekt.game.actor.render.block.player.PlayerAppearanceBlock
import xlitekt.game.actor.render.block.player.PlayerFaceActorBlock
import xlitekt.game.actor.render.block.player.PlayerFaceDirectionBlock
import xlitekt.game.actor.render.block.player.PlayerForceMovementBlock
import xlitekt.game.actor.render.block.player.PlayerHitDamageBlock
import xlitekt.game.actor.render.block.player.PlayerMovementTypeBlock
import xlitekt.game.actor.render.block.player.PlayerOverheadChatBlock
import xlitekt.game.actor.render.block.player.PlayerPublicChatBlock
import xlitekt.game.actor.render.block.player.PlayerRecolorBlock
import xlitekt.game.actor.render.block.player.PlayerSequenceBlock
import xlitekt.game.actor.render.block.player.PlayerSpotAnimationBlock
import xlitekt.game.actor.render.block.player.PlayerTemporaryMovementTypeBlock
import xlitekt.game.actor.render.block.player.PlayerUsernameOverrideBlock

val playerBlocks = mapOf(
    Render.Appearance::class to PlayerAppearanceBlock(),
    Render.Sequence::class to PlayerSequenceBlock(),
    Render.UsernameOverride::class to PlayerUsernameOverrideBlock(),
    Render.FaceActor::class to PlayerFaceActorBlock(),
    Render.FaceDirection::class to PlayerFaceDirectionBlock(),
    Render.MovementType::class to PlayerMovementTypeBlock(),
    Render.ForceMovement::class to PlayerForceMovementBlock(),
    Render.HitDamage::class to PlayerHitDamageBlock(),
    Render.OverheadChat::class to PlayerOverheadChatBlock(),
    Render.PublicChat::class to PlayerPublicChatBlock(),
    Render.Recolor::class to PlayerRecolorBlock(),
    Render.SpotAnimation::class to PlayerSpotAnimationBlock(),
    Render.TemporaryMovementType::class to PlayerTemporaryMovementTypeBlock()
)

val npcBlocks = mapOf(
    Render.Sequence::class to NPCSequenceBlock(),
    Render.NPCCustomLevel::class to NPCCustomLevelBlock(),
    Render.FaceActor::class to NPCFaceActorBlock(),
    Render.FaceTile::class to NPCFaceTileBlock(),
    Render.ForceMovement::class to NPCForceMovementBlock(),
    Render.HitDamage::class to NPCHitDamageBlock(),
    Render.Recolor::class to NPCRecolorBlock(),
    Render.OverheadChat::class to NPCOverheadChatBlock(),
    Render.SpotAnimation::class to NPCSpotAnimationBlock(),
    Render.NPCTransmogrification::class to NPCTransmogrificationBlock()
)

fun List<Render>.buildPlayerUpdateBlocks(player: Player) = buildPacket {
    val blocks = this@buildPlayerUpdateBlocks.map { it to playerBlocks[it::class]!! }.sortedBy { it.second.index }.toMap()
    val mask = blocks.map { it.value.mask }.sum().let { if (it > 0xff) it or 0x10 else it }
    if (mask > 0xff) writeShortLittleEndian(mask.toShort()) else writeByte(mask.toByte())
    blocks.forEach { writePacket(it.value.build(player, it.key)) }
}

fun BytePacketBuilder.buildNPCUpdateBlocks(npc: NPC) {
    val blocks = npc.pendingUpdates().map { it to npcBlocks[it::class]!! }.sortedBy { it.second.index }.toMap()
    val mask = blocks.map { it.value.mask }.sum().let { if (it > 0xff) it or 0x4 else it }
    if (mask > 0xff) writeShortLittleEndian(mask.toShort()) else writeByte(mask.toByte())
    blocks.forEach { writePacket(it.value.build(npc, it.key)) }
}

abstract class RenderingBlock<T : Actor, out R : Render>(val index: Int, val mask: Int) {
    abstract fun build(actor: T, render: @UnsafeVariance R): ByteReadPacket
}
