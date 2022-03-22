package script.packet.disassembler.handler

import xlitekt.game.actor.player.rebuildZones
import xlitekt.game.actor.player.shouldRefreshZones
import xlitekt.game.packet.MapBuildCompletePacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler

/**
 * @author Jordan Abraham
 */
onPacketHandler<MapBuildCompletePacket> {
    // player.rebuildZones(false)
    player.mapLoaded = true
}
