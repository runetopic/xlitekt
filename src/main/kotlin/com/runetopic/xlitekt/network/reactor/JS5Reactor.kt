package com.runetopic.xlitekt.network.reactor

import com.runetopic.xlitekt.network.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import java.nio.ByteBuffer

class JS5Reactor : Reactor<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    override fun process(client: Client, event: ReadEvent.JS5ReadEvent): WriteEvent.JS5WriteEvent? {
        return when (event.opcode) {
            0, 1 -> {
                val indexId = event.indexId
                val groupId = event.groupId
                println("${Thread.currentThread().id} $indexId, $groupId")
                val requestingChecksums = indexId == 0xff && groupId == 0xff
                val buffer = ByteBuffer.wrap(if (requestingChecksums) client.store.checksumsWithoutRSA() else client.store.groupReferenceTable(indexId, groupId))
                val compression = if (requestingChecksums) 0 else buffer.get().toInt() and 0xff
                val size = if (requestingChecksums) client.store.checksumsWithoutRSA().size else buffer.int
                WriteEvent.JS5WriteEvent(indexId, groupId, compression, size, buffer)
            }
            2, 3 -> {
                client.loggedIn = event.opcode == 2
                client.connectedToJs5 = !client.connectedToJs5
                WriteEvent.JS5WriteEvent()
            }
            4 -> {
                WriteEvent.JS5WriteEvent()
            } // TODO
            else -> null
        }
    }
}
