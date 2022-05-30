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
     * Updates this zone to the neighboring players including players in this zone.
     * This happens every tick.
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

    /**
     * Make an actor enter this zone.
     */
    fun enterZone(actor: Actor) {
        // This actor current zones.
        val zones = actor.zones()
        // This zone neighboring zones.
        val neighboring = neighboringZones()
        // Zones that are being removed from this actor current zones.
        val removed = zones.filter { it !in neighboring }
        // Zones that are being added to this actor current zones.
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

    /**
     * Make an actor leave this zone.
     * The actor is immediately required to enter a new zone upon leaving.
     */
    fun leaveZone(actor: Actor, nextZone: Zone? = null) {
        if (actor is Player) {
            players -= actor
        } else if (actor is NPC) {
            npcs -= actor
        }
        nextZone?.enterZone(actor)
    }

    /**
     * Requests a floor item to be removed from this zone.
     * Returns true if this floor item is able to be removed.
     */
    fun requestRemoveItem(floorItem: FloorItem): Boolean {
        if (floorItem in objsToRemove) return false
        objsToRemove += floorItem
        return true
    }

    /**
     * Requests a floor item to be added to this zone.
     * Returns true if this floor item is able to be added.
     */
    fun requestAddItem(floorItem: FloorItem): Boolean {
        if (floorItem in objsToAdd) return false
        objsToAdd += floorItem
        return true
    }

    /**
     * Requests a game object to be added to this zone.
     * Returns true if this game object is able to be added.
     */
    fun requestAddObject(gameObject: GameObject): Boolean {
        if (gameObject in locsToAdd) return false
        locsToAdd += gameObject
        return true
    }

    /**
     * Returns a list of players that are inside this zone and neighboring zones.
     */
    fun neighboringPlayers() = neighboringZones().filter(Zone::active).map(Zone::players).flatten()

    /**
     * Returns a list of npcs that are inside this zone and neighboring zones.
     */
    fun neighboringNpcs() = neighboringZones().filter(Zone::active).map(Zone::npcs).flatten()

    /**
     * Returns a list of game objects that are inside this zone and neighboring zones.
     */
    fun neighboringObjects() = neighboringZones().filter(Zone::active).map(Zone::locs).flatten()

    /**
     * Returns a list of floor items that are inside this zone and neighboring zones.
     */
    fun neighboringFloorItems() = neighboringZones().filter(Zone::active).map(Zone::objs).flatten()

    /**
     * Returns if this zone is active or not.
     */
    fun active() = players.isNotEmpty() || npcs.isNotEmpty() || objs.isNotEmpty() || locs.any(GameObject::spawned) || updating()

    /**
     * Returns if this zone needs updating or not.
     */
    fun updating(): Boolean = objsToAdd.isNotEmpty() || objsToRemove.isNotEmpty() || locsToAdd.isNotEmpty()

    /**
     * Returns a list of zones that are neighboring this zone including this zone.
     * This will always be in a 5x5 square area with this zone in the middle.
     */
    private fun neighboringZones() = (2.inv() + 1..2).flatMap { x ->
        (2.inv() + 1..2).map { z ->
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
