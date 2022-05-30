package xlitekt.game.world.map.zone

import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.Actor
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.content.item.FloorItem
import xlitekt.game.packet.UpdateZoneFullFollowsPacket
import xlitekt.game.packet.UpdateZonePartialEnclosedPacket
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.obj.GameObject
import xlitekt.game.world.map.zone.ZoneUpdateType.LocAddType
import xlitekt.game.world.map.zone.ZoneUpdateType.ObjAddType
import xlitekt.game.world.map.zone.ZoneUpdateType.ObjDeleteType
import xlitekt.shared.inject
import java.util.Collections

class Zone(
    val location: Location,
    val players: MutableList<Player> = Collections.synchronizedList(mutableListOf()),
    val npcs: MutableList<NPC> = Collections.synchronizedList(mutableListOf()),
    val locs: MutableList<GameObject> = Collections.synchronizedList(mutableListOf()),
    val objs: MutableList<FloorItem> = Collections.synchronizedList(mutableListOf()),
) {
    private val objsToRemove = mutableListOf<FloorItem>()
    private val objsToAdd = mutableListOf<FloorItem>()
    private val locsToAdd = mutableListOf<GameObject>()

    /**
     * Updates this zone every tick.
     */
    fun update() {
        val neighboring = neighboringPlayers()
        objsToRemove.onEach {
            neighboring.forEach { player ->
                player.writeObjDelete(it)
            }
            objs -= it
        }.also(objsToRemove::removeAll)

        objsToAdd.onEach {
            neighboring.forEach { player ->
                player.writeObjAdd(it)
            }
            objs += it
        }.also(objsToAdd::removeAll)

        locsToAdd.onEach {
            neighboring.forEach { player ->
                player.writeLocAdd(it)
            }
            locs += it
        }.also(locsToAdd::removeAll)
    }

    fun enterZone(actor: Actor) {
        val zones = actor.zones()
        val neighboring = neighboringZones()
        val removed = zones.filter { it !in neighboring }
        val added = neighboring.filter { it !in zones }
        actor.setZones(removed, added)
        if (actor is Player) {
            added.forEach {
                // Clear the zone for updates.
                actor.write(UpdateZoneFullFollowsPacket(it.location.localX(actor.lastLoadedLocation!!), it.location.localZ(actor.lastLoadedLocation!!)))

                if (!it.active()) return@forEach

                // If zone contains any of the following, send them to the client.
                it.objs.forEach(actor::writeObjAdd)
                it.locs.filter(GameObject::spawned).forEach(actor::writeLocAdd)
            }
            players += actor
        } else if (actor is NPC) {
            npcs += actor
        }
        actor.setZone(this)
    }

    fun leaveZone(actor: Actor) {
        if (actor is Player) {
            players -= actor
        } else if (actor is NPC) {
            npcs -= actor
        }
    }

    fun requestRemoveItem(floorItem: FloorItem): Boolean {
        if (floorItem in objsToRemove) return false
        objsToRemove += floorItem
        return true
    }

    fun requestAddItem(floorItem: FloorItem): Boolean {
        if (floorItem in objsToAdd) return false
        objsToAdd += floorItem
        return true
    }

    fun requestAddObject(gameObject: GameObject): Boolean {
        if (gameObject in locsToAdd) return false
        locsToAdd += gameObject
        return true
    }

    fun neighboringPlayers() = neighboringZones().filter(Zone::active).map(Zone::players).flatten()
    fun neighboringNpcs() = neighboringZones().filter(Zone::active).map(Zone::npcs).flatten()
    fun neighboringObjects() = neighboringZones().filter(Zone::active).map(Zone::locs).flatten()
    fun neighboringItems() = neighboringZones().filter(Zone::active).map(Zone::objs).flatten()

    fun active() = players.isNotEmpty() || npcs.isNotEmpty() || objs.isNotEmpty() || locs.any(GameObject::spawned) || updating()
    fun updating(): Boolean = objsToAdd.isNotEmpty() || objsToRemove.isNotEmpty() || locsToAdd.isNotEmpty()

    private fun neighboringZones(width: Int = 2, height: Int = 2) = (width.inv() + 1..width).flatMap { x ->
        (height.inv() + 1..height).map { z ->
            location.toZoneLocation().transform(x, z)
        }
    }.mapNotNull { world.zone(it.toFullLocation()) }

    private companion object {
        val world by inject<World>()
    }
}

data class LocalLocation(
    val x: Int,
    val z: Int
) {
    private val offsetX = x - ((x shr 3) shl 3)
    private val offsetZ = z - ((z shr 3) shl 3)
    val packedOffset = (offsetX shl 4) or offsetZ
}

private fun Player.writeObjAdd(floorItem: FloorItem) {
    val local = LocalLocation(
        floorItem.location.localX(lastLoadedLocation!!),
        floorItem.location.localZ(lastLoadedLocation!!)
    )
    invokeAndWriteUpdate(ObjAddType(floorItem.id, floorItem.amount, local.packedOffset), local)
}

private fun Player.writeObjDelete(floorItem: FloorItem) {
    val local = LocalLocation(
        floorItem.location.localX(lastLoadedLocation!!),
        floorItem.location.localZ(lastLoadedLocation!!)
    )
    invokeAndWriteUpdate(ObjDeleteType(floorItem.id, local.packedOffset), local)
}

private fun Player.writeLocAdd(gameObject: GameObject) {
    val local = LocalLocation(
        gameObject.location.localX(lastLoadedLocation!!),
        gameObject.location.localZ(lastLoadedLocation!!)
    )
    invokeAndWriteUpdate(LocAddType(gameObject.id, gameObject.shape, gameObject.rotation, local.packedOffset), local)
}

private fun Player.invokeAndWriteUpdate(type: ZoneUpdateType, local: LocalLocation) {
    val bytes = ZoneUpdateAssembler.assemblers[type::class]!!.bytes.invoke(type).readBytes()
    write(UpdateZonePartialEnclosedPacket(local.x, local.z, bytes))
}
