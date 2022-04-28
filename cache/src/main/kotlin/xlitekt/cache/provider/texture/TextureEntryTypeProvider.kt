package xlitekt.cache.provider.texture

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class TextureEntryTypeProvider : EntryTypeProvider<TextureEntryType>() {

    override fun load(): Map<Int, TextureEntryType> = store
        .index(TEXTURE_INDEX)
        .group(0)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(TextureEntryType(it.id)) }
        .associateBy(TextureEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: TextureEntryType): TextureEntryType {
        discard(2)
        discard(1)
        val textureCount = readUByte().toInt()
        type.textureIds = IntArray(textureCount)
        repeat(textureCount) {
            type.textureIds!![it] = readUShort().toInt()
        }

        if (textureCount > 1) {
            repeat(textureCount - 1) {
                discard(1)
            }
        }

        if (textureCount > 1) {
            repeat(textureCount - 1) {
                discard(1)
            }
        }

        repeat(textureCount) {
            discard(4)
        }

        type.animationDirection = readUByte().toInt()
        type.animationSpeed = readUByte().toInt()

        discard(remaining)
        assertEmptyAndRelease()
        return type
    }
}
