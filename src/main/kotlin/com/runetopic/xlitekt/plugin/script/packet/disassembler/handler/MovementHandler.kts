package com.runetopic.xlitekt.plugin.script.packet.disassembler.handler

import com.runetopic.xlitekt.game.actor.player.message
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_ACCESS_BITMASK
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_DEST_HEIGHT
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_DEST_WIDTH
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_MAX_TURNS
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_MOVE_NEAR_FLAG
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_OBJ_ROT
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_OBJ_SHAPE
import com.runetopic.xlitekt.game.pathfinder.DEFAULT_SRC_SIZE
import com.runetopic.xlitekt.game.pathfinder.pathFinder
import com.runetopic.xlitekt.network.packet.MovementPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacketHandler
import org.rsmod.pathfinder.collision.CollisionStrategies
import org.rsmod.pathfinder.reach.DefaultReachStrategy


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
