package script.packet.assembler

import xlitekt.game.packet.RebuildNormalPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.inject
import xlitekt.shared.lazyInject
import xlitekt.shared.resource.MapSquares

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
private val mapSquares by inject<MapSquares>()

lazyInject<PacketAssemblerListener>().assemblePacket<RebuildNormalPacket>(opcode = 54, size = -2) {
    if (initialize) {
        viewport.init(it, players)
    }

    val zoneX = location.zoneX
    val zoneZ = location.zoneZ

    it.writeShortAdd(zoneZ)
    it.writeShortLittleEndian(zoneX)

    val zonesX = ((zoneX - 6) / 8..(zoneX + 6) / 8)
    val zonesZ = ((zoneZ - 6) / 8..(zoneZ + 6) / 8)

    it.writeShort(zonesX.count() * zonesZ.count())

    for (x in zonesX) {
        for (z in zonesZ) {
            val regionId = z + (x shl 8)
            val xteaKeys = mapSquares[regionId]?.key ?: listOf(0, 0, 0, 0)
            for (key in xteaKeys) {
                it.writeInt(key)
            }
        }
    }
}
