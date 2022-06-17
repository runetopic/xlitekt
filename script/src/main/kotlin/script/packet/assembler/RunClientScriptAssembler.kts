package script.packet.assembler

import xlitekt.game.content.container.Container
import xlitekt.game.content.item.Item
import xlitekt.game.packet.RunClientScriptPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<RunClientScriptPacket>(opcode = 71, size = -2) {
    parameters.let { array ->
        val params = buildString {
            (array.size - 1 downTo 0).forEach { count ->
                append(mapParameterType(array, count))
            }
        }
        it.writeStringCp1252NullTerminated(params)
        var index = 0
        (array.size - 1 downTo 0).forEach { count ->
            when (params[count]) {
                's' -> it.writeStringCp1252NullTerminated(array[index++] as String) // String
                'o' -> it.writeInt((array[index++] as Item).id) // Item
                'i' -> it.writeInt(array[index++] as Int) // Int
                'v' -> it.writeInt((array[index++] as Container).id)
                else -> throw IllegalStateException("Run client script type was not found during parameter type write. The found type was ${params[count]}")
            }
        }
    }
    it.writeInt(id)
}

fun mapParameterType(it: Array<out Any>, count: Int) = when (it[count]) {
    is String -> "s"
    is Item -> "o"
    is Container -> "v"
    is Int -> "i"
    else -> throw IllegalStateException("Run client script type was not found during parameter type mapping. The found type was ${it[count]}")
}
