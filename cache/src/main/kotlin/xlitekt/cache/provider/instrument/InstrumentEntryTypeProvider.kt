package xlitekt.cache.provider.instrument

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.cache.provider.EntryTypeProvider
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class InstrumentEntryTypeProvider : EntryTypeProvider<InstrumentEntryType>() {

    override fun load(): Map<Int, InstrumentEntryType> = store
        .index(INSTRUMENT_INDEX)
        .groups()
        .map { ByteReadPacket(it.data).loadEntryType(InstrumentEntryType(it.id)) }
        .associateBy(InstrumentEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: InstrumentEntryType): InstrumentEntryType {
        type.instrumentSamples = Array(128) { null }
        type.field3113 = ShortArray(128)
        type.field3111 = ByteArray(128)
        type.field3115 = ByteArray(128)
        type.field3117 = arrayOfNulls(128)
        type.field3119 = ByteArray(128)
        type.groupIdOffsets = IntArray(128)
        val buffer = ByteBuffer.wrap(this.readBytes())

        // ==================================================================

        var sizeToFirstZero = 0
        while (buffer.array()[sizeToFirstZero + buffer.position()].toInt() != 0) {
            ++sizeToFirstZero
        }
        val firstArrayBlock = ByteArray(sizeToFirstZero)
        repeat(sizeToFirstZero) {
            firstArrayBlock[it] = buffer.get()
        }
        buffer.position(buffer.position() + 1)
        ++sizeToFirstZero
        var firstArrayBlockPosition = buffer.position()
        buffer.position(buffer.position() + sizeToFirstZero)

        // ==================================================================

        var sizeToSecondZero = 0
        while (buffer.array()[sizeToSecondZero + buffer.position()].toInt() != 0) {
            ++sizeToSecondZero
        }
        val secondArrayBlock = ByteArray(sizeToSecondZero)
        repeat(sizeToSecondZero) {
            secondArrayBlock[it] = buffer.get()
        }
        buffer.position(buffer.position() + 1)
        ++sizeToSecondZero
        var secondArrayBlockPosition = buffer.position()
        buffer.position(buffer.position() + sizeToSecondZero)

        // ==================================================================

        var sizeToThirdZero = 0
        while (buffer.array()[sizeToThirdZero + buffer.position()].toInt() != 0) {
            ++sizeToThirdZero
        }
        val thirdArrayBlock = ByteArray(sizeToThirdZero)
        repeat(sizeToThirdZero) {
            thirdArrayBlock[it] = buffer.get()
        }
        buffer.position(buffer.position() + 1)
        ++sizeToThirdZero

        // ==================================================================

        val var36 = ByteArray(sizeToThirdZero)
        val var12 = if (sizeToThirdZero > 1) {
            var36[1] = 1
            var var13 = 1
            var value = 2
            var var14 = 2
            while (var14 < sizeToThirdZero) {
                var var41 = buffer.get().toInt() and 0xff
                var13 = if (var41 == 0) {
                    value++
                } else {
                    if (var41 <= var13) {
                        --var41
                    }
                    var41
                }
                var36[var14] = var13.toByte()
                ++var14
            }
            value
        } else {
            sizeToThirdZero
        }

        // ==================================================================

        val var37 = Array<Instrument?>(var12) { null }
        var var15: Instrument?
        repeat(var37.size) {
            val instrument = Instrument()
            var37[it] = instrument
            var15 = instrument
            val var40 = buffer.get().toInt() and 0xff
            if (var40 > 0) {
                var15!!.field3056 = ByteArray(var40 * 2)
            }
            val var401 = buffer.get().toInt() and 0xff
            if (var401 > 0) {
                var15!!.field3054 = ByteArray(var401 * 2 + 2)
                var15!!.field3054!![1] = 64
            }
        }

        // ==================================================================

        val var141 = buffer.get().toInt() and 0xff
        val var42 = if (var141 > 0) ByteArray(var141 * 2) else null
        val var1412 = buffer.get().toInt() and 0xff
        val var16 = if (var1412 > 0) ByteArray(var1412 * 2) else null

        // ==================================================================

        var var17 = 0
        while (buffer.array()[var17 + buffer.position()].toInt() != 0) {
            ++var17
        }
        val var18 = ByteArray(var17)
        repeat(var17) {
            var18[it] = buffer.get()
        }
        buffer.position(buffer.position() + 1)
        ++var17

        // ==================================================================

        var var19 = 0
        repeat(128) {
            var19 += buffer.get().toInt() and 0xff
            type.field3113!![it] = var19.toShort()
        }

        // ==================================================================

        var19 = 0
        var var48: ShortArray
        repeat(128) {
            var19 += buffer.get().toInt() and 0xff
            var48 = type.field3113!!
            var48[it] = (var48[it] + (var19 shl 8)).toShort()
        }

        // ==================================================================

        var var20 = 0
        var var21 = 0
        var var22 = 0
        repeat(128) {
            if (var20 == 0) {
                var20 = if (var21 < var18.size) var18[var21++].toInt() else -1
                var22 = buffer.readVarInt()
            }
            var48 = type.field3113!!
            var48[it] = (var48[it] + (var22 - 1 and 2 shl 14)).toShort()
            type.groupIdOffsets!![it] = var22
            --var20
        }

        // ==================================================================

        var20 = 0
        var21 = 0
        var var23 = 0
        repeat(128) {
            if (type.groupIdOffsets!![it] != 0) {
                if (var20 == 0) {
                    var20 = if (var21 < firstArrayBlock.size) firstArrayBlock[var21++].toInt() else -1
                    var23 = buffer.array()[firstArrayBlockPosition++] - 1
                }
                type.field3119!![it] = var23.toByte()
                --var20
            }
        }

        // ==================================================================

        var20 = 0
        var21 = 0
        var var24 = 0
        repeat(128) {
            if (type.groupIdOffsets!![it] != 0) {
                if (var20 == 0) {
                    var20 = if (var21 < secondArrayBlock.size) secondArrayBlock[var21++].toInt() else -1
                    var24 = buffer.array()[secondArrayBlockPosition++] + 16 shl 2
                }
                type.field3115!![it] = var24.toByte()
                --var20
            }
        }

        // ==================================================================

        var20 = 0
        var21 = 0
        var var38: Instrument? = null
        repeat(128) {
            if (type.groupIdOffsets!![it] != 0) {
                if (var20 == 0) {
                    var38 = var37[var36[var21].toInt()]!!
                    var20 = if (var21 < thirdArrayBlock.size) thirdArrayBlock[var21++].toInt() else -1
                }
                type.field3117!![it] = var38
                --var20
            }
        }

        // ==================================================================

        var20 = 0
        var21 = 0
        var var26 = 0
        repeat(128) {
            if (var20 == 0) {
                var20 = if (var21 < var18.size) var18[var21++].toInt() else -1
                if (type.groupIdOffsets!![it] > 0) {
                    var26 = (buffer.get().toInt() and 0xff) + 1
                }
            }
            type.field3111!![it] = var26.toByte()
            --var20
        }

        // ==================================================================

        type.field3114 = (buffer.get().toInt() and 0xff) + 1

        // ==================================================================

        var var29: Int
        var var39: Instrument
        repeat(var12) {
            var39 = var37[it]!!
            if (var39.field3056 != null) {
                var29 = 1
                while (var29 < var39.field3056!!.size) {
                    var39.field3056!![var29] = buffer.get()
                    var29 += 2
                }
            }
            if (var39.field3054 != null) {
                var29 = 3
                while (var29 < var39.field3054!!.size - 2) {
                    var39.field3054!![var29] = buffer.get()
                    var29 += 2
                }
            }
        }

        // ==================================================================

        if (var42 != null) {
            var var27 = 1
            while (var27 < var42.size) {
                var42[var27] = buffer.get()
                var27 += 2
            }
        }

        if (var16 != null) {
            var var27 = 1
            while (var27 < var16.size) {
                var16[var27] = buffer.get()
                var27 += 2
            }
        }

        // ==================================================================

        repeat(var12) {
            var39 = var37[it]!!
            if (var39.field3054 != null) {
                var19 = 0
                var29 = 2
                while (var29 < var39.field3054!!.size) {
                    var19 += 1 + (buffer.get().toInt() and 0xff)
                    var39.field3054!![var29] = var19.toByte()
                    var29 += 2
                }
            }
        }

        // ==================================================================

        repeat(var12) {
            var39 = var37[it]!!
            if (var39.field3056 != null) {
                var19 = 0
                var29 = 2
                while (var29 < var39.field3056!!.size) {
                    var19 += 1 + (buffer.get().toInt() and 0xff)
                    var39.field3056!![var29] = var19.toByte()
                    var29 += 2
                }
            }
        }

        // ==================================================================

        var var30: Byte
        var var32: Int
        var var33: Int
        var var34: Int
        var var45: Int
        var var47: Byte
        if (var42 != null) {
            var19 = buffer.get().toInt() and 0xff
            var42[0] = var19.toByte()

            var var27 = 2
            while (var27 < var42.size) {
                var19 += 1 + (buffer.get().toInt() and 0xff)
                var42[var27] = var19.toByte()
                var27 += 2
            }

            var47 = var42[0]
            var var28 = var42[1]

            repeat(var47.toInt()) {
                type.field3111!![it] = (var28 * type.field3111!![it] + 32 shr 6).toByte()
            }

            var29 = 2
            while (var29 < var42.size) {
                var30 = var42[var29]
                val var31 = var42[var29 + 1]
                var32 = var28 * (var30 - var47) + (var30 - var47) / 2
                var33 = var47.toInt()
                while (var33 < var30) {
                    var34 = method4142(var32, var30 - var47)
                    type.field3111!![var33] = (var34 * type.field3111!![var33] + 32 shr 6).toByte()
                    var32 += var31 - var28
                    ++var33
                }
                var47 = var30
                var28 = var31
                var29 += 2
            }

            var45 = var47.toInt()
            while (var45 < 128) {
                type.field3111!![var45] = (var28 * type.field3111!![var45] + 32 shr 6).toByte()
                ++var45
            }
            var15 = null
        }

        // ==================================================================

        if (var16 != null) {
            var19 = buffer.get().toInt() and 0xff
            var16[0] = var19.toByte()

            var var27 = 2
            while (var27 < var16.size) {
                var19 += 1 + (buffer.get().toInt() and 0xff)
                var16[var27] = var19.toByte()
                var27 += 2
            }

            var47 = var16[0]
            var var44 = var16[1].toInt() shl 1

            repeat(var47.toInt()) {
                var45 = var44 + (type.field3115!![it].toInt() and 255)
                if (var45 < 0) {
                    var45 = 0
                }

                if (var45 > 128) {
                    var45 = 128
                }

                type.field3115!![it] = var45.toByte()
            }

            var var46: Int
            var29 = 2
            while (var29 < var16.size) {
                var30 = var16[var29]
                var46 = var16[var29 + 1].toInt() shl 1
                var32 = var44 * (var30 - var47) + (var30 - var47) / 2
                var33 = var47.toInt()
                while (var33 < var30) {
                    var34 = method4142(var32, var30 - var47)
                    var var35 = var34 + (type.field3115!![var33].toInt() and 255)
                    if (var35 < 0) {
                        var35 = 0
                    }
                    if (var35 > 128) {
                        var35 = 128
                    }
                    type.field3115!![var33] = var35.toByte()
                    var32 += var46 - var44
                    ++var33
                }
                var47 = var30
                var44 = var46
                var29 += 2
            }

            var45 = var47.toInt()
            while (var45 < 128) {
                var46 = var44 + (type.field3115!![var45].toInt() and 255)
                if (var46 < 0) {
                    var46 = 0
                }
                if (var46 > 128) {
                    var46 = 128
                }
                type.field3115!![var45] = var46.toByte()
                ++var45
            }
        }

        // ==================================================================

        repeat(var12) {
            var37[it]!!.field3052 = buffer.get().toInt() and 0xff
        }

        // ==================================================================

        repeat(var12) {
            var39 = var37[it]!!
            if (var39.field3056 != null) {
                var39.field3055 = buffer.get().toInt() and 0xff
            }

            if (var39.field3054 != null) {
                var39.field3053 = buffer.get().toInt() and 0xff
            }

            if (var39.field3052 > 0) {
                var39.field3057 = buffer.get().toInt() and 0xff
            }
        }

        // ==================================================================

        repeat(var12) {
            var37[it]!!.field3059 = buffer.get().toInt() and 0xff
        }

        repeat(var12) {
            var39 = var37[it]!!
            if (var39.field3059 > 0) {
                var39.field3058 = buffer.get().toInt() and 0xff
            }
        }

        repeat(var12) {
            var39 = var37[it]!!
            if (var39.field3058 > 0) {
                var39.field3060 = buffer.get().toInt() and 0xff
            }
        }
        return type
    }

    private fun ByteBuffer.readVarInt(): Int {
        var var1 = get().toInt()
        var var2 = 0
        while (var1 < 0) {
            var2 = (var2 or (var1 and 127)) shl 7
            var1 = get().toInt()
        }
        return var2 or var1
    }

    private fun method4142(var0: Int, var1: Int): Int {
        val var2 = var0 ushr 31
        return (var0 + var2) / var1 - var2
    }
}
