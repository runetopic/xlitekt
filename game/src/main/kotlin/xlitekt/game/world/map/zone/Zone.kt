package xlitekt.game.world.map.zone

import io.ktor.util.moveToByteArray
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
import xlitekt.game.world.map.localX
import xlitekt.game.world.map.localZ
import xlitekt.game.world.map.zone.Zone.Companion.zoneAssemblers
import xlitekt.game.world.map.zone.Zone.Companion.zoneUpdatesIndexes
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.inject
import xlitekt.shared.lazyInject
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class Zone(
    val location: Location,
    val players: NonBlockingHashSet<Player> = NonBlockingHashSet(),
    val npcs: NonBlockingHashSet<NPC> = NonBlockingHashSet(),
    private val locs: HashSet<GameObject> = HashSet(64),
    val locsSpawned: HashSet<GameObject> = HashSet(),
    val objsSpawned: HashSet<FloorItem> = HashSet(),
) {
    private lateinit var neighboringZones: Set<Zone>
    private val objRequests = HashMap<FloorItem, Boolean>()
    private val locRequests = HashMap<GameObject, Boolean>()
    private val mapProjRequests = HashSet<Projectile>()

    /**
     * Updates a player with updates about this zone.
     * This happens every tick.
     */
    fun invokeUpdateRequests(player: Player): Zone {
        val updates = HashSet<Packet>(requestSize())
        for (request in mapProjRequests) {
            updates.addMapProjAnim(request)
        }
        for (request in objRequests) {
            val obj = request.key
            if (request.value) updates.addObj(player, obj) else updates.delObj(player, obj)
        }
        for (request in locRequests) {
            val loc = request.key
            if (request.value) updates.addLoc(player, loc) else updates.delLoc(player, loc)
        }
        updates.write(player, location)
        return this
    }

    /**
     * Finalizes this zone update requests.
     * Updates corresponding collections in this zone and clears the update requests.
     */
    fun finalizeUpdateRequests() {
        if (mapProjRequests.isNotEmpty()) {
            mapProjRequests.clear()
        }
        if (objRequests.isNotEmpty()) {
            for (request in objRequests) {
                val obj = request.key
                if (request.value) objsSpawned.add(obj) else objsSpawned.remove(objsSpawned.first { it.id == obj.id && it.location == obj.location })
            }
            objRequests.clear()
        }
        if (locRequests.isNotEmpty()) {
            for (request in locRequests) {
                val loc = request.key
                if (request.value) locsSpawned.add(loc) else locsSpawned.remove(locsSpawned.first { it.id == loc.id && it.location == loc.location })
            }
            locRequests.clear()
        }
    }

    /**
     * Make an actor enter this zone.
     */
    fun enterZone(actor: Actor) {
        if (!::neighboringZones.isInitialized) setNeighboringZones()
        // This actor current zones.
        val zones = actor.zones
        // Zones that are being removed from this actor current zones.
        val removed = zones - neighboringZones
        // Zones that are being added to this actor current zones.
        val added = (neighboringZones - zones).filter {
            if (actor is Player) {
                val localX = it.location.localX(actor.lastLoadedLocation)
                val localZ = it.location.localZ(actor.lastLoadedLocation)
                localX in 0 until 104 && localZ in 0 until 104
            } else true
        }

        actor.setZones(removed, added.toSet())

        if (actor is Player) {
            for (zone in added) {
                // Clear the zone for updates.
                actor.write(UpdateZoneFullFollowsPacket(zone.location.localX(actor.lastLoadedLocation), zone.location.localZ(actor.lastLoadedLocation)))
                if (!zone.active()) {
                    continue
                }
                val updates = HashSet<Packet>(requestSize())
                // If zone contains any of the following, send them to the client.
                for (obj in zone.objsSpawned.filter { it !in objRequests }) {
                    // Filter obj requests out as they will be added later in the loop.
                    updates.addObj(actor, obj)
                }
                for (loc in zone.locsSpawned.filter { it !in locRequests }) {
                    // Filter loc requests out as they will be added later in the loop.
                    updates.addLoc(actor, loc)
                }
                updates.write(actor, zone.location)
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
    fun neighboringPlayers() = neighboringZones.map(Zone::players).flatten()

    /**
     * Returns a list of npcs that are inside this zone and neighboring zones.
     * By default, the range is limited to a 7x7 area since by default npcs are only visible within 15 tiles.
     */
    fun neighboringNpcs() = neighboringZones.map(Zone::npcs).flatten()

    /**
     * Returns a list of game objects that are inside this zone and neighboring zones.
     * By default, the range is limited to a standard 7x7 build area.
     */
    fun neighboringLocs() = neighboringZones.map { it.locs + it.locsSpawned }.flatten()

    /**
     * Returns a list of floor items that are inside this zone and neighboring zones.
     * By default, the range is limited to a standard 7x7 build area.
     */
    fun neighboringObjs() = neighboringZones.map(Zone::objsSpawned).flatten()

    /**
     * Returns if this zone is active or not.
     */
    fun active() = players.isNotEmpty() || npcs.isNotEmpty() || objsSpawned.isNotEmpty() || locsSpawned.isNotEmpty() || updating()

    /**
     * Returns if this zone needs updating or not.
     */
    fun updating() = objRequests.isNotEmpty() || locRequests.isNotEmpty() || mapProjRequests.isNotEmpty()

    /**
     * Returns the amount of update requests this zone currently has.
     */
    private fun requestSize() = objRequests.size + locRequests.size + mapProjRequests.size

    /**
     * Initializes this zone neighboring zones into memory.
     */
    private fun setNeighboringZones() {
        val zones = HashSet<Zone>(49)
        for (x in -3..3) {
            for (z in -3..3) {
                if (x == 0 && z == 0) zones.add(this)
                else zones.add(lazyInject<World>().zone(location.zoneLocation.transform(x, z).location))
            }
        }
        neighboringZones = zones
    }

    internal fun addCollisionLoc(gameObject: GameObject) {
        locs.add(gameObject)
    }

    internal companion object {
        val zoneUpdatesIndexes = mapOf(
            ObjAddPacket::class to 4,
            ObjDelPacket::class to 7,
            LocAddPacket::class to 6,
            LocDelPacket::class to 0,
            MapProjAnimPacket::class to 8
        )
        val zoneAssemblers by inject<PacketAssemblerListener>()
    }
}

@JvmInline
value class LocalLocation(val packedLocation: Int) {
    inline val x get() = packedLocation shr 8
    inline val z get() = packedLocation and 0xff
    inline val offsetX get() = x - ((x shr 3) shl 3)
    inline val offsetZ get() = z - ((z shr 3) shl 3)
    inline val packedOffset get() = (offsetX shl 4) or offsetZ
}

private fun Location.toLocalLocation(other: Location) = LocalLocation(localX(other) shl 8 or localZ(other))

/**
 * Adds a ObjAddPacket to this updates map.
 */
private fun HashSet<Packet>.addObj(player: Player, floorItem: FloorItem) = add(
    ObjAddPacket(
        id = floorItem.id,
        amount = floorItem.amount,
        packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation).packedOffset
    )
)

/**
 * Adds a ObjDelPacket to this updates map.
 */
private fun HashSet<Packet>.delObj(player: Player, floorItem: FloorItem) = add(
    ObjDelPacket(
        id = floorItem.id,
        packedOffset = floorItem.location.toLocalLocation(player.lastLoadedLocation).packedOffset
    )
)

/**
 * Adds a LocAddPacket to this updates map.
 */
private fun HashSet<Packet>.addLoc(player: Player, gameObject: GameObject) = add(
    LocAddPacket(
        id = gameObject.id,
        shape = gameObject.shape,
        rotation = gameObject.rotation,
        packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation).packedOffset
    )
)

/**
 * Adds a LocDelPacket to this updates map.
 */
private fun HashSet<Packet>.delLoc(player: Player, gameObject: GameObject) = add(
    LocDelPacket(
        shape = gameObject.shape,
        rotation = gameObject.rotation,
        packedOffset = gameObject.location.toLocalLocation(player.lastLoadedLocation).packedOffset
    )
)

/**
 * Adds a MapProjAnimPacket to this updates map.
 */
private fun HashSet<Packet>.addMapProjAnim(projectile: Projectile) = add(
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

/**
 * Writes this map of updates to the players contained within this map. Uses the zone base location.
 */
private fun HashSet<Packet>.write(player: Player, baseLocation: Location) {
    val localX = baseLocation.localX(player.lastLoadedLocation)
    val localZ = baseLocation.localZ(player.lastLoadedLocation)
    if (size == 1) {
        player.write(UpdateZonePartialFollowsPacket(localX, localZ))
        player.write(first())
        return
    }
    var bytes = byteArrayOf()
    for (packet in this) {
        val block = zoneAssemblers[packet::class]!!
        val buffer = ByteBuffer.allocate(1 + block.size)
        buffer.writeByte(zoneUpdatesIndexes[packet::class]!!)
        block.packet.invoke(packet, buffer)
        bytes += buffer.rewind().moveToByteArray()
    }
    player.write(UpdateZonePartialEnclosedPacket(localX, localZ, bytes))
}
