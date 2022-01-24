package com.runetopic.xlitekt.game.actor

import java.util.AbstractCollection

class ActorCollection<T : Actor>(private var capacity: Int) : AbstractCollection<T>() {
    var entities: Array<Actor?> = arrayOfNulls(capacity)
    var indicies: MutableSet<Int> = HashSet()
    var curIndex = MIN_VALUE

    private val lock = Any()

    override fun add(element: T): Boolean {
        synchronized(lock) {
            add(element, curIndex)
            return true
        }
    }

    override fun remove(element: T): Boolean {
        synchronized(lock) {
            entities[element.pid] = null
            indicies.remove(element.pid)
            decreaseIndex()
            return true
        }
    }

    fun remove(index: Int): T? {
        synchronized(lock) {
            val temp = entities[index]
            entities[index] = null
            indicies.remove(index)
            decreaseIndex()
            return temp as T?
        }
    }

    operator fun get(index: Int): T? {
        synchronized(lock) {
            return if (index >= entities.size) null else entities[index] as T?
        }
    }

    private fun add(entity: T, index: Int) {
        if (entities[curIndex] != null) {
            increaseIndex()
            add(entity, curIndex)
        } else {
            entities[curIndex] = entity
            entity.pid = index
            indicies.add(curIndex)
            increaseIndex()
        }
    }

    override fun iterator(): MutableIterator<T> {
        synchronized(lock) { return ActorIterator(entities, indicies, this) }
    }

    private fun increaseIndex() {
        curIndex++
        if (curIndex >= capacity) {
            curIndex = MIN_VALUE
        }
    }

    private fun decreaseIndex() {
        curIndex--
        if (curIndex <= capacity) curIndex = MIN_VALUE
    }

    override fun contains(element: T): Boolean {
        return indexOf(element) > -1
    }

    private fun indexOf(entity: T): Int {
        for (index in indicies) {
            if (entities[index] == entity) {
                return index
            }
        }
        return -1
    }

    override val size: Int get() = indicies.size

    companion object {
        private const val MIN_VALUE = 1
    }
}
