package xlitekt.cache.provider.instrument

import io.ktor.util.copy
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.discardUntilDelimiter
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readVarInt
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class InstrumentEntryTypeProvider : EntryTypeProvider<InstrumentEntryType>() {

    override fun load(): Map<Int, InstrumentEntryType> = store
        .index(INSTRUMENT_INDEX)
        .groups()
        .map { ByteBuffer.wrap(it.data).loadEntryType(InstrumentEntryType(it.id)) }
        .associateBy(InstrumentEntryType::id)

    override fun ByteBuffer.loadEntryType(type: InstrumentEntryType): InstrumentEntryType {
        type.audioBuffers = Array(128) { null }
        type.pitchOffset = ShortArray(128)
        type.volumeOffset = ByteArray(128)
        type.panOffset = ByteArray(128)
        type.field3117 = arrayOfNulls(128)
        type.loopMode = ByteArray(128)
        type.offsets = IntArray(128)

        val firstArrayBlock = ByteArray(copy().discardUntilDelimiter(0)) { readByte().toByte() }
        var firstArrayBlockPosition = firstArrayBlock.size + 1 + 1
        discard(firstArrayBlock.size + 1 + 1)

        val secondArrayBlock = ByteArray(copy().discardUntilDelimiter(0)) { readByte().toByte() }
        var secondArrayBlockPosition = secondArrayBlock.size + 1 + 1
        discard(secondArrayBlock.size + 1 + 1)

        val thirdArrayBlock = ByteArray(copy().discardUntilDelimiter(0)) { readByte().toByte() }
        discard(1)

        val var36 = ByteArray(thirdArrayBlock.size + 1)
        val var12 = if (thirdArrayBlock.size + 1 > 1) {
            var36[1] = 1
            var var13 = 1
            var value = 2
            for (index in 2 until thirdArrayBlock.size + 1) {
                var var41 = readUByte()
                var13 = if (var41 == 0) {
                    value++
                } else {
                    if (var41 <= var13) {
                        --var41
                    }
                    var41
                }
                var36[index] = var13.toByte()
            }
            value
        } else {
            thirdArrayBlock.size + 1
        }

        var var15: Instrument?
        val instruments = Array<Instrument?>(var12) {
            val instrument = Instrument()
            var15 = instrument
            val var40 = readUByte()
            if (var40 > 0) {
                var15!!.field3056 = ByteArray(var40 * 2)
            }
            val var401 = readUByte()
            if (var401 > 0) {
                var15!!.field3054 = ByteArray(var401 * 2 + 2)
                var15!!.field3054!![1] = 64
            }
            instrument
        }

        val var141 = readUByte()
        val var42 = if (var141 > 0) ByteArray(var141 * 2) else null
        val var1412 = readUByte()
        val var16 = if (var1412 > 0) ByteArray(var1412 * 2) else null

        val var18 = ByteArray(copy().discardUntilDelimiter(0)) { readByte().toByte() }
        discard(1)

        var var19 = 0
        repeat(128) {
            var19 += readUByte()
            type.pitchOffset!![it] = var19.toShort()
        }

        var19 = 0
        repeat(128) {
            var19 += readUByte()
            type.pitchOffset!![it] = (type.pitchOffset!![it] + (var19 shl 8)).toShort()
        }

        var var20 = 0
        var var21 = 0
        var var22 = 0
        repeat(128) {
            if (var20 == 0) {
                var20 = if (var21 < var18.size) var18[var21++].toInt() else -1
                var22 = readVarInt()
            }
            type.pitchOffset!![it] = (type.pitchOffset!![it] + ((var22 - 1 and 2) shl 14)).toShort()
            type.offsets!![it] = var22
            --var20
        }

        var20 = 0
        var21 = 0
        var var23 = 0
        repeat(128) {
            if (type.offsets!![it] != 0) {
                if (var20 == 0) {
                    var20 = if (var21 < firstArrayBlock.size) firstArrayBlock[var21++].toInt() else -1
                    var23 = copy().array()[firstArrayBlockPosition++] - 1
                }
                type.loopMode!![it] = var23.toByte()
                --var20
            }
        }

        var20 = 0
        var21 = 0
        var var24 = 0
        repeat(128) {
            if (type.offsets!![it] != 0) {
                if (var20 == 0) {
                    var20 = if (var21 < secondArrayBlock.size) secondArrayBlock[var21++].toInt() else -1
                    var24 = copy().array()[secondArrayBlockPosition++] + 16 shl 2
                }
                type.panOffset!![it] = var24.toByte()
                --var20
            }
        }

        var20 = 0
        var21 = 0
        var var38: Instrument? = null
        repeat(128) {
            if (type.offsets!![it] != 0) {
                if (var20 == 0) {
                    var38 = instruments[var36[var21].toInt()]!!
                    var20 = if (var21 < thirdArrayBlock.size) thirdArrayBlock[var21++].toInt() else -1
                }
                type.field3117!![it] = var38
                --var20
            }
        }

        var20 = 0
        var21 = 0
        var var26 = 0
        repeat(128) {
            if (var20 == 0) {
                var20 = if (var21 < var18.size) var18[var21++].toInt() else -1
                if (type.offsets!![it] > 0) {
                    var26 = readUByte() + 1
                }
            }
            type.volumeOffset!![it] = var26.toByte()
            --var20
        }

        type.baseVelocity = readUByte() + 1

        repeat(var12) {
            val instrument = instruments[it]!!
            if (instrument.field3056 != null) {
                for (index in 1 until instrument.field3056!!.size step 2) {
                    instrument.field3056!![index] = readByte().toByte()
                }
            }
            if (instrument.field3054 != null) {
                for (index in 3 until instrument.field3054!!.size - 2 step 2) {
                    instrument.field3054!![index] = readByte().toByte()
                }
            }
        }

        if (var42 != null) {
            for (index in 1 until var42.size step 2) {
                var42[index] = readByte().toByte()
            }
        }

        if (var16 != null) {
            for (index in 1 until var16.size step 2) {
                var16[index] = readByte().toByte()
            }
        }

        repeat(var12) {
            val instrument = instruments[it]!!
            if (instrument.field3054 != null) {
                var var191 = 0
                for (index in 2 until instrument.field3054!!.size step 2) {
                    var191 += 1 + readUByte()
                    instrument.field3054!![index] = var191.toByte()
                }
            }
        }

        repeat(var12) {
            val instrument = instruments[it]!!
            if (instrument.field3056 != null) {
                var var191 = 0
                for (index in 2 until instrument.field3056!!.size step 2) {
                    var191 += 1 + readUByte()
                    instrument.field3056!![index] = var191.toByte()
                }
            }
        }

        if (var42 != null) {
            var var191 = readUByte()
            var42[0] = var191.toByte()

            for (index in 2 until var42.size step 2) {
                var191 += 1 + readUByte()
                var42[index] = var191.toByte()
            }

            var var47 = var42[0]
            var var28 = var42[1]

            repeat(var47.toInt()) {
                type.volumeOffset!![it] = (var28 * type.volumeOffset!![it] + 32 shr 6).toByte()
            }

            for (index in 2 until var42.size step 2) {
                val var30 = var42[index]
                val var31 = var42[index + 1]
                var var32 = var28 * (var30 - var47) + (var30 - var47) / 2
                for (volumeIndex in var47 until var30) {
                    val var34 = method4142(var32, var30 - var47)
                    type.volumeOffset!![volumeIndex] = (var34 * type.volumeOffset!![volumeIndex] + 32 shr 6).toByte()
                    var32 += var31 - var28
                }
                var47 = var30
                var28 = var31
            }

            for (index in var47 until 128) {
                type.volumeOffset!![index] = (var28 * type.volumeOffset!![index] + 32 shr 6).toByte()
            }
            var15 = null
        }

        if (var16 != null) {
            var var191 = readUByte()
            var16[0] = var191.toByte()

            for (index in 2 until var16.size step 2) {
                var191 += 1 + readUByte()
                var16[index] = var191.toByte()
            }

            var var47 = var16[0]
            var var44 = var16[1].toInt() shl 1

            repeat(var47.toInt()) {
                var var45 = var44 + (type.panOffset!![it].toInt() and 0xff)
                if (var45 < 0) {
                    var45 = 0
                }
                if (var45 > 128) {
                    var45 = 128
                }
                type.panOffset!![it] = var45.toByte()
            }

            for (index in 2 until var16.size step 2) {
                val var30 = var16[index]
                val var46 = var16[index + 1].toInt() shl 1
                var var32 = var44 * (var30 - var47) + (var30 - var47) / 2
                for (panIndex in var47 until var30) {
                    val var34 = method4142(var32, var30 - var47)
                    var var35 = var34 + (type.panOffset!![panIndex].toInt() and 0xff)
                    if (var35 < 0) {
                        var35 = 0
                    }
                    if (var35 > 128) {
                        var35 = 128
                    }
                    type.panOffset!![panIndex] = var35.toByte()
                    var32 += var46 - var44
                }
                var47 = var30
                var44 = var46
            }

            for (index in var47 until 128) {
                var var46 = var44 + (type.panOffset!![index].toInt() and 0xff)
                if (var46 < 0) {
                    var46 = 0
                }
                if (var46 > 128) {
                    var46 = 128
                }
                type.panOffset!![index] = var46.toByte()
            }
        }

        repeat(var12) {
            instruments[it]!!.field3052 = readUByte()
        }

        repeat(var12) {
            val var39 = instruments[it]!!
            if (var39.field3056 != null) {
                var39.field3055 = readUByte()
            }

            if (var39.field3054 != null) {
                var39.field3053 = readUByte()
            }

            if (var39.field3052 > 0) {
                var39.field3057 = readUByte()
            }
        }

        repeat(var12) {
            instruments[it]!!.field3059 = readUByte()
        }

        repeat(var12) {
            val var39 = instruments[it]!!
            if (var39.field3059 > 0) {
                var39.field3058 = readUByte()
            }
        }

        repeat(var12) {
            val var39 = instruments[it]!!
            if (var39.field3058 > 0) {
                var39.field3060 = readUByte()
            }
        }
        assertEmptyAndRelease()
        return type
    }

    private fun method4142(var0: Int, var1: Int): Int {
        val var2 = var0 ushr 31
        return (var0 + var2) / var1 - var2
    }
}
