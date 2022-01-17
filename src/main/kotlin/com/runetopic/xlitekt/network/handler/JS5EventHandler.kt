package com.runetopic.xlitekt.network.handler

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.inject
import java.nio.ByteBuffer
import org.slf4j.Logger

class JS5EventHandler : EventHandler<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    private val store by inject<Js5Store>()
    private val logger by inject<Logger>()

    override fun handleEvent(client: Client, event: ReadEvent.JS5ReadEvent): WriteEvent.JS5WriteEvent? {
        return when (event.opcode) {
            0, 1 -> {
                val indexId = event.indexId
                val groupId = event.groupId
                logger.info("${Thread.currentThread().id} $indexId, $groupId")
                val requestingChecksums = indexId == 0xff && groupId == 0xff
                val buffer = ByteBuffer.wrap(if (requestingChecksums) store.checksumsWithoutRSA() else store.groupReferenceTable(indexId, groupId))
                val compression = if (requestingChecksums) 0 else buffer.get().toInt() and 0xff
                val size = if (requestingChecksums) store.checksumsWithoutRSA().size else buffer.int
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
