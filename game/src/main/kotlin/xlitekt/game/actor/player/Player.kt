package xlitekt.game.actor.player

import kotlinx.serialization.Serializable
import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.Actor
import xlitekt.game.actor.movementType
import xlitekt.game.actor.player.serializer.PlayerSerializer
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.speed
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.game.content.container.inventory.Inventory
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.skill.Skills
import xlitekt.game.content.ui.Interfaces
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.content.vars.Vars
import xlitekt.game.event.EventBus
import xlitekt.game.event.impl.Events
import xlitekt.game.fs.PlayerJsonEncoderService
import xlitekt.game.packet.*
import xlitekt.game.packet.disassembler.handler.PacketHandler
import xlitekt.game.world.World
import xlitekt.game.world.map.Location
import xlitekt.shared.lazy
import kotlin.math.abs

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
@Serializable(with = PlayerSerializer::class)
class Player(
    override var location: Location = Location(3222, 3222),
    val username: String,
    val password: String,
    val rights: Int = 0,
    var weight: Float = 0f,
    val appearance: Render.Appearance = Render.Appearance().also { it.displayName = username },
    val skills: Skills = Skills(),
    var runEnergy: Float = 10_000f,
    var brandNew: Boolean = true
) : Actor(location) {
    val viewport = Viewport(this)
    val interfaces = Interfaces(this)
    val vars = Vars(this)
    val inventory: Inventory = Inventory(this)
    val equipment: Equipment = Equipment(this)
    var lastLoadedLocation = Location.None

    /**
     * This players connected client. This client is used for reading and writing packets.
     */
    private var client: Client? = null

    /**
     * This players online var. If true this player is processed by the game loop.
     */
    private var online = false

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    /**
     * Initiates this player when logging into the game world.
     * This happens before anything else.
     */
    internal fun init(client: Client, players: NonBlockingHashMapLong<Player>) {
        this.client = client
        previousLocation = location
        rebuildNormal(players) { true }
        interfaces.openTop(interfaces.currentInterfaceLayout.interfaceId)
        invokeAndClearWritePool()
        zone().enterZone(this)
        login()
    }

    /**
     * Login the player into the game world.
     */
    private fun login() {
        vars.login()
        interfaces.login()
        inventory.login()
        equipment.login()
        appearance.equipment = equipment
        renderAppearance()
        movementType { false }
        updateRunEnergy()
        speed { VarPlayer.ToggleRun in vars }
        lazy<EventBus>().notify(Events.OnLoginEvent(this))
        // Set the player online here, so they start processing by the main game loop.
        online = true
    }

    /**
     * Logout the player from the game world.
     */
    fun logout() {
        if (!online) return
        online = false
        write(LogoutPacket(0))
        invokeAndClearWritePool()
        zone().leaveZone(this)
        lazy<World>().removePlayer(this)
        lazy<PlayerJsonEncoderService>().requestSave(this)
    }

    fun isOnline() = online

    /**
     * Pools a packet to be sent to the client.
     */
    fun write(packet: Packet) = client?.addToWritePool(packet)

    /**
     * Pools a disassembled packet from the connected client.
     */
    fun read(packetHandler: PacketHandler<Packet>) = client?.addToReadPool(packetHandler)

    /**
     * Invokes and writes the pooled packets to the connected client.
     * The pool is then cleared after operation.
     * This happens every tick.
     */
    internal fun invokeAndClearWritePool() = client?.invokeAndClearWritePool()

    /**
     * Invokes and handles the pooled packets sent from the connected client.
     * This is used to keep the player synchronized with the game loop no matter their actions from the client.
     * The pool is then cleared after operation.
     * This happens every tick.
     */
    internal fun invokeAndClearReadPool() = client?.invokeAndClearReadPool()

    /**
     * Returns if this player needs a map rebuild.
     */
    internal fun shouldRebuildMap(size: Int = 104): Boolean {
        val lastZoneX = lastLoadedLocation.zoneX
        val lastZoneZ = lastLoadedLocation.zoneZ
        val zoneX = location.zoneX
        val zoneZ = location.zoneZ
        val limit = ((size shr 3) / 2) - 1
        return abs(lastZoneX - zoneX) >= limit || abs(lastZoneZ - zoneZ) >= limit
    }
}

inline fun Player.varp(id: Int, value: () -> Int) = value.invoke().also {
    if (it < Byte.MIN_VALUE || it > Byte.MAX_VALUE) {
        write(VarpLargePacket(id, it))
    } else {
        write(VarpSmallPacket(id, it))
    }
}

fun Player.updateStat(skill: Skill, level: Int, experience: Double) {
    write(UpdateStatPacket(skill.id, level, experience))
}

inline fun Player.message(message: () -> String) = write(MessageGamePacket(0, message.invoke(), false)) // TODO build messaging system
fun Player.script(scriptId: Int, vararg parameters: Any) = write(RunClientScriptPacket(scriptId, parameters))
fun Player.updateRunEnergy() = write(UpdateRunEnergyPacket(runEnergy / 100))

inline fun Player.rebuildNormal(players: NonBlockingHashMapLong<Player>, update: () -> Boolean) {
    write(RebuildNormalPacket(viewport, location, update.invoke(), players))
    lastLoadedLocation = location
}

fun Player.renderAppearance() = render(appearance)
