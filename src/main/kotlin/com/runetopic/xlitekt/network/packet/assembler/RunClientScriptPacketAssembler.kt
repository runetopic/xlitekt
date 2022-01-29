package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.game.container.Container
import com.runetopic.xlitekt.game.item.Item
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.util.ext.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.writeInt

class RunClientScriptPacketAssembler : PacketAssembler<RunClientScriptPacket>(opcode = 71, size = -2) {
    override fun assemblePacket(packet: RunClientScriptPacket) = buildPacket {
        packet.parameters.let {

            val params = buildString {
                (it.size - 1 downTo 0).forEach { count ->
                    append(mapParameterType(it, count))
                }
            }

            writeStringCp1252NullTerminated(params)

            var index = 0
            (it.size - 1 downTo 0).forEach { count ->
                when (params[count]) {
                    's' -> writeStringCp1252NullTerminated(it[index++] as String) // String
                    'o' -> writeInt((it[index++] as Item).id) // Item
                    'i' -> writeInt(it[index++] as Int) // Int
                    'v' -> writeInt((it[index++] as Container).id)
                    else -> throw IllegalStateException("Run client script type was not found during parameter type write. The found type was ${params[count]}")
                }
            }
        }

        writeInt(packet.id)
    }

    private fun mapParameterType(it: List<Any>, count: Int) = when (it[count]) {
        is String -> "s"
        is Item -> "o"
        is Container -> "v"
        is Int -> "i"
        else -> throw IllegalStateException("Run client script type was not found during parameter type mapping. The found type was ${it[count]}")
    }
}