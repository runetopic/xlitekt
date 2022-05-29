package xlitekt.game.world.map.zone

/**
 * @author Jordan Abraham
 */
sealed class ZoneUpdateType {
    class ObjAddType(
        val itemId: Int,
        val amount: Int,
        val packedOffset: Int
    ) : ZoneUpdateType()

    class ObjDeleteType(
        val itemId: Int,
        val packedOffset: Int
    ) : ZoneUpdateType()
}
