package xlitekt.shared

/**
 * @author Jordan Abraham
 */
fun String.formatChatMessage() = buildString {
    val symbols = listOf('.', '!', '?', ':')
    val raw = replace("_", " ").lowercase()
    raw.forEachIndexed { index, c ->
        append(if (index == 0 || raw[index - 1] in symbols) c.uppercase() else c)
    }
}
