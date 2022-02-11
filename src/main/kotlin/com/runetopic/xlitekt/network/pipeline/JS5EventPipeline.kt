package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_ENCRYPTION_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_HIGH_PRIORITY_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_LOGGED_IN_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_LOGGED_OUT_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_LOW_PRIORITY_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.coroutines.withTimeout

/**
 * @author Jordan Abraham
 */
class JS5EventPipeline : EventPipeline<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    private val environment by inject<ApplicationEnvironment>()
    private val timeout = environment.config.property("network.timeout").getString().toLong()

    override suspend fun read(client: Client): ReadEvent.JS5ReadEvent {
        if (client.readChannel.availableForRead < 4) {
            withTimeout(timeout) { client.readChannel.awaitContent() }
        }
        val opcode = client.readChannel.readByte().toInt()
        val indexId = client.readChannel.readByte().toInt() and 0xff
        val groupId = client.readChannel.readShort().toInt() and 0xffff
        return when (opcode) {
            JS5_HIGH_PRIORITY_OPCODE, JS5_LOW_PRIORITY_OPCODE, JS5_LOGGED_IN_OPCODE, JS5_LOGGED_OUT_OPCODE, JS5_ENCRYPTION_OPCODE -> ReadEvent.JS5ReadEvent(
                opcode,
                indexId,
                groupId
            )
            else -> throw IllegalStateException("Unhandled Js5 opcode. Opcode=$opcode")
        }
    }

    override suspend fun write(client: Client, event: WriteEvent.JS5WriteEvent) {
        if (event.bytes.capacity() == 0 || event.bytes.limit() == 0) return

        val compression = event.compression
        val size = event.size

        client.writeChannel.apply {
            writeByte(event.indexId.toByte())
            writeShort(event.groupId.toShort())
            writeByte(compression.toByte())
            writeInt(size)

            var writeOffset = 8
            repeat(if (compression != 0) size + 4 else size) {
                if (writeOffset % 512 == 0) {
                    writeByte(0xff.toByte())
                    writeOffset = 1
                }
                writeByte(event.bytes[it + event.bytes.position()])
                writeOffset++
            }
            flush()
        }
    }
}
