package com.runetopic.xlitekt.game.collision

object CollisionFlag {

    const val WALL_NORTH_WEST: Int = 0x1
    const val WALL_NORTH: Int = 0x2
    const val WALL_NORTH_EAST: Int = 0x4
    const val WALL_EAST: Int = 0x8
    const val WALL_SOUTH_EAST: Int = 0x10
    const val WALL_SOUTH: Int = 0x20
    const val WALL_SOUTH_WEST: Int = 0x40
    const val WALL_WEST: Int = 0x80
    const val OBJECT: Int = 0x100
    const val WALL_NORTH_WEST_PROJECTILE_BLOCKER: Int = 0x200
    const val WALL_NORTH_PROJECTILE_BLOCKER: Int = 0x400
    const val WALL_NORTH_EAST_PROJECTILE_BLOCKER: Int = 0x800
    const val WALL_EAST_PROJECTILE_BLOCKER: Int = 0x1000
    const val WALL_SOUTH_EAST_PROJECTILE_BLOCKER: Int = 0x2000
    const val WALL_SOUTH_PROJECTILE_BLOCKER: Int = 0x4000
    const val WALL_SOUTH_WEST_PROJECTILE_BLOCKER: Int = 0x8000
    const val WALL_WEST_PROJECTILE_BLOCKER: Int = 0x10000
    const val OBJECT_PROJECTILE_BLOCKER: Int = 0x20000
    const val FLOOR_DECORATION: Int = 0x40000
    /*private const val NOT_IN_USE_BIT_20: Int = 0x80000 */

    /*private const val NOT_IN_USE_BIT_21: Int = 0x100000 */
    const val FLOOR: Int = 0x200000
    const val WALL_NORTH_WEST_ROUTE_BLOCKER: Int = 0x400000
    const val WALL_NORTH_ROUTE_BLOCKER: Int = 0x800000
    const val WALL_NORTH_EAST_ROUTE_BLOCKER: Int = 0x1000000
    const val WALL_EAST_ROUTE_BLOCKER: Int = 0x2000000
    const val WALL_SOUTH_EAST_ROUTE_BLOCKER: Int = 0x4000000
    const val WALL_SOUTH_ROUTE_BLOCKER: Int = 0x8000000
    const val WALL_SOUTH_WEST_ROUTE_BLOCKER: Int = 0x10000000
    const val WALL_WEST_ROUTE_BLOCKER: Int = 0x20000000
    const val OBJECT_ROUTE_BLOCKER: Int = 0x40000000
    /*private const val NOT_IN_USE_BIT_31: Int = 0x80000000*/

    /* A shorthand combination of both the floor flags. */
    private const val FLOOR_BLOCKED = FLOOR or FLOOR_DECORATION

    /* Mixed masks of the above flags */
    const val BLOCK_WEST: Int = WALL_EAST or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_EAST: Int = WALL_WEST or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_SOUTH: Int = WALL_NORTH or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_NORTH: Int = WALL_SOUTH or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_SOUTH_WEST: Int = WALL_NORTH or
        WALL_NORTH_EAST or
        WALL_EAST or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_SOUTH_EAST: Int = WALL_NORTH_WEST or
        WALL_NORTH or
        WALL_WEST or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_NORTH_WEST: Int = WALL_EAST or
        WALL_SOUTH_EAST or
        WALL_SOUTH or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_NORTH_EAST: Int = WALL_SOUTH or
        WALL_SOUTH_WEST or
        WALL_WEST or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_NORTH_AND_SOUTH_EAST: Int = WALL_NORTH or
        WALL_NORTH_EAST or
        WALL_EAST or
        WALL_SOUTH_EAST or
        WALL_SOUTH or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_NORTH_AND_SOUTH_WEST: Int = WALL_NORTH_WEST or
        WALL_NORTH or
        WALL_SOUTH or
        WALL_SOUTH_WEST or
        WALL_WEST or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_NORTH_EAST_AND_WEST: Int = WALL_NORTH_WEST or
        WALL_NORTH or
        WALL_NORTH_EAST or
        WALL_EAST or
        WALL_WEST or
        OBJECT or
        FLOOR_BLOCKED

    const val BLOCK_SOUTH_EAST_AND_WEST: Int = WALL_EAST or
        WALL_SOUTH_EAST or
        WALL_SOUTH or
        WALL_SOUTH_WEST or
        WALL_WEST or
        OBJECT or
        FLOOR_BLOCKED

    /* Route blocker flags. These are used in ~550+ clients to generate paths through bankers and such. */
    const val BLOCK_WEST_ROUTE_BLOCKER: Int = WALL_EAST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_EAST_ROUTE_BLOCKER: Int = WALL_WEST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_SOUTH_ROUTE_BLOCKER: Int = WALL_NORTH_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_NORTH_ROUTE_BLOCKER: Int = WALL_SOUTH_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_SOUTH_WEST_ROUTE_BLOCKER: Int = WALL_NORTH_ROUTE_BLOCKER or
        WALL_NORTH_EAST_ROUTE_BLOCKER or
        WALL_EAST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_SOUTH_EAST_ROUTE_BLOCKER: Int = WALL_NORTH_WEST_ROUTE_BLOCKER or
        WALL_NORTH_ROUTE_BLOCKER or
        WALL_WEST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_NORTH_WEST_ROUTE_BLOCKER: Int = WALL_EAST_ROUTE_BLOCKER or
        WALL_SOUTH_EAST_ROUTE_BLOCKER or
        WALL_SOUTH_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_NORTH_EAST_ROUTE_BLOCKER: Int = WALL_SOUTH_ROUTE_BLOCKER or
        WALL_SOUTH_WEST_ROUTE_BLOCKER or
        WALL_WEST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_NORTH_AND_SOUTH_EAST_ROUTE_BLOCKER: Int = WALL_NORTH_ROUTE_BLOCKER or
        WALL_NORTH_EAST_ROUTE_BLOCKER or
        WALL_EAST_ROUTE_BLOCKER or
        WALL_SOUTH_EAST_ROUTE_BLOCKER or
        WALL_SOUTH_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_NORTH_AND_SOUTH_WEST_ROUTE_BLOCKER: Int = WALL_NORTH_WEST_ROUTE_BLOCKER or
        WALL_NORTH_ROUTE_BLOCKER or
        WALL_SOUTH_ROUTE_BLOCKER or
        WALL_SOUTH_WEST_ROUTE_BLOCKER or
        WALL_WEST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_NORTH_EAST_AND_WEST_ROUTE_BLOCKER: Int = WALL_NORTH_WEST_ROUTE_BLOCKER or
        WALL_NORTH_ROUTE_BLOCKER or
        WALL_NORTH_EAST_ROUTE_BLOCKER or
        WALL_EAST_ROUTE_BLOCKER or
        WALL_WEST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
    const val BLOCK_SOUTH_EAST_AND_WEST_ROUTE_BLOCKER: Int = WALL_EAST_ROUTE_BLOCKER or
        WALL_SOUTH_EAST_ROUTE_BLOCKER or
        WALL_SOUTH_ROUTE_BLOCKER or
        WALL_SOUTH_WEST_ROUTE_BLOCKER or
        WALL_WEST_ROUTE_BLOCKER or
        OBJECT_ROUTE_BLOCKER or
        FLOOR_BLOCKED
}
