package com.runetopic.xlitekt.network.pipeline

import IO_TIMEOUT
import com.runetopic.xlitekt.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import kotlinx.coroutines.withTimeout

class JS5EventPipeline : EventPipeline<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    override suspend fun read(client: Client): ReadEvent.JS5ReadEvent? {
        if (client.readChannel.availableForRead < 4) {
            withTimeout(IO_TIMEOUT) { client.readChannel.awaitContent() }
        }
        val opcode = client.readChannel.readByte().toInt()
        val indexId = client.readChannel.readByte().toInt() and 0xff
        val groupId = client.readChannel.readShort().toInt() and 0xffff
        return when (opcode) {
            0, 1, 2, 3, 4 -> ReadEvent.JS5ReadEvent(opcode, indexId, groupId)
            else -> null
        }
    }

    override suspend fun write(client: Client, event: WriteEvent.JS5WriteEvent) {
        if (event.bytes.capacity() == 0 || event.bytes.limit() == 0) return

        val indexId = event.indexId
        val groupId = event.groupId
        val compression = event.compression
        val size = event.size

        client.writeChannel.writeByte(indexId.toByte())
        client.writeChannel.writeShort(groupId.toShort())
        client.writeChannel.writeByte(compression.toByte())
        client.writeChannel.writeInt(size)

        var writeOffset = 8
        repeat(if (compression != 0) size + 4 else size) {
            if (writeOffset % 512 == 0) {
                client.writeChannel.writeByte(0xff.toByte())
                writeOffset = 1
            }
            client.writeChannel.writeByte(event.bytes[it + event.bytes.position()])
            writeOffset++
        }
        client.writeChannel.flush()
    }
}
