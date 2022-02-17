package com.runetopic.xlitekt.network.client

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.CamResetPacket
import com.runetopic.xlitekt.network.packet.ForceLogoutPacket
import com.runetopic.xlitekt.network.packet.HintArrowPacket
import com.runetopic.xlitekt.network.packet.IfCloseSubPacket
import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.IfSetColorPacket
import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.network.packet.IfSetHiddenPacket
import com.runetopic.xlitekt.network.packet.IfSetTextPacket
import com.runetopic.xlitekt.network.packet.LogoutPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.MidiSongPacket
import com.runetopic.xlitekt.network.packet.MiniMapTogglePacket
import com.runetopic.xlitekt.network.packet.NPCInfoExtendedViewportPacket
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.NoTimeoutPacket
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.network.packet.SetMapFlagPacket
import com.runetopic.xlitekt.network.packet.SetPlayerOpPacket
import com.runetopic.xlitekt.network.packet.SoundEffectPacket
import com.runetopic.xlitekt.network.packet.UpdateContainerFullPacket
import com.runetopic.xlitekt.network.packet.UpdateContainerPartialPacket
import com.runetopic.xlitekt.network.packet.UpdateFriendListPacket
import com.runetopic.xlitekt.network.packet.UpdatePrivateChatStatusPacket
import com.runetopic.xlitekt.network.packet.UpdatePublicChatStatusPacket
import com.runetopic.xlitekt.network.packet.UpdateRebootTimerPacket
import com.runetopic.xlitekt.network.packet.UpdateRunEnergyPacket
import com.runetopic.xlitekt.network.packet.UpdateStatPacket
import com.runetopic.xlitekt.network.packet.UpdateWeightPacket
import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.network.packet.assembler.CamResetPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.ForceLogoutPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.HintArrowPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfCloseSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfMoveSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfOpenSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfOpenTopPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfSetColorPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfSetEventsPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfSetHiddenPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfSetTextPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.LogoutPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.MessageGamePacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.MidiSongPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.MiniMapTogglePacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.NPCInfoPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.NoTimeoutPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.PlayerInfoPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.RebuildNormalPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.RunClientScriptPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.SetMapFlagPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.SetPlayerOpPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.SoundEffectPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateContainerFullPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateContainerPartialPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateFriendsListPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdatePrivateChatStatusPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdatePublicChatStatusPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateRebootTimerPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateRunEnergyAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateStatAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateWeightPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.VarpLargePacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.VarpSmallPacketAssembler
import com.runetopic.xlitekt.network.packet.disassembler.CloseModalPacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton10PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton1PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton2PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton3PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton4PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton5PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton6PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton7PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton8PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton9PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.OpHeld5PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.OpObj6PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.WindowStatusPacketDisassembler
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.Dispatcher
import com.runetopic.xlitekt.shared.buffer.poolToWriteChannel
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
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
) {
    val logger = InlineLogger()
    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    var clientCipher: ISAAC? = null
    var serverCipher: ISAAC? = null
    var player: Player? = null // TODO Figure out a way to not have the player here.

    fun disconnect(reason: String) {
        player?.logout()
        socket.close()
        logger.debug { "Client disconnected for reason={$reason}." }
    }

    fun setIsaacCiphers(clientCipher: ISAAC, serverCipher: ISAAC) {
        if (this.clientCipher != null || this.serverCipher != null) {
            return disconnect("Client or server cipher is already set.")
        }
        this.clientCipher = clientCipher
        this.serverCipher = serverCipher
    }

    fun handleException(exception: Exception) = when {
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

    fun writePacket(packet: Packet) = runBlocking(Dispatcher.GAME) {
        val assembler = assemblers[packet::class] ?: return@runBlocking disconnect("Unhandled packet found when trying to write. Packet was $packet.")
        poolToWriteChannel(assembler.opcode, assembler.size, assembler.assemblePacket(packet))
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

        val assemblers = mapOf(
            CamResetPacket::class to CamResetPacketAssembler(),
            IfOpenTopPacket::class to IfOpenTopPacketAssembler(),
            IfOpenSubPacket::class to IfOpenSubPacketAssembler(),
            IfMoveSubPacket::class to IfMoveSubPacketAssembler(),
            IfCloseSubPacket::class to IfCloseSubPacketAssembler(),
            IfSetColorPacket::class to IfSetColorPacketAssembler(),
            IfSetEventsPacket::class to IfSetEventsPacketAssembler(),
            IfSetHiddenPacket::class to IfSetHiddenPacketAssembler(),
            IfSetTextPacket::class to IfSetTextPacketAssembler(),
            ForceLogoutPacket::class to ForceLogoutPacketAssembler(),
            LogoutPacket::class to LogoutPacketAssembler(),
            HintArrowPacket::class to HintArrowPacketAssembler(),
            MessageGamePacket::class to MessageGamePacketAssembler(),
            MidiSongPacket::class to MidiSongPacketAssembler(),
            MiniMapTogglePacket::class to MiniMapTogglePacketAssembler(),
            NoTimeoutPacket::class to NoTimeoutPacketAssembler(),
            NPCInfoPacket::class to NPCInfoPacketAssembler(false),
            NPCInfoExtendedViewportPacket::class to NPCInfoPacketAssembler(false),
            PlayerInfoPacket::class to PlayerInfoPacketAssembler(),
            RebuildNormalPacket::class to RebuildNormalPacketAssembler(),
            RunClientScriptPacket::class to RunClientScriptPacketAssembler(),
            SetMapFlagPacket::class to SetMapFlagPacketAssembler(),
            SetPlayerOpPacket::class to SetPlayerOpPacketAssembler(),
            SoundEffectPacket::class to SoundEffectPacketAssembler(),
            UpdateContainerFullPacket::class to UpdateContainerFullPacketAssembler(),
            UpdateContainerPartialPacket::class to UpdateContainerPartialPacketAssembler(),
            UpdateFriendListPacket::class to UpdateFriendsListPacketAssembler(),
            UpdateRebootTimerPacket::class to UpdateRebootTimerPacketAssembler(),
            UpdateRunEnergyPacket::class to UpdateRunEnergyAssembler(),
            UpdateStatPacket::class to UpdateStatAssembler(),
            UpdatePublicChatStatusPacket::class to UpdatePublicChatStatusPacketAssembler(),
            UpdatePrivateChatStatusPacket::class to UpdatePrivateChatStatusPacketAssembler(),
            UpdateWeightPacket::class to UpdateWeightPacketAssembler(),
            VarpLargePacket::class to VarpLargePacketAssembler(),
            VarpSmallPacket::class to VarpSmallPacketAssembler()
        )

        val disassemblers = setOf(
            WindowStatusPacketDisassembler(),

            CloseModalPacketDisassembler(),

            IfButton1PacketDisassembler(),
            IfButton2PacketDisassembler(),
            IfButton3PacketDisassembler(),
            IfButton4PacketDisassembler(),
            IfButton5PacketDisassembler(),
            IfButton6PacketDisassembler(),
            IfButton7PacketDisassembler(),
            IfButton8PacketDisassembler(),
            IfButton9PacketDisassembler(),
            IfButton10PacketDisassembler(),

            OpObj6PacketDisassembler(),

            OpHeld5PacketDisassembler()
        )
    }
}
