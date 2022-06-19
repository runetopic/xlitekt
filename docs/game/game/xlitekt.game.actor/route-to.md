//[game](../../index.md)/[xlitekt.game.actor](index.md)/[routeTo](route-to.md)

# routeTo

[jvm]\
fun [Actor](-actor/index.md).[routeTo](route-to.md)(location: [Location](../xlitekt.game.world.map/-location/index.md), reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null)

Route this actor to a location.

## Parameters

jvm

| | |
|---|---|
| location | The location to route to. |
| reachAction | A callback function to invoke when the actor reaches the destination. |

[jvm]\
fun [Actor](-actor/index.md).[routeTo](route-to.md)(gameObject: [GameObject](../xlitekt.game.world.map/-game-object/index.md), reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null)

Route this actor to a game object.

## Parameters

jvm

| | |
|---|---|
| gameObject | The game object to route to. |
| reachAction | A callback function to invoke when the actor reaches the destination. |

[jvm]\
fun [Actor](-actor/index.md).[routeTo](route-to.md)(npc: [NPC](../xlitekt.game.actor.npc/-n-p-c/index.md), reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null)

Route this actor to a npc.

## Parameters

jvm

| | |
|---|---|
| npc | The npc to route to. |
| reachAction | A callback function to invoke when the actor reaches the destination. |
