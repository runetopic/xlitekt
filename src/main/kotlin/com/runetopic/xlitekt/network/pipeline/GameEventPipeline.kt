package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

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

    override suspend fun write(client: Client, event: WriteEvent.GameWriteEvent) {}
}
