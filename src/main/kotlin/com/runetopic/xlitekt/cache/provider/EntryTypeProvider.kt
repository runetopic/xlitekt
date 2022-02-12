package com.runetopic.xlitekt.cache.provider

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.utils.io.core.ByteReadPacket

abstract class EntryTypeProvider<T : EntryType> {
    private val entries: List<T> get() = load()
    protected val store by inject<Js5Store>()

    abstract fun load(): List<T>
    abstract fun loadEntryType(buffer: ByteReadPacket, type: T): T

    fun size() = entries.size
    fun entryType(id: Int): T? = entries.find { it.id == id }

    companion object {
        const val CONFIG_INDEX = 2
        const val INTERFACE_INDEX = 3
        const val VARBIT_GROUP_ID = 14
    }
}
