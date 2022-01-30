package com.runetopic.xlitekt.game.actor.render

import com.runetopic.xlitekt.game.actor.Actor

enum class HitBarType(val id: Int) {
    DEFAULT(0), MEDIUM(10), LARGE(20);

    fun percentage(e: Actor): Int {
        val total = e.totalHitpoints()
        val current = e.currentHitpoints().coerceAtMost(total)
        var percentage = if (total == 0) 0 else current * 30 /*HitBarDefProvider.lookup(id).getHealthScale()*/ / total
        if (percentage == 0 && current > 0) {
            percentage = 1
        }
        return percentage
    }
}
