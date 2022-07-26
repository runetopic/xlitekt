package xlitekt.game.actor.movement.pf

import io.ktor.server.application.ApplicationEnvironment
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.LineValidator
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.reach.DefaultReachStrategy
import xlitekt.game.actor.Actor
import xlitekt.game.world.map.GameObject
import xlitekt.shared.inject
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * @author Jordan Abraham
 */
private val environment by inject<ApplicationEnvironment>()
private val zoneFlags by inject<ZoneFlags>()

private const val DEFAULT_DEST_WIDTH = 0
private const val DEFAULT_DEST_HEIGHT = 0
private const val DEFAULT_OBJ_ROT = 10
private const val DEFAULT_OBJ_SHAPE = -1

private val dumb = DumbPathFinder(
    flags = zoneFlags.flags,
    defaultFlag = 0
)

private val smart = ConcurrentLinkedDeque<SmartPathFinder>().also {
    repeat(environment.config.property("game.cores").getString().toInt()) { _ ->
        it.add(
            SmartPathFinder(
                flags = zoneFlags.flags,
                defaultFlag = 0,
                useRouteBlockerFlags = true
            )
        )
    }
}

object PathFinders {
    private const val SIZE = 128
    private const val DEFAULT_FLAG = 0
    private const val USE_ROUTE_BLOCKER_FLAGS = true
    private val flags: Array<IntArray?> = zoneFlags.flags
    private val lineValidator = LineValidator(searchMapSize = SIZE, flags = flags, defaultFlag = DEFAULT_FLAG)

    fun hasLineOfSight(
        srcX: Int,
        srcZ: Int,
        destX: Int,
        destZ: Int,
        z: Int,
        srcSize: Int = -1,
        destWidth: Int = 0,
        destHeight: Int = 0,
    ): Boolean = lineValidator.hasLineOfSight(srcX, srcZ, z, destX, destZ, srcSize, destWidth, destHeight)

    fun hasLineOfWalk(
        srcX: Int,
        srcZ: Int,
        destX: Int,
        destZ: Int,
        z: Int,
        srcSize: Int = -1,
        destWidth: Int = 0,
        destHeight: Int = 0,
    ): Boolean = lineValidator.hasLineOfWalk(srcX, srcZ, z, destX, destZ, srcSize, destWidth, destHeight)

    fun reached(source: Actor, destination: GameObject): Boolean {
        if (source.location.level != destination.location.level) return false
        return DefaultReachStrategy.reached(
            zoneFlags.flags,
            DEFAULT_FLAG,
            x = source.location.x,
            y = source.location.z,
            z = source.location.level,
            destX = destination.location.x,
            destY = destination.location.z,
            destHeight = destination.entry?.height ?: 1,
            destWidth = destination.entry?.width ?: 1,
            srcSize = source.size(), // TODO these needs to be based on the actors size
            rotation = destination.rotation,
            shape = destination.shape,
            accessBitMask = 0 // TODO access flags.
        )
    }

    fun findPath(
        smart: Boolean,
        srcX: Int,
        srcZ: Int,
        destX: Int,
        destZ: Int,
        level: Int,
        destWidth: Int? = null,
        destHeight: Int? = null,
        rotation: Int? = null,
        shape: Int? = null
    ) = if (smart) findSmartPath(srcX, srcZ, destX, destZ, level, destWidth, destHeight, rotation, shape)
    else findDumbPath(srcX, srcZ, destX, destZ, level, destWidth, destHeight, rotation, shape)

    private fun findSmartPath(
        srcX: Int,
        srcZ: Int,
        destX: Int,
        destZ: Int,
        level: Int,
        destWidth: Int? = null,
        destHeight: Int? = null,
        rotation: Int? = null,
        shape: Int? = null
    ): Route {
        if (smart.isEmpty()) return findSmartPath(srcX, srcZ, destX, destZ, level, destWidth, destHeight, rotation, shape)
        val pathfinder = smart.removeLast()
        val route = pathfinder.findPath(
            srcX = srcX,
            srcY = srcZ,
            destX = destX,
            destY = destZ,
            z = level,
            destWidth = destWidth ?: DEFAULT_DEST_WIDTH,
            destHeight = destHeight ?: DEFAULT_DEST_HEIGHT,
            objRot = rotation ?: DEFAULT_OBJ_ROT,
            objShape = shape ?: DEFAULT_OBJ_SHAPE
        )
        smart.addFirst(pathfinder)
        return route
    }

    private fun findDumbPath(
        srcX: Int,
        srcZ: Int,
        destX: Int,
        destZ: Int,
        level: Int,
        destWidth: Int? = null,
        destHeight: Int? = null,
        rotation: Int? = null,
        shape: Int? = null
    ): Route = dumb.findPath(
        srcX = srcX,
        srcY = srcZ,
        destX = destX,
        destY = destZ,
        z = level,
        destWidth = destWidth ?: DEFAULT_DEST_WIDTH,
        destHeight = destHeight ?: DEFAULT_DEST_HEIGHT,
        objRot = rotation ?: DEFAULT_OBJ_ROT,
        objShape = shape ?: DEFAULT_OBJ_SHAPE
    )
}
