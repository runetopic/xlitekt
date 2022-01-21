package com.runetopic.xlitekt.network.handler

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.CONNECTION_LOGGED_IN_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.CONNECTION_LOGGED_OUT_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.ENCRYPTION_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.HIGH_PRIORITY_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.LOW_PRIORITY_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.LOGIN_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.ktor.inject
import org.slf4j.Logger
import java.nio.ByteBuffer

class JS5EventHandler : EventHandler<ReadEvent.JS5ReadEvent, WriteEvent.JS5WriteEvent> {

    private val store by inject<Js5Store>()
    private val logger by inject<Logger>()

    override fun handleEvent(client: Client, event: ReadEvent.JS5ReadEvent): WriteEvent.JS5WriteEvent? {
        return when (event.opcode) {
            HIGH_PRIORITY_OPCODE, LOW_PRIORITY_OPCODE -> {
                val indexId = event.indexId
                val groupId = event.groupId
                logger.info("${Thread.currentThread().id} $indexId, $groupId")
                val requestingChecksums = indexId == 0xff && groupId == 0xff
                val buffer = ByteBuffer.wrap(if (requestingChecksums) store.checksumsWithoutRSA() else store.groupReferenceTable(indexId, groupId))
                val compression = if (requestingChecksums) 0 else buffer.get().toInt() and 0xff
                val size = if (requestingChecksums) store.checksumsWithoutRSA().size else buffer.int
                WriteEvent.JS5WriteEvent(indexId, groupId, compression, size, buffer)
            }
            CONNECTION_LOGGED_IN_OPCODE, CONNECTION_LOGGED_OUT_OPCODE -> {
                client.loggedIn = event.opcode == LOGIN_SUCCESS_OPCODE
                client.connectedToJs5 = !client.connectedToJs5
                WriteEvent.JS5WriteEvent()
            }
            ENCRYPTION_OPCODE -> { WriteEvent.JS5WriteEvent() } // TODO
            else -> null
        }
    }
}
