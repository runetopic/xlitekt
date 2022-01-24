package com.runetopic.xlitekt.network.packet.write.block

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.render.Render
import io.ktor.utils.io.core.ByteReadPacket

interface RenderingBlock<T : Actor, R : Render> {
    fun keyPair(): Pair<Int, Int>
    fun build(actor: T, render: R): ByteReadPacket
}
