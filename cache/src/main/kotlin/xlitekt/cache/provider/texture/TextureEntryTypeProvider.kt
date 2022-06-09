package xlitekt.cache.provider.texture

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.toBoolean
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class TextureEntryTypeProvider : EntryTypeProvider<TextureEntryType>() {

    override fun load(): Map<Int, TextureEntryType> = store
        .index(TEXTURE_INDEX)
        .group(0)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(TextureEntryType(it.id)) }
        .associateBy(TextureEntryType::id)

    override fun ByteBuffer.loadEntryType(type: TextureEntryType): TextureEntryType {
        type.averageRGB = readUShort()
        type.field2206 = readUByte().toBoolean()
        val textureCount = readUByte()
        if (textureCount in 1..4) {
            type.textureIds = IntArray(textureCount)
            repeat(textureCount) {
                type.textureIds!![it] = readUShort()
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
            type.animationDirection = readUByte()
            type.animationSpeed = readUByte()
        }

        discard(array().size - position())
        assertEmptyAndRelease()
        return type
    }
}
