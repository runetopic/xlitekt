/*
 * Copyright (C) 2008  RS2DBase Development team
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runetopic.xlitekt.game.actor

class ActorIterator<E : Actor?>(
    private val entities: Array<Actor?>,
    private val indicies: Set<Int>,
    private val entityList: ActorCollection<*>
) : MutableIterator<E> {
    private val typedIndicies: Array<Int> = indicies.toTypedArray()
    private var curIndex = 0

    override fun hasNext(): Boolean {
        return indicies.size != curIndex
    }

    override fun next(): E {
        val temp = entities[typedIndicies[curIndex]]
        curIndex++
        return temp as E
    }

    override fun remove() {
        if (curIndex >= 1) {
            entityList.remove(typedIndicies[curIndex - 1])
        }
    }
}
