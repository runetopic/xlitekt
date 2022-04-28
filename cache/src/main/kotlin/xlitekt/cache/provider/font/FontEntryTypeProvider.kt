package xlitekt.cache.provider.font

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.cache.provider.sprite.SpriteEntryTypeProvider
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
class FontEntryTypeProvider : EntryTypeProvider<FontEntryType>() {

    private val sprites by inject<SpriteEntryTypeProvider>()

    override fun load(): Map<Int, FontEntryType> {
        val names = listOf(
            "verdana_15pt_regular",
            "p11_full",
            "verdana_11pt_regular",
            "p12_full",
            "verdana_13pt_regular",
            "b12_full"
        )
        val groups = names.map(store.index(SPRITE_INDEX)::group)
        return groups.flatMapIndexed { index, group ->
            store.index(FONT_INDEX).group(group.id).files().map { ByteReadPacket(it.data).loadEntryType(FontEntryType(group.id, name = names[index])) }
        }.associateBy(FontEntryType::id)
    }

    override fun ByteReadPacket.loadEntryType(type: FontEntryType): FontEntryType {
        val sprite = sprites.entryType(type.id) ?: return type
        type.offsetsX = sprite.offsetsX
        type.offsetsY = sprite.offsetsY
        type.widths = sprite.widths
        type.heights = sprite.heights

        val bytes = readBytes()
        assertEmptyAndRelease()

        val advances = IntArray(256)
        if (bytes.size == 257) {
            repeat(256) {
                advances[it] = bytes[it].toInt() and 0xff
            }
        } else {
            var index = 0
            repeat(256) {
                advances[it] = bytes[index++].toInt() and 0xff
            }

            val var10 = IntArray(256)
            val var4 = IntArray(256)

            repeat(256) {
                var10[it] = bytes[index++].toInt() and 0xff
            }

            repeat(256) {
                var4[it] = bytes[index++].toInt() and 0xff
            }

            val var11 = arrayOfNulls<ByteArray>(256)

            repeat(256) {
                var11[it] = ByteArray(var10[it])
                var var7 = 0
                repeat(var11[it]!!.size) { i ->
                    var7 += bytes[index++]
                    var11[it]!![i] = var7.toByte()
                }
            }

            val var12 = arrayOfNulls<ByteArray>(256)

            repeat(256) {
                var12[it] = ByteArray(var10[it])
                var var14 = 0
                repeat(var12[it]!!.size) { i ->
                    var14 += bytes[index++]
                    var12[it]!![i] = var14.toByte()
                }
            }

            type.kerning = ByteArray(65536)

            repeat(256) {
                if (it != 32 && it != 160) {
                    repeat(256) { i ->
                        if (i != 32 && i != 160) {
                            type.kerning!![i + (it shl 8)] = method6038(var11, var12, var4, advances, var10, it, i).toByte()
                        }
                    }
                }
            }

            type.ascent = var4[32] + var10[32]
        }

        var maxAscent = Int.MAX_VALUE
        var maxDescent = Int.MAX_VALUE

        repeat(256) {
            if (type.offsetsY!![it] < maxAscent && type.heights!![it] != 0) {
                maxAscent = type.offsetsY!![it]
            }

            if (type.offsetsY!![it] + type.heights!![it] > maxDescent) {
                maxDescent = type.offsetsY!![it] + type.heights!![it]
            }
        }

        return type
    }

    private fun method6038(var0: Array<ByteArray?>, var1: Array<ByteArray?>, var2: IntArray, advances: IntArray, var4: IntArray, var5: Int, var6: Int): Int {
        val var7 = var2[var5]
        val var8 = var7 + var4[var5]
        val var9 = var2[var6]
        val var10 = var9 + var4[var6]
        var var11 = var7
        if (var9 > var7) {
            var11 = var9
        }
        var var12 = var8
        if (var10 < var8) {
            var12 = var10
        }
        var kerning = advances[var5]
        if (advances[var6] < kerning) {
            kerning = advances[var6]
        }
        val var14 = var1[var5]
        val var15 = var0[var6]
        var var16 = var11 - var7
        var var17 = var11 - var9
        for (var18 in var11 until var12) {
            val var19 = var14!![var16++] + var15!![var17++]
            if (var19 < kerning) {
                kerning = var19
            }
        }
        return -kerning
    }
}
