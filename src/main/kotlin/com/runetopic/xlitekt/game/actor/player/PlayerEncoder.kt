package com.runetopic.xlitekt.game.actor.player

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

/**
 * @author Jordan Abraham
 */
object PlayerEncoder {
    private val json = Json { prettyPrint = true }

    fun Player.encodeToJson() {
        Path.of("./players/").apply {
            if (notExists()) createDirectories()
        }.also {
            json.encodeToStream(this, Path.of("$it/$username.json").outputStream())
        }
    }
}
