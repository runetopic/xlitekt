//[game](../../../index.md)/[xlitekt.game.actor.npc](../index.md)/[NPC](index.md)

# NPC

[jvm]\
class [NPC](index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), var location: [Location](../../xlitekt.game.world.map/-location/index.md)) : [Actor](../../xlitekt.game.actor/-actor/index.md)

#### Author

Tyler Telis

Jordan Abraham

## Constructors

| | |
|---|---|
| [NPC](-n-p-c.md) | [jvm]<br>fun [NPC](-n-p-c.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), location: [Location](../../xlitekt.game.world.map/-location/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [alternativeHighDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-high-definition-rendering-blocks.md) | [jvm]<br>fun [alternativeHighDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-high-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[AlternativeDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-alternative-definition-rendering-block/index.md)&gt;<br>Returns a map of this players alternative rendering blocks. |
| [alternativeLowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-low-definition-rendering-blocks.md) | [jvm]<br>fun [alternativeLowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-low-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[AlternativeDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-alternative-definition-rendering-block/index.md)&gt; |
| [currentHitpoints](current-hitpoints.md) | [jvm]<br>open override fun [currentHitpoints](current-hitpoints.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [highDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/high-definition-rendering-blocks.md) | [jvm]<br>fun [highDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/high-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[HighDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-high-definition-rendering-block/index.md)&gt;<br>Returns a list of this actors high definition rendering blocks. |
| [init](init.md) | [jvm]<br>fun [init](init.md)() |
| [lowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/low-definition-rendering-blocks.md) | [jvm]<br>fun [lowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/low-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[LowDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-low-definition-rendering-block/index.md)&gt;<br>Returns a list of this actors low definition rendering blocks. |
| [processMovement](../../xlitekt.game.actor/-actor/process-movement.md) | [jvm]<br>fun [processMovement](../../xlitekt.game.actor/-actor/process-movement.md)(players: NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt;): [MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?<br>Processes any pending movement this actor may have. This happens every tick. |
| [render](../../xlitekt.game.actor/-actor/render.md) | [jvm]<br>fun [render](../../xlitekt.game.actor/-actor/render.md)(render: [Render](../../xlitekt.game.actor.render/-render/index.md))<br>Flags this actor with a new pending rendering block. |
| [resetDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/reset-definition-rendering-blocks.md) | [jvm]<br>fun [resetDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/reset-definition-rendering-blocks.md)()<br>Happens after this actor has finished processing by the game loop. |
| [setZone](../../xlitekt.game.actor/-actor/set-zone.md) | [jvm]<br>fun [setZone](../../xlitekt.game.actor/-actor/set-zone.md)(zone: [Zone](../../xlitekt.game.world.map.zone/-zone/index.md))<br>Set this actor current zone. |
| [setZones](../../xlitekt.game.actor/-actor/set-zones.md) | [jvm]<br>fun [setZones](../../xlitekt.game.actor/-actor/set-zones.md)(removed: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;, added: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;)<br>Set this actor zones with a list of zones being removed and a list of zones being added. |
| [toString](to-string.md) | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [totalHitpoints](total-hitpoints.md) | [jvm]<br>open override fun [totalHitpoints](total-hitpoints.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Properties

| Name | Summary |
|---|---|
| [bonuses](../../xlitekt.game.actor/-actor/bonuses.md) | [jvm]<br>val [bonuses](../../xlitekt.game.actor/-actor/bonuses.md): [Bonuses](../../xlitekt.game.actor.bonus/-bonuses/index.md) |
| [entry](entry.md) | [jvm]<br>val [entry](entry.md): [NPCEntryType](../../../../cache/cache/xlitekt.cache.provider.config.npc/-n-p-c-entry-type/index.md)? |
| [id](id.md) | [jvm]<br>val [id](id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [index](../../xlitekt.game.actor/-actor/--index--.md) | [jvm]<br>var [index](../../xlitekt.game.actor/-actor/--index--.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>This actor index. |
| [indexL](../../xlitekt.game.actor/-actor/index-l.md) | [jvm]<br>val [indexL](../../xlitekt.game.actor/-actor/index-l.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [location](location.md) | [jvm]<br>open override var [location](location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [movement](../../xlitekt.game.actor/-actor/movement.md) | [jvm]<br>val [movement](../../xlitekt.game.actor/-actor/movement.md): [Movement](../../xlitekt.game.actor.movement/-movement/index.md) |
| [previousLocation](../../xlitekt.game.actor/-actor/previous-location.md) | [jvm]<br>var [previousLocation](../../xlitekt.game.actor/-actor/previous-location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [queue](../../xlitekt.game.actor/-actor/queue.md) | [jvm]<br>val [queue](../../xlitekt.game.actor/-actor/queue.md): [ActorScriptQueue](../../xlitekt.game.queue/-actor-script-queue/index.md) |
| [zone](../../xlitekt.game.actor/-actor/zone.md) | [jvm]<br>val [zone](../../xlitekt.game.actor/-actor/zone.md): [Zone](../../xlitekt.game.world.map.zone/-zone/index.md)<br>Represents this actor current location zone. |
| [zones](../../xlitekt.game.actor/-actor/zones.md) | [jvm]<br>val [zones](../../xlitekt.game.actor/-actor/zones.md): [HashSet](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;<br>Represents this actor 7x7 build area of zones. |
