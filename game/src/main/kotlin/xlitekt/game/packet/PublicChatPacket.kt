package xlitekt.game.packet

data class PublicChatPacket(
    val unknown: Int,
    val color: Int,
    val effect: Int,
    val length: Int,
    val data: ByteArray
) : Packet {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublicChatPacket

        if (unknown != other.unknown) return false
        if (color != other.color) return false
        if (effect != other.effect) return false
        if (length != other.length) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = unknown
        result = 31 * result + color
        result = 31 * result + effect
        result = 31 * result + length.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
