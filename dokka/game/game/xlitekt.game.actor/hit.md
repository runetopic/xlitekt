//[game](../../index.md)/[xlitekt.game.actor](index.md)/[hit](hit.md)

# hit

[jvm]\
inline fun [Actor](-actor/index.md).[hit](hit.md)(hitBar: [HitBar](../xlitekt.game.actor.render/-hit-bar/index.md), source: [Actor](-actor/index.md)?, type: [HitType](../xlitekt.game.actor.render/-hit-type/index.md), delay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), damage: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))

Render this actor with hits.

## Parameters

jvm

| | |
|---|---|
| hitBar | The hitbar associated with this hit. |
| source | The source actor performing this hit. |
| type | The type of hit this is. |
| damage | The amount of damage to invoke. |
