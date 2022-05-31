package xlitekt.game.world.map.zone

import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.Actor
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.projectile.Projectile
import xlitekt.game.packet.LocAddPacket
import xlitekt.game.packet.LocDelPacket
import xlitekt.game.packet.MapProjAnimPacket
import xlitekt.game.packet.ObjAddPacket
import xlitekt.game.packet.ObjDelPacket
import xlitekt.game.packet.Packet
import xlitekt.game.packet.UpdateZoneFullFollowsPacket
import xlitekt.game.packet.UpdateZonePartialEnclosedPacket
import xlitekt.game.packet.UpdateZonePartialFollowsPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.obj.GameObject
import xlitekt.shared.inject
import java.util.Collections

/**
 * @author Jordan Abraham
 */
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
    private val locsToRemove = mutableListOf<GameObject>()
    private val projectilesToAdd = mutableListOf<Projectile>()

    /**
     * Updates this zone to the neighboring players including players in this zone.
     * This happens every tick.
     */
    fun update() {
        val updates = mutableMapOf<Player, MutableList<Packet>>()

        val neighboring = neighboringPlayers()
        if (projectilesToAdd.isNotEmpty()) {
            for (player in neighboring) {
                for (projectile in projectilesToAdd) {
                    updates.addMapProjAnim(player, projectile)
                }
            }
            projectilesToAdd.clear()
        }

        if (objsToRemove.isNotEmpty()) {
            for (player in neighboring) {
                for (floorItem in objsToRemove) {
                    updates.delObj(player, floorItem)
                }
            }
            for (floorItem in objsToRemove) {
                objs.removeAt(objs.lastIndexOf(floorItem))
            }
            objsToRemove.clear()
        }

        if (objsToAdd.isNotEmpty()) {
            for (player in neighboring) {
                for (floorItem in objsToAdd) {
                    updates.addObj(player, floorItem)
                }
            }
            for (floorItem in objsToAdd) {
                objs.add(0, floorItem)
            }
            objsToAdd.clear()
        }

        if (locsToRemove.isNotEmpty()) {
            for (player in neighboring) {
                for (gameObject in locsToRemove) {
                    updates.delLoc(player, gameObject)
                }
            }
            for (gameObject in locsToRemove) {
                locs.removeAt(locs.filter(GameObject::spawned).lastIndexOf(gameObject))
            }
            locsToRemove.clear()
        }

        if (locsToAdd.isNotEmpty()) {
            for (player in neighboring) {
                for (gameObject in locsToAdd) {
                    updates.addLoc(player, gameObject)
                }
            }
            for (gameObject in locsToAdd) {
                locs.add(0, gameObject)
            }
            locsToAdd.clear()
        }

        updates.write(location)
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
        val added = neighboring.filter { it !in zones }.filter {
            return@filter if (actor is Player) {
                val localX = it.location.localX(actor.lastLoadedLocation!!)
                val localZ = it.location.localZ(actor.lastLoadedLocation!!)
                localX in 0 until 104 && localZ in 0 until 104
            } else true
        }
        actor.setZones(removed, added)

        if (actor is Player) {
            added.forEach { zone ->
                // Clear the zone for updates.
                actor.write(UpdateZoneFullFollowsPacket(zone.location.localX(actor.lastLoadedLocation!!), zone.location.localZ(actor.lastLoadedLocation!!)))

                if (!zone.active()) return@forEach

                // actor.write(UpdateZonePartialFollowsPacket(local.x, local.z))
                val updates = mutableMapOf<Player, MutableList<Packet>>()
                // If zone contains any of the following, send them to the client.
                zone.objs.forEach {
                    updates.addObj(actor, it)
                }
                zone.locs.filter(GameObject::spawned).forEach {
                    updates.addLoc(actor, it)
                }
                updates.write(zone.location)
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
     * nextZone is nullable for logout.
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
     * Returns false if this floor item is already requested to be removed.
     */
    fun requestRemoveObj(floorItem: FloorItem): Boolean {
        if (floorItem in objsToRemove) return false
        objsToRemove += floorItem
        return true
    }

    /**
     * Requests a floor item to be added to this zone.
     * Returns true if this floor item is able to be added.
     */
    fun requestAddObj(floorItem: FloorItem): Boolean {
        if (floorItem in objsToAdd) return false
        objsToAdd += floorItem
        return true
    }

    /**
     * Requests a game object to be removed from this zone.
     * Returns true if this game object is able to be removed.
     * Returns false if this game object is already requested to be removed.
     */
    fun requestDeleteLoc(gameObject: GameObject): Boolean {
        if (gameObject in locsToRemove) return false
        locsToRemove += gameObject
        return true
    }

    /**
     * Requests a game object to be added to this zone.
     * Returns true if this game object is able to be added.
     */
    fun requestAddLoc(gameObject: GameObject): Boolean {
        if (gameObject in locsToAdd) return false
        locsToAdd += gameObject
        return true
    }

    /**
     * Requests a map proj anim to be added to this zone.
     * Returns true if this map proj anim is able to be added.
     */
    fun requestAddMapProjAnim(projectile: Projectile): Boolean {
        if (projectile in projectilesToAdd) return false
        projectilesToAdd += projectile
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
    fun neighboringLocs() = neighboringZones().filter(Zone::active).map(Zone::locs).flatten()

    /**
     * Returns a list of floor items that are inside this zone and neighboring zones.
     */
    fun neighboringObjs() = neighboringZones().filter(Zone::active).map(Zone::objs).flatten()

    /**
     * Returns if this zone is active or not.
     */
    fun active() = players.isNotEmpty() || npcs.isNotEmpty() || objs.isNotEmpty() || locs.any(GameObject::spawned) || updating()

    /**
     * Returns if this zone needs updating or not.
     */
    fun updating() = objsToAdd.isNotEmpty() || objsToRemove.isNotEmpty() || locsToAdd.isNotEmpty() || locsToRemove.isNotEmpty() || projectilesToAdd.isNotEmpty()

    /**
     * Returns a list of zones that are neighboring this zone including this zone.
     * This will always be in a 7x7 square area with this zone in the middle.
     */
    private fun neighboringZones() = (-3..3).flatMap { x ->
        (-3..3).map { z ->
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

private fun Location.toLocalLocation(other: Location) = LocalLocation(localX(other), localZ(other))

/**
 * Adds a ObjAddPacket to this updates map.
 */
private fun MutableMap<Player, MutableList<Packet>>.addObj(player: Player, floorItem: FloorItem) {
    val current = this[player] ?: mutableListOf()
    current += ObjAddPacket(
        id = floorItem.id,
        amount = floorItem.amount,
        packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation!!).packedOffset
    )
    this[player] = current
}

/**
 * Adds a ObjDelPacket to this updates map.
 */
private fun MutableMap<Player, MutableList<Packet>>.delObj(player: Player, floorItem: FloorItem) {
    val current = this[player] ?: mutableListOf()
    current += ObjDelPacket(
        id = floorItem.id,
        packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation!!).packedOffset
    )
    this[player] = current
}

/**
 * Adds a LocAddPacket to this updates map.
 */
private fun MutableMap<Player, MutableList<Packet>>.addLoc(player: Player, gameObject: GameObject) {
    val current = this[player] ?: mutableListOf()
    current += LocAddPacket(
        id = gameObject.id,
        shape = gameObject.shape,
        rotation = gameObject.rotation,
        packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation!!).packedOffset
    )
    this[player] = current
}

/**
 * Adds a LocDelPacket to this updates map.
 */
private fun MutableMap<Player, MutableList<Packet>>.delLoc(player: Player, gameObject: GameObject) {
    val current = this[player] ?: mutableListOf()
    current += LocDelPacket(
        shape = gameObject.shape,
        rotation = gameObject.rotation,
        packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation!!).packedOffset
    )
    this[player] = current
}

/**
 * Adds a MapProjAnimPacket to this updates map.
 */
private fun MutableMap<Player, MutableList<Packet>>.addMapProjAnim(player: Player, projectile: Projectile) {
    val current = this[player] ?: mutableListOf()
    current += MapProjAnimPacket(
        id = projectile.id,
        distanceX = projectile.distanceX(),
        distanceZ = projectile.distanceZ(),
        startHeight = projectile.startHeight,
        endHeight = projectile.endHeight,
        delay = projectile.delay,
        lifespan = 5 + 5 * 10,
        angle = projectile.angle,
        steepness = projectile.steepness,
        packedOffset = ((projectile.startLocation.x and 0x7) shl 4) or (projectile.startLocation.z and 0x7)
    )
    this[player] = current
}

/**
 * Writes this map of updates to the players contained within this map. Uses the zone base location.
 */
private fun MutableMap<Player, MutableList<Packet>>.write(baseLocation: Location) {
    for (entry in this) {
        val player = entry.key
        val localX = baseLocation.localX(player.lastLoadedLocation!!)
        val localZ = baseLocation.localZ(player.lastLoadedLocation!!)
        if (entry.value.size == 1) {
            player.write(UpdateZonePartialFollowsPacket(localX, localZ))
            player.write(entry.value.first())
        } else {
            player.write(
                UpdateZonePartialEnclosedPacket(
                    localX = localX,
                    localZ = localZ,
                    bytes = entry.value.fold(byteArrayOf()) { current, next ->
                        val assembler = PacketAssemblerListener.listeners[next::class]!!
                        // Insert this zone update index byte ahead of the body.
                        val bytes = byteArrayOf(ZoneUpdate.zoneUpdateMap[next::class]!!.index) + assembler.packet.invoke(next).readBytes()
                        current + bytes
                    }
                )
            )
        }
    }
}
