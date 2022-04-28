package xlitekt.cache.provider.sprite

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readFully
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUMedium

/**
 * @author Jordan Abraham
 */
class SpriteEntryTypeProvider : EntryTypeProvider<SpriteEntryType>() {

    override fun load(): Map<Int, SpriteEntryType> = store
        .index(SPRITE_INDEX)
        .groups()
        .map { ByteReadPacket(it.data).loadEntryType(SpriteEntryType(it.id)) }
        .associateBy(SpriteEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: SpriteEntryType): SpriteEntryType {
        val header = copy().spriteHeader()
        val spriteCount = header.readUShort().toInt()
        header.release()

        val offsetsX = IntArray(spriteCount)
        val offsetsY = IntArray(spriteCount)
        val widths = IntArray(spriteCount)
        val heights = IntArray(spriteCount)

        val buffer = copy().spriteBuffer(spriteCount)
        buffer.discard(2) // Width
        buffer.discard(2) // Height
        val paletteSize = (buffer.readUByte().toInt() and 0xff) + 1
        repeat(spriteCount) { offsetsX[it] = buffer.readUShort().toInt() }
        repeat(spriteCount) { offsetsY[it] = buffer.readUShort().toInt() }
        repeat(spriteCount) { widths[it] = buffer.readUShort().toInt() }
        repeat(spriteCount) { heights[it] = buffer.readUShort().toInt() }
        buffer.release()

        val palette = copy().spritePalette(spriteCount, paletteSize)
        val spritePalette = IntArray(paletteSize)
        for (index in 1 until spritePalette.size) {
            spritePalette[index] = palette.readUMedium().let { if (it == 0) 1 else it }
        }
        palette.release()

        // The sprites.
        repeat(spriteCount) { spriteId ->
            val spriteWidth = widths[spriteId]
            val spriteHeight = heights[spriteId]
            val dimension = spriteWidth * spriteHeight
            val indices = ByteArray(dimension)
            val alphas = ByteArray(dimension)

            val mask = readUByte().toInt()
            when {
                mask and 1 == 0 -> repeat(dimension) {
                    indices[it] = readByte()
                }
                else -> repeat(spriteWidth) { x ->
                    repeat(spriteHeight) { y ->
                        indices[x + spriteWidth * y] = readByte()
                    }
                }
            }
            val pixels = IntArray(dimension)
            repeat(dimension) {
                val i = indices[it].toInt()
                if (i != 0) {
                    alphas[it] = 0xff.toByte()
                }
                pixels[it] = spritePalette[i and 0xff] or (alphas[it].toInt() shl 24)
            }
            type.sprites = type.sprites.toMutableList().apply {
                add(Sprite(spriteId, spriteWidth, spriteHeight, pixels))
            }
        }

        discard(remaining)
        assertEmptyAndRelease()
        return type
    }

    private fun ByteReadPacket.spriteHeader(): ByteReadPacket {
        discard(remaining.toInt() - 2)
        val bytes = ByteArray(remaining.toInt())
        readFully(bytes)
        return ByteReadPacket(bytes)
    }

    private fun ByteReadPacket.spriteBuffer(spriteCount: Int): ByteReadPacket {
        discard(remaining.toInt() - 7 - spriteCount * 8)
        val bytes = ByteArray(remaining.toInt())
        readFully(bytes)
        return ByteReadPacket(bytes)
    }

    private fun ByteReadPacket.spritePalette(spriteCount: Int, paletteSize: Int): ByteReadPacket {
        discard(remaining.toInt() - 7 - spriteCount * 8 - (paletteSize - 1) * 3)
        val bytes = ByteArray(remaining.toInt())
        readFully(bytes)
        return ByteReadPacket(bytes)
    }
}
