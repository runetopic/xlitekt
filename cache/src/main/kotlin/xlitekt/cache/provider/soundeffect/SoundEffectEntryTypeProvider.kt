package xlitekt.cache.provider.soundeffect

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.cache.provider.EntryTypeProvider
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class SoundEffectEntryTypeProvider : EntryTypeProvider<SoundEffectEntryType>() {

    override fun load(): Map<Int, SoundEffectEntryType> = store
        .index(SOUND_EFFECT_INDEX)
        .groups()
        .map { ByteReadPacket(it.data).loadEntryType(SoundEffectEntryType(it.id)) }
        .associateBy(SoundEffectEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: SoundEffectEntryType): SoundEffectEntryType {
        type.instruments = arrayOfNulls(10)
        val buffer = ByteBuffer.wrap(readBytes())
        repeat(10) {
            if (buffer.get().toInt() and 0xff != 0) {
                buffer.position(buffer.position() - 1)
                type.instruments!![it] = SoundEffectInstrument(buffer)
            }
        }
        type.start = buffer.short.toInt() and 0xffff
        type.end = buffer.short.toInt() and 0xffff
        return type
    }
}
