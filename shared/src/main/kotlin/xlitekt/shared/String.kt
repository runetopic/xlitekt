package xlitekt.shared

/**
 * @author Jordan Abraham
 */
fun String.formatChatMessage(): String {
    val symbols = listOf('.', '!', '?', ':')
    return replace("_", " ").lowercase().foldIndexed("") { index, current, next ->
        current + if (index == 0 || current[index - 1] in symbols) next.uppercase() else next
    }
}
