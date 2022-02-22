package com.runetopic.xlitekt.game.actor.player

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.inputStream

/**
 * @author Jordan Abraham
 */
object PlayerDecoder {
    fun decodeFromJson(username: String, password: String): Player {
        val path = Path.of("./players/$username.json")
        return if (path.exists()) Json.decodeFromStream(path.inputStream()) else Player(username, password)
    }
}
