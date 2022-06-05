package xlitekt.game.world.map.zone

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import org.jctools.maps.NonBlockingHashSet
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
import xlitekt.game.world.map.GameObject
import xlitekt.game.world.map.Location
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
class Zone(
    val location: Location,
    val players: NonBlockingHashSet<Player> = NonBlockingHashSet(),
    val npcs: NonBlockingHashSet<NPC> = NonBlockingHashSet(),
    val locs: ArrayList<GameObject> = ArrayList(64),
    val objs: ArrayList<FloorItem> = ArrayList(),
) {
    private lateinit var neighboringZones: Set<Zone>
    private val objRequests = HashMap<FloorItem, Boolean>()
    private val locRequests = HashMap<GameObject, Boolean>()
    private val mapProjRequests = ArrayList<Projectile>()

    fun setNeighboringZones() {
        val zones = HashSet<Zone>(49)
        for (x in -3..3) {
            for (z in -3..3) {
                if (x == 0 && z == 0) zones.add(this)
                else zones.add(world.zone(location.toZoneLocation().transform(x, z).toFullLocation()))
            }
        }
        neighboringZones = zones
    }

    /**
     * Updates this zone to the neighboring players including players in this zone.
     * This happens every tick.
     */
    fun update() {
        val neighboring = neighboringPlayers()
        val updates = HashMap<Player, ArrayList<Packet>>(neighboring.size)
        if (mapProjRequests.isNotEmpty()) {
            for (projectile in mapProjRequests) {
                for (player in neighboring) {
                    updates.addMapProjAnim(this, player, projectile)
                }
            }
            mapProjRequests.clear()
        }

        if (objRequests.isNotEmpty()) {
            for (request in objRequests) {
                val obj = request.key
                val adding = request.value
                for (player in neighboring) {
                    if (adding) {
                        updates.addObj(this, player, obj)
                    } else {
                        updates.delObj(this, player, obj)
                    }
                }
                if (adding) {
                    objs.add(0, obj)
                } else {
                    objs.removeAt(objs.lastIndexOf(obj))
                }
            }
            objRequests.clear()
        }

        if (locRequests.isNotEmpty()) {
            for (request in locRequests) {
                val loc = request.key
                val adding = request.value
                for (player in neighboring) {
                    if (adding) {
                        updates.addLoc(this, player, loc)
                    } else {
                        updates.delLoc(this, player, loc)
                    }
                }
                if (adding) {
                    locs.add(0, loc)
                } else {
                    locs.removeAt(locs.filter(GameObject::spawned).lastIndexOf(loc))
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
        if (!::neighboringZones.isInitialized) setNeighboringZones()
        // This actor current zones.
        val zones = actor.zones()
        // Zones that are being removed from this actor current zones.
        val removed = zones.filter { it !in neighboringZones }
        // Zones that are being added to this actor current zones.
        val added = neighboringZones.filter { it !in zones }.filter {
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
                val updates = HashMap<Player, ArrayList<Packet>>(1)
                // If zone contains any of the following, send them to the client.
                for (obj in zone.objs) {
                    updates.addObj(this, actor, obj)
                }
                for (loc in zone.locs.filter(GameObject::spawned)) {
                    updates.addLoc(this, actor, loc)
                }
                updates.write(zone.location)
            }
            players.add(actor)
        } else if (actor is NPC) {
            npcs.add(actor)
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
     * By default, the range is limited to a standard 7x7 build area.
     */
    fun neighboringPlayers() = neighboringZones.filter(Zone::active).map(Zone::players).flatten()

    /**
     * Returns a list of npcs that are inside this zone and neighboring zones.
     * By default, the range is limited to a 5x5 area since by default npcs are only visible within 15 tiles.
     */
    fun neighboringNpcs() = neighboringZones.filter(Zone::active).map(Zone::npcs).flatten()

    /**
     * Returns a list of game objects that are inside this zone and neighboring zones.
     * By default, the range is limited to a standard 7x7 build area.
     */
    fun neighboringLocs() = neighboringZones.filter(Zone::active).map(Zone::locs).flatten()

    /**
     * Returns a list of floor items that are inside this zone and neighboring zones.
     * By default, the range is limited to a standard 7x7 build area.
     */
    fun neighboringObjs() = neighboringZones.filter(Zone::active).map(Zone::objs).flatten()

    /**
     * Returns if this zone is active or not.
     */
    fun active() = players.isNotEmpty() || npcs.isNotEmpty() || objs.isNotEmpty() || locs.any(GameObject::spawned) || updating()

    /**
     * Returns if this zone needs updating or not.
     */
    fun updating() = objRequests.isNotEmpty() || locRequests.isNotEmpty() || mapProjRequests.isNotEmpty()

    /**
     * Returns the amount of update requests this zone currently has.
     */
    internal fun requestSize() = objRequests.size + locRequests.size + mapProjRequests.size

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
private fun HashMap<Player, ArrayList<Packet>>.addObj(zone: Zone, player: Player, floorItem: FloorItem) {
    if (this[player] == null) this[player] = ArrayList(zone.requestSize())
    this[player]?.plusAssign(
        ObjAddPacket(
            id = floorItem.id,
            amount = floorItem.amount,
            packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation).packedOffset
        )
    )
}

/**
 * Adds a ObjDelPacket to this updates map.
 */
private fun HashMap<Player, ArrayList<Packet>>.delObj(zone: Zone, player: Player, floorItem: FloorItem) {
    if (this[player] == null) this[player] = ArrayList(zone.requestSize())
    this[player]?.plusAssign(
        ObjDelPacket(
            id = floorItem.id,
            packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation).packedOffset
        )
    )
}

/**
 * Adds a LocAddPacket to this updates map.
 */
private fun HashMap<Player, ArrayList<Packet>>.addLoc(zone: Zone, player: Player, gameObject: GameObject) {
    if (this[player] == null) this[player] = ArrayList(zone.requestSize())
    this[player]?.plusAssign(
        LocAddPacket(
            id = gameObject.id,
            shape = gameObject.shape,
            rotation = gameObject.rotation,
            packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation).packedOffset
        )
    )
}

/**
 * Adds a LocDelPacket to this updates map.
 */
private fun HashMap<Player, ArrayList<Packet>>.delLoc(zone: Zone, player: Player, gameObject: GameObject) {
    if (this[player] == null) this[player] = ArrayList(zone.requestSize())
    this[player]?.plusAssign(
        LocDelPacket(
            shape = gameObject.shape,
            rotation = gameObject.rotation,
            packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation).packedOffset
        )
    )
}

/**
 * Adds a MapProjAnimPacket to this updates map.
 */
private fun HashMap<Player, ArrayList<Packet>>.addMapProjAnim(zone: Zone, player: Player, projectile: Projectile) {
    if (this[player] == null) this[player] = ArrayList(zone.requestSize())
    this[player]?.plusAssign(
        MapProjAnimPacket(
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
    )
}

/**
 * Writes this map of updates to the players contained within this map. Uses the zone base location.
 */
private fun HashMap<Player, ArrayList<Packet>>.write(baseLocation: Location) {
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
