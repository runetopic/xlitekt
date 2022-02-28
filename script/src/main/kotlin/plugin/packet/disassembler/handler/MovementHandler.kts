package plugin.packet.disassembler.handler

import org.rsmod.pathfinder.collision.CollisionStrategies
import org.rsmod.pathfinder.reach.DefaultReachStrategy
import xlitekt.game.actor.player.message
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.pathfinder.DEFAULT_ACCESS_BITMASK
import xlitekt.game.pathfinder.DEFAULT_DEST_HEIGHT
import xlitekt.game.pathfinder.DEFAULT_DEST_WIDTH
import xlitekt.game.pathfinder.DEFAULT_MAX_TURNS
import xlitekt.game.pathfinder.DEFAULT_MOVE_NEAR_FLAG
import xlitekt.game.pathfinder.DEFAULT_OBJ_ROT
import xlitekt.game.pathfinder.DEFAULT_OBJ_SHAPE
import xlitekt.game.pathfinder.DEFAULT_SRC_SIZE
import xlitekt.game.pathfinder.pathFinder


onPacketHandler<MovementPacket> {
    val path = pathFinder.findPath(
        srcX = player.location.x,
        srcY = player.location.z,
        destX = this.packet.destinationX,
        destY = this.packet.destinationZ,
        z = player.location.level,
        srcSize = DEFAULT_SRC_SIZE,
        destWidth = DEFAULT_DEST_WIDTH,
        destHeight = DEFAULT_DEST_HEIGHT,
        objRot = DEFAULT_OBJ_ROT,
        objShape = DEFAULT_OBJ_SHAPE,
        moveNear = DEFAULT_MOVE_NEAR_FLAG,
        accessBitMask = DEFAULT_ACCESS_BITMASK,
        maxTurns = DEFAULT_MAX_TURNS,
        collision = CollisionStrategies.Normal,
        reachStrategy = DefaultReachStrategy
    )

    player.message("Coords $path")
}
