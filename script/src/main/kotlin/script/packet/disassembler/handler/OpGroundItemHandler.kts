import com.github.michaelbull.logging.InlineLogger
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.routeTo
import xlitekt.game.actor.speed
import xlitekt.game.actor.teleportTo
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.packet.OpGroundItemPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.Location
import xlitekt.shared.inject

/**
 * @author Justin Kenney
 */
private val zoneFlags by inject<ZoneFlags>()
private val pathfinder = SmartPathFinder(
    flags = zoneFlags.flags,
    defaultFlag = 0,
    useRouteBlockerFlags = true
)

private val logger = InlineLogger()

onPacketHandler<OpGroundItemPacket> {
//    logger.debug { packet }

    val destination = Location(this.packet.x, this.packet.z, player.location.level)

    // Teleport movement (ctrl+click teleporting)
    if (player.rights >= 2 && packet.isModified) {
        player.teleportTo { destination }
        return@onPacketHandler
    }

    // Toggles Actor's speed only for the duration of the movement (if isModified=true)
    player.speed { (VarPlayer.ToggleRun in player.vars).let { if (packet.isModified) !it else it } }

    // TODO change this to find the item within the zone and then angle it to the item.
    with(player) {
        cancelAll()
        routeTo(destination)
    }
}
