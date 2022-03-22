package xlitekt.game.packet

import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
data class UpdateZonePartialEnclosedPacket(
    val packet: ByteReadPacket
) : Packet
