package com.runetopic.xlitekt.cache.provider

import com.runetopic.cache.store.Js5Store

interface EntryTypeProvider<T : EntryType> {
    fun load(store: Js5Store)
}
