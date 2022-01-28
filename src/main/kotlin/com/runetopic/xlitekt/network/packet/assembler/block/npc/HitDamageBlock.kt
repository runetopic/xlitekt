package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.HitType
import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeByteAdd
import com.runetopic.xlitekt.util.ext.writeByteNegate
import com.runetopic.xlitekt.util.ext.writeSmart
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class HitDamageBlock : RenderingBlock<NPC, Render.HitDamage>(2, 0x1) {
    override fun build(actor: NPC, render: Render.HitDamage) = buildPacket {
        writeByteAdd(actor.nextHits.size.toByte())

        actor.nextHits.forEach {
            val type = it.type
            val isInteracting = it.isInteracting(actor, it.source)
            val showTintedHitSplats = type.isTinted()

            writeSmart(if (!isInteracting && showTintedHitSplats) type.id + 1 else type.id)
            writeSmart(it.damage)
            writeSmart(it.delay)
        }

        writeByteNegate(actor.nextHitBars.size.toByte())

        actor.nextHitBars.forEach {
            writeSmart(it.id)
            writeSmart(0) // dunno yet
            writeSmart(0)
            writeByteAdd(getPercentage(actor).toByte())
        }
    }

    private fun getPercentage(e: Actor): Int {
        val maxHitPoints = e.totalHitpoints()
        val current = e.currentHitpoints().coerceAtMost(maxHitPoints)
        var percentage = if (maxHitPoints == 0) 0 else current * 30 /*HitBarDefProvider.lookup(id).getHealthScale()*/ / maxHitPoints
        if (percentage == 0 && current > 0) {
            percentage = 1
        }
        return percentage
    }

    private fun HitType.isTinted(): Boolean = this != HitType.VENOM_DAMAGE && this != HitType.POISON_DAMAGE && this != HitType.HEAL
    private fun Render.HitDamage.isInteracting(actor: Actor, target: Actor?): Boolean = source == actor || actor == target
}
