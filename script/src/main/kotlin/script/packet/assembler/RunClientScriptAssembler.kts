package script.packet.assembler

import xlitekt.game.content.container.Container
import xlitekt.game.content.item.Item
import xlitekt.game.packet.RunClientScriptPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPacketAssembler<RunClientScriptPacket>(opcode = 71, size = -2) {
    allocateDynamic(256) {
        parameters.let {
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
        writeInt(id)
    }
}

fun mapParameterType(it: Array<out Any>, count: Int) = when (it[count]) {
    is String -> "s"
    is Item -> "o"
    is Container -> "v"
    is Int -> "i"
    else -> throw IllegalStateException("Run client script type was not found during parameter type mapping. The found type was ${it[count]}")
}
