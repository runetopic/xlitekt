//[game](../../../index.md)/[xlitekt.game.actor](../index.md)/[Actor](index.md)

# Actor

[jvm]\
abstract class [Actor](index.md)(var location: [Location](../../xlitekt.game.world.map/-location/index.md))

#### Author

Tyler Telis

Jordan Abraham

## Constructors

| | |
|---|---|
| [Actor](-actor.md) | [jvm]<br>fun [Actor](-actor.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [alternativeHighDefinitionRenderingBlocks](alternative-high-definition-rendering-blocks.md) | [jvm]<br>fun [alternativeHighDefinitionRenderingBlocks](alternative-high-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[AlternativeDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-alternative-definition-rendering-block/index.md)&gt;<br>Returns a map of this players alternative rendering blocks. |
| [alternativeLowDefinitionRenderingBlocks](alternative-low-definition-rendering-blocks.md) | [jvm]<br>fun [alternativeLowDefinitionRenderingBlocks](alternative-low-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[AlternativeDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-alternative-definition-rendering-block/index.md)&gt; |
| [currentHitpoints](current-hitpoints.md) | [jvm]<br>abstract fun [currentHitpoints](current-hitpoints.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [highDefinitionRenderingBlocks](high-definition-rendering-blocks.md) | [jvm]<br>fun [highDefinitionRenderingBlocks](high-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[HighDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-high-definition-rendering-block/index.md)&gt;<br>Returns a list of this actors high definition rendering blocks. |
| [lowDefinitionRenderingBlocks](low-definition-rendering-blocks.md) | [jvm]<br>fun [lowDefinitionRenderingBlocks](low-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[LowDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-low-definition-rendering-block/index.md)&gt;<br>Returns a list of this actors low definition rendering blocks. |
| [processMovement](process-movement.md) | [jvm]<br>fun [processMovement](process-movement.md)(players: NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt;): [MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?<br>Processes any pending movement this actor may have. This happens every tick. |
| [render](render.md) | [jvm]<br>fun [render](render.md)(render: [Render](../../xlitekt.game.actor.render/-render/index.md))<br>Flags this actor with a new pending rendering block. |
| [resetDefinitionRenderingBlocks](reset-definition-rendering-blocks.md) | [jvm]<br>fun [resetDefinitionRenderingBlocks](reset-definition-rendering-blocks.md)()<br>Happens after this actor has finished processing by the game loop. |
| [setZone](set-zone.md) | [jvm]<br>fun [setZone](set-zone.md)(zone: [Zone](../../xlitekt.game.world.map.zone/-zone/index.md))<br>Set this actor current zone. |
| [setZones](set-zones.md) | [jvm]<br>fun [setZones](set-zones.md)(removed: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;, added: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;)<br>Set this actor zones with a list of zones being removed and a list of zones being added. |
| [totalHitpoints](total-hitpoints.md) | [jvm]<br>abstract fun [totalHitpoints](total-hitpoints.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Properties

| Name | Summary |
|---|---|
| [bonuses](bonuses.md) | [jvm]<br>val [bonuses](bonuses.md): [Bonuses](../../xlitekt.game.actor.bonus/-bonuses/index.md) |
| [index](--index--.md) | [jvm]<br>var [index](--index--.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>This actor index. |
| [indexL](index-l.md) | [jvm]<br>val [indexL](index-l.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [location](location.md) | [jvm]<br>open var [location](location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [movement](movement.md) | [jvm]<br>val [movement](movement.md): [Movement](../../xlitekt.game.actor.movement/-movement/index.md) |
| [previousLocation](previous-location.md) | [jvm]<br>var [previousLocation](previous-location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [queue](queue.md) | [jvm]<br>val [queue](queue.md): [ActorScriptQueue](../../xlitekt.game.queue/-actor-script-queue/index.md) |
| [zone](zone.md) | [jvm]<br>val [zone](zone.md): [Zone](../../xlitekt.game.world.map.zone/-zone/index.md)<br>Represents this actor current location zone. |
| [zones](zones.md) | [jvm]<br>val [zones](zones.md): [HashSet](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;<br>Represents this actor 7x7 build area of zones. |

## Inheritors

| Name |
|---|
| [NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md) |
| [Player](../../xlitekt.game.actor.player/-player/index.md) |

## Extensions

| Name | Summary |
|---|---|
| [angleTo](../angle-to.md) | [jvm]<br>fun [Actor](index.md).[angleTo](../angle-to.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md))<br>Angle this actor to a location. If this actor is a Player this will flag the faceAngle render. If this actor is a NPC this will flag the faceLocation render.<br>[jvm]<br>fun [Actor](index.md).[angleTo](../angle-to.md)(gameObject: [GameObject](../../xlitekt.game.world.map/-game-object/index.md))<br>Angle this actor to a game object. If this actor is a Player this will flag the faceAngle render. If this actor is a NPC this will flag the faceLocation render.<br>[jvm]<br>fun [Actor](index.md).[angleTo](../angle-to.md)(npc: [NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md))<br>Angle this actor to a npc. If this actor is a Player this will flag the faceAngle render. If this actor is a NPC this will flag the faceLocation render. |
| [animate](../animate.md) | [jvm]<br>inline fun [Actor](index.md).[animate](../animate.md)(sequenceId: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>Render this actor to animate a sequence. |
| [cancelAll](../cancel-all.md) | [jvm]<br>fun [Actor](index.md).[cancelAll](../cancel-all.md)()<br>Cancels all the actions an actor is doing. |
| [chat](../chat.md) | [jvm]<br>inline fun [Actor](index.md).[chat](../chat.md)(rights: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), effects: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), message: () -&gt; [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))<br>Render this actor to public chat. This is only supported by player actors. |
| [faceActor](../face-actor.md) | [jvm]<br>inline fun [Actor](index.md).[faceActor](../face-actor.md)(index: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>Render this actor to face an actor. |
| [hit](../hit.md) | [jvm]<br>inline fun [Actor](index.md).[hit](../hit.md)(hitBar: [HitBar](../../xlitekt.game.actor.render/-hit-bar/index.md), source: [Actor](index.md)?, type: [HitType](../../xlitekt.game.actor.render/-hit-type/index.md), delay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), damage: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>Render this actor with hits. |
| [movementType](../movement-type.md) | [jvm]<br>inline fun [Actor](index.md).[movementType](../movement-type.md)(running: () -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))<br>Render this actor to update the movement type. This is only supported by player actors. |
| [overheadChat](../overhead-chat.md) | [jvm]<br>inline fun [Actor](index.md).[overheadChat](../overhead-chat.md)(message: () -&gt; [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))<br>Render this actor to overhead chat. |
| [queueNormal](../queue-normal.md) | [jvm]<br>fun [Actor](index.md).[queueNormal](../queue-normal.md)(task: [SuspendableQueuedScript](../../xlitekt.game.queue/index.md#1908705368%2FClasslikes%2F440369633)&lt;[Actor](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [queueSoft](../queue-soft.md) | [jvm]<br>fun [Actor](index.md).[queueSoft](../queue-soft.md)(task: [SuspendableQueuedScript](../../xlitekt.game.queue/index.md#1908705368%2FClasslikes%2F440369633)&lt;[Actor](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [queueStrong](../queue-strong.md) | [jvm]<br>fun [Actor](index.md).[queueStrong](../queue-strong.md)(task: [SuspendableQueuedScript](../../xlitekt.game.queue/index.md#1908705368%2FClasslikes%2F440369633)&lt;[Actor](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [queueWeak](../queue-weak.md) | [jvm]<br>fun [Actor](index.md).[queueWeak](../queue-weak.md)(task: [SuspendableQueuedScript](../../xlitekt.game.queue/index.md#1908705368%2FClasslikes%2F440369633)&lt;[Actor](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [resetMovement](../reset-movement.md) | [jvm]<br>fun [Actor](index.md).[resetMovement](../reset-movement.md)(resetFlag: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false)<br>Resets movement related information. This also cancels the facingActorIndex flag. |
| [routeTo](../route-to.md) | [jvm]<br>fun [Actor](index.md).[routeTo](../route-to.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md), reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null)<br>Route this actor to a location.<br>[jvm]<br>fun [Actor](index.md).[routeTo](../route-to.md)(gameObject: [GameObject](../../xlitekt.game.world.map/-game-object/index.md), reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null)<br>Route this actor to a game object.<br>[jvm]<br>fun [Actor](index.md).[routeTo](../route-to.md)(npc: [NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md), reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null)<br>Route this actor to a npc. |
| [speed](../speed.md) | [jvm]<br>inline fun [Actor](index.md).[speed](../speed.md)(running: () -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Toggle the actor movement speed between walking and running. If the actor is a Player then this will also flag for movement and temporary movement type updates. |
| [spotAnimate](../spot-animate.md) | [jvm]<br>inline fun [Actor](index.md).[spotAnimate](../spot-animate.md)(spotAnimationId: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>Render this actor to spot animate. |
| [teleportTo](../teleport-to.md) | [jvm]<br>inline fun [Actor](index.md).[teleportTo](../teleport-to.md)(location: () -&gt; [Location](../../xlitekt.game.world.map/-location/index.md))<br>Route the actor movement to a single location with teleport speed. |
| [temporaryMovementType](../temporary-movement-type.md) | [jvm]<br>inline fun [Actor](index.md).[temporaryMovementType](../temporary-movement-type.md)(id: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>Render this actor to temporary movement type. This is only supported by player actors. |
