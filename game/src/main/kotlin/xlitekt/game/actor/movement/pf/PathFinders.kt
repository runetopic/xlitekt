package xlitekt.game.actor.movement.pf

import io.ktor.server.application.ApplicationEnvironment
import org.rsmod.pathfinder.DumbPathFinder
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
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
