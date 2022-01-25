package com.runetopic.xlitekt.network.packet.assembler.block

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.render.Render
import io.ktor.utils.io.core.ByteReadPacket

abstract class RenderingBlock<T : Actor, out R : Render>(val index: Int, val mask: Int) {
    abstract fun build(actor: T, render: @UnsafeVariance R): ByteReadPacket
}
