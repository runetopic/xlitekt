package script.packet.disassembler.handler

import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.routeTo
import xlitekt.game.actor.speed
import xlitekt.game.actor.teleportTo
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.Location

onPacketHandler<MovementPacket> {

    val destination = Location(packet.destinationX, packet.destinationZ, player.location.level)

    // Teleport movement (ctrl+click teleporting)
    if (player.rights >= 2 && packet.isModified) {
        player.teleportTo { destination }
        return@onPacketHandler
    }

    // Toggles Actor's speed only for the duration of the movement (if isModified=true)
    player.speed { (VarPlayer.ToggleRun in player.vars).let { if (packet.isModified) !it else it } }

    with(player) {
        cancelAll()
        routeTo(destination)
    }
}
