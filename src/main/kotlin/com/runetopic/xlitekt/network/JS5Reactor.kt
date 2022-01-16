package com.runetopic.xlitekt.network

import Client
import java.nio.ByteBuffer

class JS5Reactor : Reactor<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    override fun process(client: Client, event: ReadEvent.JS5ReadEvent): WriteEvent.JS5WriteEvent? {
        if (event.opcode == 0 || event.opcode == 1) {
            val indexId = event.indexId
            val groupId = event.groupId
            val requestingChecksums = indexId == 0xff && groupId == 0xff
            val buffer = ByteBuffer.wrap(if (requestingChecksums) client.store.checksumsWithoutRSA() else client.store.groupReferenceTable(indexId, groupId))
            val compression = if (requestingChecksums) 0 else buffer.get().toInt() and 0xff
            val size = if (requestingChecksums) client.store.checksumsWithoutRSA().size else buffer.int
            return WriteEvent.JS5WriteEvent(event.indexId, event.groupId, compression, size, buffer)
        } else {
            assert(event.opcode == 3)
        }
        return WriteEvent.JS5WriteEvent(-1, -1, -1, -1, ByteBuffer.allocate(0))
    }
}
