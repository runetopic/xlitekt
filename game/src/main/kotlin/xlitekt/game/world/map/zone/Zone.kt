package xlitekt.game.world.map.zone

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.content.item.GroundItem
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.ZoneLocation
import xlitekt.game.world.map.obj.GameObject
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteSubtract
import java.util.Collections

class Zone(
    val zoneLocation: ZoneLocation,
) {
    val players: MutableSet<Player?> = Collections.synchronizedSet(mutableSetOf())
    val npcs: MutableSet<NPC?> = Collections.synchronizedSet(mutableSetOf())
    val gameObjects: MutableSet<GameObject?> = Collections.synchronizedSet(mutableSetOf())
    val groundItems: MutableSet<GroundItem?> = Collections.synchronizedSet(mutableSetOf())
    val updates: MutableMap<Location, List<ByteReadPacket>> = Collections.synchronizedMap(mutableMapOf())

    val itemsToAdd = mutableSetOf<GroundItem>()
    val itemsToRemove = mutableSetOf<GroundItem>()

    fun requestAddGroundItem(groundItem: GroundItem) {
        itemsToAdd += groundItem
    }

    fun requestRemoveGroundItem(groundItem: GroundItem) {
        itemsToRemove += groundItem
    }

    /**
     * Called from game loop only.
     */
    fun process(location: Location) {
        println("process")
        itemsToAdd.forEach {
            println("adding")
            val x = it.location.sceneX(location)
            val z = it.location.sceneZ(location)
            val local = Location(x, z)
            updates[local] = updates[local]?.toMutableList()?.let { packets ->
                packets.add(addGroundItem(it, local))
                packets
            } ?: listOf(addGroundItem(it, local))
        }

        itemsToRemove.forEach {
            println("removing")
            val x = it.location.sceneX(location)
            val z = it.location.sceneZ(location)
            val local = Location(x, z)
            updates[local] = updates[local]?.toMutableList()?.let { packets ->
                packets.add(removeGroundItem(it, local))
                packets
            } ?: listOf(removeGroundItem(it, local))
        }
    }

    fun queueRefresh(location: Location) {
        println("queueu first")
        if (itemsToAdd.isNotEmpty()) return
        if (itemsToRemove.isNotEmpty()) return
        // Refresh everything here.
        groundItems.filterNotNull().forEach {
            println("queueuee")
            val x = it.location.sceneX(location)
            val z = it.location.sceneZ(location)
            val local = Location(x, z)
            updates[local] = updates[local]?.toMutableList()?.let { packets ->
                packets.add(addGroundItem(it, local))
                packets
            } ?: listOf(addGroundItem(it, local))
        }
    }

    /**
     * Called from game loop only.
     */
    fun reset() {
        groundItems += itemsToAdd
        groundItems -= itemsToRemove
        itemsToAdd.clear()
        itemsToRemove.clear()
        updates.clear()
    }

    private fun addGroundItem(item: GroundItem, local: Location): ByteReadPacket = buildPacket {
        writeByte(4) // add item
        writeShort(item.id.toShort())
        val offsetX = local.x - ((local.x shr 3) shl 3)
        val offsetZ = local.z - ((local.z shr 3) shl 3)
        println(local)
        println(offsetX)
        println(offsetZ - local.z)
        writeByteSubtract(((offsetX shl 4) or offsetZ).toByte())
    }

    private fun removeGroundItem(item: GroundItem, local: Location): ByteReadPacket = buildPacket {
        writeByte(7) // remove item
        val offsetX = local.x - ((local.x shr 3) shl 3)
        val offsetZ = local.z - ((local.z shr 3) shl 3)
        writeByteAdd(((offsetX shl 4) or offsetZ).toByte())
        writeShortLittleEndian(item.id.toShort())
    }
}
