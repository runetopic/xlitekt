package xlitekt.game.loop.sync

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.isNotEmpty
import io.ktor.utils.io.core.readBytes
import java.util.concurrent.ConcurrentHashMap
import xlitekt.game.actor.player.Player
import xlitekt.game.packet.UpdateZonePartialEnclosedPacket
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeBytes

/**
 * @author Jordan Abraham
 */
class ZonesSynchronizer : Synchronizer() {
    override fun run() {
        val zoneUpdates = ConcurrentHashMap<Zone, ByteReadPacket>()
        val players = world.players.filterNotNull().filter(Player::online)

        val dick = players.associateWith(Player::zones)

        players.parallelStream().forEach { player ->
            val zones = dick[player] ?: return@forEach
            zones.filterNotNull().forEach {
                it.process(player.lastLoadedLocation!!)
                if (it.updates.isNotEmpty()) {
                    val data = buildPacket {
                        it.updates.forEach { entry ->
                            val location = entry.key
                            println("FUCKER")
                            println(location)
                            println((location.x shr 3) shl 3)
                            println((location.z shr 3) shl 3)
                            writeByteNegate(((location.z shr 3) shl 3).toByte())
                            writeByte(((location.x shr 3) shl 3).toByte())

                            val packets = entry.value
                            println(packets.size)
                            packets.forEach { packet ->
                                writeBytes(packet.copy().readBytes())
                            }
                        }
                    }
                    zoneUpdates[it] = data
                }
            }
        }

        players.parallelStream().forEach { player ->
            val zones = dick[player] ?: return@forEach
            zones.filterNotNull().forEach {
                val updates = zoneUpdates[it]
                if (updates != null && updates.isNotEmpty) {
                    zoneUpdates[it]?.let { packet -> player.write(UpdateZonePartialEnclosedPacket(packet)) }
                }
                it.reset()
            }
        }
    }
}
