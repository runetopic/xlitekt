package com.runetopic.xlitekt.network.pipeline

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.ext.readPacketOpcode
import com.runetopic.xlitekt.util.ext.readPacketSize
import com.runetopic.xlitekt.util.ext.writePacketOpcode
import com.runetopic.xlitekt.util.ext.writePacketSize
import io.ktor.application.ApplicationEnvironment
import io.ktor.utils.io.readPacket
import kotlinx.coroutines.withTimeout

/**
 * @author Jordan Abraham
 */
class GameEventPipeline : EventPipeline<ReadEvent.GameReadEvent, WriteEvent.GameWriteEvent> {

    private val environment by inject<ApplicationEnvironment>()
    private val sizes = environment.config.property("game.packet.sizes").getList().map { it.toInt() }
    private val timeout = environment.config.property("network.timeout").getString().toLong()
    private val logger = InlineLogger()

    override suspend fun read(client: Client): ReadEvent.GameReadEvent? {
        if (client.readChannel.availableForRead <= 0) {
            withTimeout(timeout) { client.readChannel.awaitContent() }
        }
        val opcode = client.readChannel.readPacketOpcode(client.clientCipher!!)
        if (opcode < 0 || opcode >= sizes.size) return null
        val size = client.readChannel.readPacketSize(sizes[opcode])

        if (opcode != 12)
            logger.debug { "Read Packet with opcode=$opcode and size=$size" }
        return ReadEvent.GameReadEvent(opcode, size, client.readChannel.readPacket(size))
    }

    override suspend fun write(client: Client, event: WriteEvent.GameWriteEvent) {
        client.writeChannel.let {
            it.writePacketOpcode(client.serverCipher!!, event.opcode)
            it.writePacketSize(event.size, event.payload.remaining)
            it.writePacket(event.payload)
            it.flush()
        }
        event.payload.release()
    }
}
