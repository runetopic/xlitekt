package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.util.ext.writePacketOpcode
import com.runetopic.xlitekt.util.ext.writePacketSize

/**
 * @author Jordan Abraham
 */
class GameEventPipeline : EventPipeline<ReadEvent.GameReadEvent, WriteEvent.GameWriteEvent> {

    override suspend fun read(client: Client): ReadEvent.GameReadEvent? {
        println("Read packet.")
        val opcode = client.readChannel.readByte().toInt() and 0xff
        println(opcode)
        return null
    }

    override suspend fun write(client: Client, event: WriteEvent.GameWriteEvent) {
        println("Writing packet Opcode=${event.opcode} Size=${event.payload.remaining}")
        client.writeChannel.writePacketOpcode(client.serverCipher!!, event.opcode)
        client.writeChannel.writePacketSize(event.size, event.payload.remaining)
        client.writeChannel.writePacket(event.payload)
        client.writeChannel.flush()
        event.payload.release()
    }
}

