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
        type.audioBuffers = Array(128) { null }
        type.pitchOffset = ShortArray(128)
        type.volumeOffset = ByteArray(128)
        type.panOffset = ByteArray(128)
        type.field3117 = arrayOfNulls(128)
        type.loopMode = ByteArray(128)
        type.offsets = IntArray(128)
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
            for (index in 2 until sizeToThirdZero) {
                var var41 = buffer.get().toInt() and 0xff
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
            sizeToThirdZero
        }

        // ==================================================================

        val instruments = Array<Instrument?>(var12) { null }
        var var15: Instrument?
        repeat(instruments.size) {
            val instrument = Instrument()
            instruments[it] = instrument
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
            type.pitchOffset!![it] = var19.toShort()
        }

        // ==================================================================

        var19 = 0
        repeat(128) {
            var19 += buffer.get().toInt() and 0xff
            type.pitchOffset!![it] = (type.pitchOffset!![it] + (var19 shl 8)).toShort()
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
            type.pitchOffset!![it] = (type.pitchOffset!![it] + ((var22 - 1 and 2) shl 14)).toShort()
            type.offsets!![it] = var22
            --var20
        }

        // ==================================================================

        var20 = 0
        var21 = 0
        var var23 = 0
        repeat(128) {
            if (type.offsets!![it] != 0) {
                if (var20 == 0) {
                    var20 = if (var21 < firstArrayBlock.size) firstArrayBlock[var21++].toInt() else -1
                    var23 = buffer.array()[firstArrayBlockPosition++] - 1
                }
                type.loopMode!![it] = var23.toByte()
                --var20
            }
        }

        // ==================================================================

        var20 = 0
        var21 = 0
        var var24 = 0
        repeat(128) {
            if (type.offsets!![it] != 0) {
                if (var20 == 0) {
                    var20 = if (var21 < secondArrayBlock.size) secondArrayBlock[var21++].toInt() else -1
                    var24 = buffer.array()[secondArrayBlockPosition++] + 16 shl 2
                }
                type.panOffset!![it] = var24.toByte()
                --var20
            }
        }

        // ==================================================================

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

        // ==================================================================

        var20 = 0
        var21 = 0
        var var26 = 0
        repeat(128) {
            if (var20 == 0) {
                var20 = if (var21 < var18.size) var18[var21++].toInt() else -1
                if (type.offsets!![it] > 0) {
                    var26 = (buffer.get().toInt() and 0xff) + 1
                }
            }
            type.volumeOffset!![it] = var26.toByte()
            --var20
        }

        // ==================================================================

        type.baseVelocity = (buffer.get().toInt() and 0xff) + 1

        // ==================================================================

        repeat(var12) {
            val instrument = instruments[it]!!
            if (instrument.field3056 != null) {
                for (index in 1 until instrument.field3056!!.size step 2) {
                    instrument.field3056!![index] = buffer.get()
                }
            }
            if (instrument.field3054 != null) {
                for (index in 3 until instrument.field3054!!.size - 2 step 2) {
                    instrument.field3054!![index] = buffer.get()
                }
            }
        }

        // ==================================================================

        if (var42 != null) {
            for (index in 1 until var42.size step 2) {
                var42[index] = buffer.get()
            }
        }

        if (var16 != null) {
            for (index in 1 until var16.size step 2) {
                var16[index] = buffer.get()
            }
        }

        // ==================================================================

        repeat(var12) {
            val instrument = instruments[it]!!
            if (instrument.field3054 != null) {
                var var191 = 0
                for (index in 2 until instrument.field3054!!.size step 2) {
                    var191 += 1 + (buffer.get().toInt() and 0xff)
                    instrument.field3054!![index] = var191.toByte()
                }
            }
        }

        // ==================================================================

        repeat(var12) {
            val instrument = instruments[it]!!
            if (instrument.field3056 != null) {
                var var191 = 0
                for (index in 2 until instrument.field3056!!.size step 2) {
                    var191 += 1 + (buffer.get().toInt() and 0xff)
                    instrument.field3056!![index] = var191.toByte()
                }
            }
        }

        // ==================================================================

        if (var42 != null) {
            var var191 = buffer.get().toInt() and 0xff
            var42[0] = var191.toByte()

            for (index in 2 until var42.size step 2) {
                var191 += 1 + (buffer.get().toInt() and 0xff)
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
                for (volumeIndex in var47.toInt() until var30) {
                    val var34 = method4142(var32, var30 - var47)
                    type.volumeOffset!![volumeIndex] = (var34 * type.volumeOffset!![volumeIndex] + 32 shr 6).toByte()
                    var32 += var31 - var28
                }
                var47 = var30
                var28 = var31
            }

            for (index in var47.toInt() until 128) {
                type.volumeOffset!![index] = (var28 * type.volumeOffset!![index] + 32 shr 6).toByte()
            }
            var15 = null
        }

        // ==================================================================

        if (var16 != null) {
            var var191 = buffer.get().toInt() and 0xff
            var16[0] = var191.toByte()

            for (index in 2 until var16.size step 2) {
                var191 += 1 + (buffer.get().toInt() and 0xff)
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
                for (panIndex in var47.toInt() until var30) {
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

            for (index in var47.toInt() until 128) {
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

        // ==================================================================

        repeat(var12) {
            instruments[it]!!.field3052 = buffer.get().toInt() and 0xff
        }

        // ==================================================================

        repeat(var12) {
            val var39 = instruments[it]!!
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
            instruments[it]!!.field3059 = buffer.get().toInt() and 0xff
        }

        repeat(var12) {
            val var39 = instruments[it]!!
            if (var39.field3059 > 0) {
                var39.field3058 = buffer.get().toInt() and 0xff
            }
        }

        repeat(var12) {
            val var39 = instruments[it]!!
            if (var39.field3058 > 0) {
                var39.field3060 = buffer.get().toInt() and 0xff
            }
        }
        assertEmptyAndRelease()
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
