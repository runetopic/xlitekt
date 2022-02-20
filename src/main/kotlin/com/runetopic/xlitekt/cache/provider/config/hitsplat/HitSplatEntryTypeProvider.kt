package com.runetopic.xlitekt.cache.provider.config.hitsplat

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.shared.buffer.readStringCp1252NullCircumfixed
import com.runetopic.xlitekt.shared.buffer.readUIntSmart
import com.runetopic.xlitekt.shared.buffer.readUMedium
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class HitSplatEntryTypeProvider : EntryTypeProvider<HitSplatEntryType>() {

    override fun load(): Map<Int, HitSplatEntryType> = store
        .index(CONFIG_INDEX)
        .group(HITSPLAT_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(HitSplatEntryType(it.id)) }
        .associateBy(HitSplatEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: HitSplatEntryType): HitSplatEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.fontId = readUIntSmart()
            2 -> type.textColor = readUMedium()
            3 -> type.spriteId1 = readUIntSmart()
            4 -> type.spriteId2 = readUIntSmart()
            5 -> type.spriteId3 = readUIntSmart()
            6 -> type.spriteId4 = readUIntSmart()
            7 -> type.field1929 = readShort().toInt()
            8 -> type.field1942 = readStringCp1252NullCircumfixed()
            9 -> type.field1934 = readUShort().toInt()
            10 -> type.field1940 = readShort().toInt()
            11 -> type.field1943 = 0
            12 -> type.field1946 = readUByte().toInt()
            13 -> type.field1944 = readShort().toInt()
            14 -> type.field1943 = readUShort().toInt()
            17, 18 -> {
                type.transformVarbit = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transformVarp = readUShort().toInt().let { if (it == 0xffff) -1 else it }
                val prime = if (opcode == 17) -1 else readUShort().toInt().let { if (it == 0xffff) -1 else it }
                type.transforms = buildList {
                    repeat(readUByte().toInt() + 1) {
                        add(readUShort().toInt().let { if (it == 0xffff) -1 else it })
                    }
                    add(prime)
                }
            }
        }
        return loadEntryType(type)
    }
}
