package com.runetopic.xlitekt.network.handler

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_ENCRYPTION_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_HIGH_PRIORITY_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_LOGGED_IN_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_LOGGED_OUT_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.JS5_LOW_PRIORITY_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.inject
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class JS5EventHandler : EventHandler<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    private val store by inject<Js5Store>()

    override suspend fun handleEvent(client: Client, event: ReadEvent.JS5ReadEvent): WriteEvent.JS5WriteEvent {
        return when (event.opcode) {
            JS5_HIGH_PRIORITY_OPCODE, JS5_LOW_PRIORITY_OPCODE -> {
                val indexId = event.indexId
                val groupId = event.groupId
                val requestingChecksums = indexId == 0xff && groupId == 0xff
                val buffer = ByteBuffer.wrap(if (requestingChecksums) store.checksumsWithoutRSA() else store.groupReferenceTable(indexId, groupId))
                val compression = if (requestingChecksums) 0 else buffer.get().toInt() and 0xff
                val size = if (requestingChecksums) store.checksumsWithoutRSA().size else buffer.int
                WriteEvent.JS5WriteEvent(indexId, groupId, compression, size, buffer)
            }
            JS5_ENCRYPTION_OPCODE -> { WriteEvent.JS5WriteEvent() } // TODO this does need to return something so we can handle the encryption value properly
            JS5_LOGGED_IN_OPCODE, JS5_LOGGED_OUT_OPCODE -> {
                client.loggedIn = event.opcode == JS5_LOGGED_IN_OPCODE
                client.connectedToJs5 = !client.connectedToJs5
                WriteEvent.JS5WriteEvent()
            }
            else -> throw IllegalStateException("Unhandled Js5 Opcode in event handler. Opcode=${event.opcode}")
        }
    }
}
