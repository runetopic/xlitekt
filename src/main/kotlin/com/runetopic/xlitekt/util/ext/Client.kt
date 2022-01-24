package com.runetopic.xlitekt.util.ext

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.WriteEvent
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShort

suspend fun Client.sendRebuildNormalMap() {
    val builder = BytePacketBuilder()

    builder.writeShort(302)
    builder.writeShort(302)
    eventPipeline.write(this, WriteEvent.GameWriteEvent(54, -2, builder.build()))
}
