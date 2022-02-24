package com.runetopic.xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.PlayerInfoPacketAssembler
import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import kotlinx.coroutines.Runnable
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
class EntitySynchronizer : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()

    override fun run() {
        val players = world.players.filterNotNull().filter(Player::online)

        val time = measureTime {
            val pending = players.associateWith { it.pendingUpdates().toList() }
            val updates = mutableMapOf<Player, ByteReadPacket>()
            players.parallelStream().forEach { player ->
                val builder = BytePacketBuilder()
                val blocks = pending[player]?.map { it to PlayerInfoPacketAssembler.renderingBlockMap[it::class]!! }?.sortedBy { it.second.index }?.toMap() ?: emptyMap()
                val mask = blocks.map { it.value.mask }.sum().let { if (it > 0xff) it or 0x10 else it }
                if (mask > 0xff) builder.writeShortLittleEndian(mask.toShort()) else builder.writeByte(mask.toByte())
                blocks.forEach { builder.writePacket(it.value.build(player, it.key)) }
                updates[player] = builder.build()
            }
            players.parallelStream().forEach {
                it.write(PlayerInfoPacket(it, updates))
                it.write(NPCInfoPacket(it))
                it.flushPool()
            }
            players.forEach(Player::reset)
        }
        logger.debug { "Synchronization took $time for ${players.size} players." }
    }
}
