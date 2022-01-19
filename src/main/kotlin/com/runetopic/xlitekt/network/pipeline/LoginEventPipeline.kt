package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.BAD_SESSION_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.CLIENT_OUTDATED_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.application.ApplicationEnvironment
import org.slf4j.Logger

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 */
class LoginEventPipeline(val serverSeed: Long = -1L) : EventPipeline<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {
    private val logger by inject<Logger>()
    private val environment by inject<ApplicationEnvironment>()

    override suspend fun read(client: Client): ReadEvent.LoginReadEvent? {
        val opcode = client.readChannel.readByte().toInt() and 0xff

        if (!isValidLoginRequestHeader(client)) {
            logger.info("Invalid login request header. Client=$client Opcode=$opcode Available bytes=${client.readChannel.availableForRead}")
            return null
        }

        val unknownByte1 = client.readChannel.readByte().toInt() and 0xff
        val unknownByte2 = client.readChannel.readByte().toInt() and 0xff
        return ReadEvent.LoginReadEvent(opcode)
    }

    private suspend fun isValidLoginRequestHeader(client: Client): Boolean {
        val size = client.readChannel.readShort().toInt() and 0xffff
        val availableBytes = client.readChannel.availableForRead

        if (size != availableBytes) {
            logger.info("Bad session. Client=$client Size Read=$size Available bytes=$availableBytes")
            client.writeResponse(BAD_SESSION_OPCODE)
            client.disconnect()
            return false
        }

        val majorVersion = client.readChannel.readInt()

        if (majorVersion != environment.config.property("game.build.major").getString().toInt()) {
            logger.info("Client outdated. Client=$client Major Version=$majorVersion")
            client.writeResponse(CLIENT_OUTDATED_OPCODE)
            client.disconnect()
            return false
        }

        val minorVersion = client.readChannel.readInt()

        if (minorVersion != environment.config.property("game.build.minor").getString().toInt()) {
            logger.info("Client outdated. Client=$client Minor Version=$majorVersion")
            client.writeResponse(CLIENT_OUTDATED_OPCODE)
            client.disconnect()
            return false
        }

        return true
    }

    override suspend fun write(client: Client, event: WriteEvent.LoginWriteEvent) {
    }
}
