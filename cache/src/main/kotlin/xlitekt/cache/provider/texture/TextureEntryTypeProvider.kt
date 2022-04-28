package xlitekt.cache.provider.texture

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.toBoolean

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
        type.averageRGB = readUShort().toInt()
        type.field2206 = readUByte().toInt().toBoolean()
        val textureCount = readUByte().toInt()
        if (textureCount in 1..4) {
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
        }

        discard(remaining)
        assertEmptyAndRelease()
        return type
    }
}
