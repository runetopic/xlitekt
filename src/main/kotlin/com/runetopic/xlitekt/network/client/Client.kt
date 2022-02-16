package com.runetopic.xlitekt.network.client

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.packet.RegisteredPackets.assemblers
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.Dispatcher
import com.runetopic.xlitekt.shared.buffer.writePacket
import io.ktor.application.ApplicationEnvironment
import io.ktor.network.sockets.Socket
import io.ktor.util.reflect.instanceOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.ClosedWriteChannelException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.SocketException

/**
 * @author Jordan Abraham
 */
class Client(
    private val socket: Socket,
    internal val readChannel: ByteReadChannel,
    internal val writeChannel: ByteWriteChannel
) {
    internal val logger = InlineLogger()
    internal val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    internal var clientCipher: ISAAC? = null
    internal var serverCipher: ISAAC? = null
    internal var player: Player? = null // TODO Figure out a way to not have the player here.

    internal fun disconnect(reason: String) {
        player?.logout()
        socket.close()
        logger.debug { "Client disconnected for reason={$reason}." }
    }

    internal fun setIsaacCiphers(clientCipher: ISAAC, serverCipher: ISAAC) {
        if (this.clientCipher != null || this.serverCipher != null) {
            disconnect("Client or server cipher is already set.")
            return
        }
        this.clientCipher = clientCipher
        this.serverCipher = serverCipher
    }

    internal fun handleException(exception: Exception) = when {
        exception.instanceOf(TimeoutCancellationException::class) -> disconnect("Client timed out.")
        exception.instanceOf(SocketException::class) -> disconnect("Connection reset.")
        exception.instanceOf(ClosedWriteChannelException::class) -> disconnect("The channel was closed.")
        exception.instanceOf(ClosedReceiveChannelException::class) -> disconnect("The channel was closed.")
        exception.instanceOf(IOException::class) -> disconnect("Client IO exception caught.")
        else -> {
            logger.error(exception) { "Exception caught during client IO Events." }
            disconnect(exception.message.toString())
        }
    }

    fun writePacket(packet: Packet) {
        val assembler = assemblers[packet::class] ?: return disconnect("Unhandled message found when trying to write packet. Message was $packet.")
        val payload = assembler.assemblePacket(packet)
        runBlocking(Dispatcher.GAME) {
            writePacket(assembler.opcode, assembler.size, payload)
        }
    }

    companion object {
        private val environment by inject<ApplicationEnvironment>()
        val majorBuild = environment.config.property("game.build.major").getString().toInt()
        val minorBuild = environment.config.property("game.build.minor").getString().toInt()
        val rsaExponent = environment.config.property("game.rsa.exponent").getString()
        val rsaModulus = environment.config.property("game.rsa.modulus").getString()
        val token = environment.config.property("game.build.token").getString()
        val sizes = environment.config.property("game.packet.sizes").getList().map(String::toInt)

        val store by inject<Js5Store>()
        val checksums = store.checksumsWithoutRSA()

        val world by inject<World>()
    }
}
