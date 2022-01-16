package com.runetopic.xlitekt.network

import Client

class JS5Pipeline : Pipeline<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    override suspend fun read(client: Client): ReadEvent.JS5ReadEvent? {
        val opcode = client.readChannel.readByte().toInt()
        val indexId = client.readChannel.readByte().toInt() and 0xff
        val groupId = client.readChannel.readShort().toInt() and 0xffff
        return when (opcode) {
            0, 1, 3 -> ReadEvent.JS5ReadEvent(opcode, indexId, groupId)
            else -> null
        }
    }

    override suspend fun write(client: Client, event: WriteEvent.JS5WriteEvent) {
        val indexId = event.indexId
        val groupId = event.groupId
        val requestingChecksums = indexId == 0xff && groupId == 0xff
        val compression = event.compression
        val size = event.size

        if (indexId == -1 && groupId == -1 && compression == -1 && size == -1) return // TODO

        client.writeChannel.writeByte(indexId.toByte())
        client.writeChannel.writeShort(groupId.toShort())
        client.writeChannel.writeByte(compression.toByte())
        client.writeChannel.writeInt(size)

        var writerOffset = 8
        var readerOffset = if (requestingChecksums) 0 else 5

        while (readerOffset < if (requestingChecksums) size else (if (compression != 0) size + 4 else size) + 5) {
            if (writerOffset == 512) {
                client.writeChannel.writeByte(0xff.toByte())
                writerOffset = 1
            }
            client.writeChannel.writeByte(event.bytes[readerOffset])
            writerOffset++
            readerOffset++
        }
        client.writeChannel.flush()
    }
}
