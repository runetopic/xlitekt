package com.runetopic.xlitekt.cache

import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.huffman.Huffman
import io.ktor.application.ApplicationEnvironment
import org.koin.dsl.module
import java.nio.file.Path

/**
 * @author Jordan Abraham
 */
val cacheModule = module(createdAtStart = true) {
    single { Js5Store(path = Path.of(inject<ApplicationEnvironment>().value.config.property("game.cache.path").getString()), parallel = true) }
    single { Huffman(get<Js5Store>().index(indexId = 10).group(groupName = "huffman").file(0).data) }
}
