package xlitekt.game.packet

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.world.map.Location

/**
 * @author Jordan Abraham
 *
 * <b>Information</b>
 *
 * Represents the REBUILD_NORMAL server -> client packet.
 *
 * This packet is used to update the client with the location of the client player
 * and update the client on player gpi if the client is logging into the game world.
 *
 * <b>Assembly Example</b>
 *
 * ```
 * // If the client is logging into the game world.
 * if (initialize) {
 *      // Switch to bit modification on the received buffer.
 *      writeBits(30, location.packedLocation) // Our player location.
 *      for (index in 1 until 2048) {
 *          if (index == ourPlayerIndex) continue
 *          val otherPlayer = players[index] // A collection of players sorted by the player index which contains players that are currently online and ones that are not online.
 *          writeBits(18, otherPlayer?.location?.regionLocation ?: 0)
 *      }
 *      // Switch to byte modification on the received buffer.
 * }
 *
 * val zoneX = location.zoneX
 * val zoneZ = location.zoneZ
 *
 * writeShortAdd(zoneZ)
 * writeShortLittleEndian(zoneX)
 *
 * // A normal area of zones is a 7x7 square area (buildArea).
 * val numberOfZonesOnTheXAxis = ((zoneX - 6) / 8..(zoneX + 6) / 8)
 * val numberOfZonesOnTheZAxis = ((zoneX - 6) / 8..(zoneX + 6) / 8)
 *
 * writeShort(numberOfZonesOnTheXAxis.count() * numberOfZonesOnTheZAxis.count())
 *
 * for (x in numberOfZonesOnTheXAxis) {
 *      for (z in numberOfZonesOnTheZAxis) {
 *          // Four xtea keys used to decompress the maps data from the cache on the client side.
 *          val keys = intArrayOf(0, 0, 0, 0)
 *          for (key in keys) {
 *              writeInt(key)
 *          }
 *      }
 * }
 * ```
 *
 * @see Viewport.init
 * @see Location.packedLocation
 * @see Location.regionLocation
 * @see Location.zoneX
 * @see Location.zoneZ
 *
 * @property viewport The viewport of the [Player] which contains all the necessary information for the [PlayerInfoPacket].
 * @property location The location of the player.
 * @property initialize True if the client is logging into the game world. False if the client is already inside the game world.
 * @property players A map of players that are currently logged into the game world.
 */
data class RebuildNormalPacket(
    val viewport: Viewport,
    val location: Location,
    val initialize: Boolean,
    val players: NonBlockingHashMapLong<Player>
) : Packet
