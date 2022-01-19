package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.ktor.inject
import org.slf4j.Logger

class LoginEventPipeline(val serverSeed: Long = -1L) : EventPipeline<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {
    private val logger by inject<Logger>()

    override suspend fun read(client: Client): ReadEvent.LoginReadEvent {
        logger.info("Testing")
        val opcode = client.readChannel.readByte().toInt() and 0xff
        val size = client.readChannel.readShort().toInt() and 0xffff
        logger.info("Login event $opcode $size")
        return ReadEvent.LoginReadEvent()
    }

    override suspend fun write(client: Client, event: WriteEvent.LoginWriteEvent) {
    }
}
