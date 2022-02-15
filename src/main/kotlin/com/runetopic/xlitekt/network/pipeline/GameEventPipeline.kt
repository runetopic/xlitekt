package com.runetopic.xlitekt.network.pipeline

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.packet.RegisteredPackets
import com.runetopic.xlitekt.plugin.koin.inject
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
    private val sizes = environment.config.property("game.packet.sizes").getList().map(String::toInt)
    private val timeout = environment.config.property("network.timeout").getString().toLong()
    private val logger = InlineLogger()

    init {
        logger.debug { "Loaded ${RegisteredPackets.assemblers.size} packet assemblers." }
        logger.debug { "Loaded ${RegisteredPackets.disassemblers.size} packet disassemblers." }
        logger.debug { "Loaded ${RegisteredPackets.handlers.size} packet disassembler handlers." }
    }

    override suspend fun read(client: Client): ReadEvent.GameReadEvent? {
        if (client.readChannel.availableForRead <= 0) {
            withTimeout(timeout) { client.readChannel.awaitContent() }
        }
        val opcode = client.readChannel.readPacketOpcode(client.clientCipher!!)
        if (opcode < 0 || opcode >= sizes.size) return null
        val size = client.readChannel.readPacketSize(sizes[opcode])
        logger.debug { "Read Packet with opcode=$opcode and size=$size" }
        return ReadEvent.GameReadEvent(opcode, size, client.readChannel.readPacket(size))
    }

    override suspend fun write(client: Client, event: WriteEvent.GameWriteEvent) {
        client.writeChannel.apply {
            writePacketOpcode(client.serverCipher!!, event.opcode)
            if (event.size == -1 || event.size == -2) {
                writePacketSize(event.size, event.payload.remaining)
            }
            writePacket(event.payload)
        }.flush()
    }
}
