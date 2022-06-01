package xlitekt.game.world.map.zone

import io.ktor.utils.io.core.buildPacket
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
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeBytes
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
    private val objRequests = mutableMapOf<FloorItem, Boolean>()
    private val locRequests = mutableMapOf<GameObject, Boolean>()
    private val mapProjRequests = mutableListOf<Projectile>()

    /**
     * Updates this zone to the neighboring players including players in this zone.
     * This happens every tick.
     */
    fun update() {
        val updates = mutableMapOf<Player, MutableList<Packet>>()

        val neighboring = neighboringPlayers()
        if (mapProjRequests.isNotEmpty()) {
            for (projectile in mapProjRequests) {
                for (player in neighboring) {
                    updates.addMapProjAnim(player, projectile)
                }
            }
            mapProjRequests.clear()
        }

        if (objRequests.isNotEmpty()) {
            val removing = objRequests.filterValues { !it }
            val adding = objRequests.filterValues { it }

            if (removing.isNotEmpty()) {
                for (floorItem in removing.keys) {
                    for (player in neighboring) {
                        updates.delObj(player, floorItem)
                    }
                    objs.removeAt(objs.lastIndexOf(floorItem))
                }
            }

            if (adding.isNotEmpty()) {
                for (floorItem in adding.keys) {
                    for (player in neighboring) {
                        updates.addObj(player, floorItem)
                    }
                    objs.add(0, floorItem)
                }
            }
            objRequests.clear()
        }

        if (locRequests.isNotEmpty()) {
            val removing = locRequests.filterValues { !it }
            val adding = locRequests.filterValues { it }

            if (removing.isNotEmpty()) {
                for (gameObject in removing.keys) {
                    for (player in neighboring) {
                        updates.delLoc(player, gameObject)
                    }
                    locs.removeAt(locs.filter(GameObject::spawned).lastIndexOf(gameObject))
                }
            }

            if (adding.isNotEmpty()) {
                for (gameObject in adding.keys) {
                    for (player in neighboring) {
                        updates.addLoc(player, gameObject)
                    }
                    locs.add(0, gameObject)
                }
            }
            locRequests.clear()
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
            if (actor is Player) {
                val localX = it.location.localX(actor.lastLoadedLocation)
                val localZ = it.location.localZ(actor.lastLoadedLocation)
                localX in 0 until 104 && localZ in 0 until 104
            } else true
        }
        actor.setZones(removed.toSet(), added.toSet())

        if (actor is Player) {
            for (zone in added) {
                // Clear the zone for updates.
                actor.write(UpdateZoneFullFollowsPacket(zone.location.localX(actor.lastLoadedLocation), zone.location.localZ(actor.lastLoadedLocation)))
                if (!zone.active()) {
                    continue
                }
                val updates = mutableMapOf<Player, MutableList<Packet>>()
                // If zone contains any of the following, send them to the client.
                for (obj in zone.objs) {
                    updates.addObj(actor, obj)
                }
                for (loc in zone.locs.filter(GameObject::spawned)) {
                    updates.addLoc(actor, loc)
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
        if (objRequests.containsKey(floorItem)) return false
        objRequests[floorItem] = false
        return true
    }

    /**
     * Requests a floor item to be added to this zone.
     * Returns true if this floor item is able to be added.
     */
    fun requestAddObj(floorItem: FloorItem): Boolean {
        if (objRequests.containsKey(floorItem)) return false
        objRequests[floorItem] = true
        return true
    }

    /**
     * Requests a game object to be removed from this zone.
     * Returns true if this game object is able to be removed.
     * Returns false if this game object is already requested to be removed.
     */
    fun requestRemoveLoc(gameObject: GameObject): Boolean {
        if (locRequests.containsKey(gameObject)) return false
        locRequests[gameObject] = false
        return true
    }

    /**
     * Requests a game object to be added to this zone.
     * Returns true if this game object is able to be added.
     */
    fun requestAddLoc(gameObject: GameObject): Boolean {
        if (locRequests.containsKey(gameObject)) return false
        locRequests[gameObject] = true
        return true
    }

    /**
     * Requests a map proj anim to be added to this zone.
     * Returns true if this map proj anim is able to be added.
     */
    fun requestAddMapProjAnim(projectile: Projectile): Boolean {
        if (projectile in mapProjRequests) return false
        mapProjRequests += projectile
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
    fun updating() = objRequests.isNotEmpty() || locRequests.isNotEmpty() || mapProjRequests.isNotEmpty()

    /**
     * Returns a list of zones that are neighboring this zone including this zone.
     * This will always be in a 7x7 square area with this zone in the middle.
     */
    private fun neighboringZones(): Set<Zone> {
        val zones = mutableSetOf<Zone>()
        for (x in -3..3) {
            for (z in -3..3) {
                zones.add(world.zone(location.toZoneLocation().transform(x, z).toFullLocation()))
            }
        }
        return zones
    }

    private companion object {
        val world by inject<World>()
    }
}

@JvmInline
value class LocalLocation(val packedLocation: Int) {
    private val x get() = packedLocation shr 8
    private val z get() = packedLocation and 0xff
    private val offsetX get() = x - ((x shr 3) shl 3)
    private val offsetZ get() = z - ((z shr 3) shl 3)
    val packedOffset get() = (offsetX shl 4) or offsetZ
}

private fun Location.toLocalLocation(other: Location) = LocalLocation(localX(other) shl 8 or localZ(other))

/**
 * Adds a ObjAddPacket to this updates map.
 */
private fun MutableMap<Player, MutableList<Packet>>.addObj(player: Player, floorItem: FloorItem) {
    val current = this[player] ?: mutableListOf()
    current += ObjAddPacket(
        id = floorItem.id,
        amount = floorItem.amount,
        packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation).packedOffset
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
        packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation).packedOffset
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
        packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation).packedOffset
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
        packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation).packedOffset
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
        val localX = baseLocation.localX(player.lastLoadedLocation)
        val localZ = baseLocation.localZ(player.lastLoadedLocation)
        if (entry.value.size == 1) {
            player.write(UpdateZonePartialFollowsPacket(localX, localZ))
            player.write(entry.value.first())
        } else {
            val bytes = buildPacket {
                for (packet in entry.value) {
                    writeByte(ZoneUpdate.zoneUpdateMap[packet::class]!!::index)
                    val assembler = PacketAssemblerListener.listeners[packet::class]!!
                    writeBytes(assembler.packet.invoke(packet)::readBytes)
                }
            }.readBytes()
            player.write(UpdateZonePartialEnclosedPacket(localX, localZ, bytes))
        }
    }
}
