//[game](../../../index.md)/[xlitekt.game.actor.player](../index.md)/[Player](index.md)

# Player

[jvm]\
class [Player](index.md)(var location: [Location](../../xlitekt.game.world.map/-location/index.md) = Location(3222, 3222), val username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val password: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val rights: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var weight: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) = 0.0f, val appearance: [Render.Appearance](../../xlitekt.game.actor.render/-render/-appearance/index.md) = Render.Appearance().also { it.displayName = username }, var runEnergy: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), var brandNew: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, val skills: [Skills](../../xlitekt.game.content.skill/-skills/index.md) = Skills()) : [Actor](../../xlitekt.game.actor/-actor/index.md)

#### Author

Jordan Abraham

Tyler Telis

## Constructors

| | |
|---|---|
| [Player](-player.md) | [jvm]<br>fun [Player](-player.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md) = Location(3222, 3222), username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), password: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), rights: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, weight: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) = 0.0f, appearance: [Render.Appearance](../../xlitekt.game.actor.render/-render/-appearance/index.md) = Render.Appearance().also { it.displayName = username }, runEnergy: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), brandNew: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, skills: [Skills](../../xlitekt.game.content.skill/-skills/index.md) = Skills()) |

## Functions

| Name | Summary |
|---|---|
| [alternativeHighDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-high-definition-rendering-blocks.md) | [jvm]<br>fun [alternativeHighDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-high-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[AlternativeDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-alternative-definition-rendering-block/index.md)&gt;<br>Returns a map of this players alternative rendering blocks. |
| [alternativeLowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-low-definition-rendering-blocks.md) | [jvm]<br>fun [alternativeLowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/alternative-low-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[AlternativeDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-alternative-definition-rendering-block/index.md)&gt; |
| [currentHitpoints](current-hitpoints.md) | [jvm]<br>open override fun [currentHitpoints](current-hitpoints.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [highDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/high-definition-rendering-blocks.md) | [jvm]<br>fun [highDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/high-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[HighDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-high-definition-rendering-block/index.md)&gt;<br>Returns a list of this actors high definition rendering blocks. |
| [init](init.md) | [jvm]<br>fun [init](init.md)(client: [Client](../-client/index.md), players: NonBlockingHashMapLong&lt;[Player](index.md)&gt;)<br>Initiates this player when logging into the game world. This happens before anything else. |
| [invokeAndClearReadPool](invoke-and-clear-read-pool.md) | [jvm]<br>fun [invokeAndClearReadPool](invoke-and-clear-read-pool.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>Invokes and handles the pooled packets sent from the connected client. This is used to keep the player synchronized with the game loop no matter their actions from the client. The pool is then cleared after operation. This happens every tick. |
| [invokeAndClearWritePool](invoke-and-clear-write-pool.md) | [jvm]<br>fun [invokeAndClearWritePool](invoke-and-clear-write-pool.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>Invokes and writes the pooled packets to the connected client. The pool is then cleared after operation. This happens every tick. |
| [logout](logout.md) | [jvm]<br>fun [logout](logout.md)()<br>Logout the player from the game world. |
| [lowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/low-definition-rendering-blocks.md) | [jvm]<br>fun [lowDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/low-definition-rendering-blocks.md)(): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[LowDefinitionRenderingBlock](../../xlitekt.game.actor.render.block/-low-definition-rendering-block/index.md)&gt;<br>Returns a list of this actors low definition rendering blocks. |
| [processMovement](../../xlitekt.game.actor/-actor/process-movement.md) | [jvm]<br>fun [processMovement](../../xlitekt.game.actor/-actor/process-movement.md)(players: NonBlockingHashMapLong&lt;[Player](index.md)&gt;): [MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?<br>Processes any pending movement this actor may have. This happens every tick. |
| [read](read.md) | [jvm]<br>fun [read](read.md)(packetHandler: [PacketHandler](../../xlitekt.game.packet.disassembler.handler/-packet-handler/index.md)&lt;[Packet](../../xlitekt.game.packet/-packet/index.md)&gt;): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>Pools a disassembled packet from the connected client. |
| [render](../../xlitekt.game.actor/-actor/render.md) | [jvm]<br>fun [render](../../xlitekt.game.actor/-actor/render.md)(render: [Render](../../xlitekt.game.actor.render/-render/index.md))<br>Flags this actor with a new pending rendering block. |
| [resetDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/reset-definition-rendering-blocks.md) | [jvm]<br>fun [resetDefinitionRenderingBlocks](../../xlitekt.game.actor/-actor/reset-definition-rendering-blocks.md)()<br>Happens after this actor has finished processing by the game loop. |
| [setZone](../../xlitekt.game.actor/-actor/set-zone.md) | [jvm]<br>fun [setZone](../../xlitekt.game.actor/-actor/set-zone.md)(zone: [Zone](../../xlitekt.game.world.map.zone/-zone/index.md))<br>Set this actor current zone. |
| [setZones](../../xlitekt.game.actor/-actor/set-zones.md) | [jvm]<br>fun [setZones](../../xlitekt.game.actor/-actor/set-zones.md)(removed: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;, added: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;)<br>Set this actor zones with a list of zones being removed and a list of zones being added. |
| [totalHitpoints](total-hitpoints.md) | [jvm]<br>open override fun [totalHitpoints](total-hitpoints.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [write](write.md) | [jvm]<br>fun [write](write.md)(packet: [Packet](../../xlitekt.game.packet/-packet/index.md)): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>Pools a packet to be sent to the client. |

## Properties

| Name | Summary |
|---|---|
| [appearance](appearance.md) | [jvm]<br>val [appearance](appearance.md): [Render.Appearance](../../xlitekt.game.actor.render/-render/-appearance/index.md) |
| [bonuses](../../xlitekt.game.actor/-actor/bonuses.md) | [jvm]<br>val [bonuses](../../xlitekt.game.actor/-actor/bonuses.md): [Bonuses](../../xlitekt.game.actor.bonus/-bonuses/index.md) |
| [brandNew](brand-new.md) | [jvm]<br>var [brandNew](brand-new.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true |
| [equipment](equipment.md) | [jvm]<br>val [equipment](equipment.md): [Equipment](../../xlitekt.game.content.container.equipment/-equipment/index.md) |
| [index](../../xlitekt.game.actor/-actor/--index--.md) | [jvm]<br>var [index](../../xlitekt.game.actor/-actor/--index--.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>This actor index. |
| [indexL](../../xlitekt.game.actor/-actor/index-l.md) | [jvm]<br>val [indexL](../../xlitekt.game.actor/-actor/index-l.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [interfaces](interfaces.md) | [jvm]<br>val [interfaces](interfaces.md): [Interfaces](../../xlitekt.game.content.ui/-interfaces/index.md) |
| [inventory](inventory.md) | [jvm]<br>val [inventory](inventory.md): [Inventory](../../xlitekt.game.content.container.inventory/-inventory/index.md) |
| [lastLoadedLocation](last-loaded-location.md) | [jvm]<br>var [lastLoadedLocation](last-loaded-location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [location](location.md) | [jvm]<br>open override var [location](location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [movement](../../xlitekt.game.actor/-actor/movement.md) | [jvm]<br>val [movement](../../xlitekt.game.actor/-actor/movement.md): [Movement](../../xlitekt.game.actor.movement/-movement/index.md) |
| [online](online.md) | [jvm]<br>val [online](online.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>This players online var. If true this player is processed by the game loop. |
| [password](password.md) | [jvm]<br>val [password](password.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [previousLocation](../../xlitekt.game.actor/-actor/previous-location.md) | [jvm]<br>var [previousLocation](../../xlitekt.game.actor/-actor/previous-location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [queue](../../xlitekt.game.actor/-actor/queue.md) | [jvm]<br>val [queue](../../xlitekt.game.actor/-actor/queue.md): [ActorScriptQueue](../../xlitekt.game.queue/-actor-script-queue/index.md) |
| [rights](rights.md) | [jvm]<br>val [rights](rights.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [runEnergy](run-energy.md) | [jvm]<br>var [runEnergy](run-energy.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [skills](skills.md) | [jvm]<br>val [skills](skills.md): [Skills](../../xlitekt.game.content.skill/-skills/index.md) |
| [username](username.md) | [jvm]<br>val [username](username.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [vars](vars.md) | [jvm]<br>val [vars](vars.md): [Vars](../../xlitekt.game.content.vars/-vars/index.md) |
| [viewport](viewport.md) | [jvm]<br>val [viewport](viewport.md): [Viewport](../-viewport/index.md) |
| [weight](weight.md) | [jvm]<br>var [weight](weight.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) = 0.0f |
| [zone](../../xlitekt.game.actor/-actor/zone.md) | [jvm]<br>val [zone](../../xlitekt.game.actor/-actor/zone.md): [Zone](../../xlitekt.game.world.map.zone/-zone/index.md)<br>Represents this actor current location zone. |
| [zones](../../xlitekt.game.actor/-actor/zones.md) | [jvm]<br>val [zones](../../xlitekt.game.actor/-actor/zones.md): [HashSet](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)&lt;[Zone](../../xlitekt.game.world.map.zone/-zone/index.md)&gt;<br>Represents this actor 7x7 build area of zones. |

## Extensions

| Name | Summary |
|---|---|
| [addExperience](../add-experience.md) | [jvm]<br>fun [Player](index.md).[addExperience](../add-experience.md)(skill: [Skill](../../xlitekt.game.content.skill/-skill/index.md), experience: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [drainRunEnergy](../drain-run-energy.md) | [jvm]<br>fun [Player](index.md).[drainRunEnergy](../drain-run-energy.md)() |
| [message](../message.md) | [jvm]<br>inline fun [Player](index.md).[message](../message.md)(message: () -&gt; [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [process](../process.md) | [jvm]<br>fun [Player](index.md).[process](../process.md)() |
| [rebuildNormal](../rebuild-normal.md) | [jvm]<br>inline fun [Player](index.md).[rebuildNormal](../rebuild-normal.md)(players: NonBlockingHashMapLong&lt;[Player](index.md)&gt;, update: () -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [renderAppearance](../render-appearance.md) | [jvm]<br>fun [Player](index.md).[renderAppearance](../render-appearance.md)() |
| [resetMiniMapFlag](../reset-mini-map-flag.md) | [jvm]<br>fun [Player](index.md).[resetMiniMapFlag](../reset-mini-map-flag.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [restoreRunEnergy](../restore-run-energy.md) | [jvm]<br>fun [Player](index.md).[restoreRunEnergy](../restore-run-energy.md)() |
| [script](../script.md) | [jvm]<br>fun [Player](index.md).[script](../script.md)(scriptId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg parameters: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [updateRunEnergy](../update-run-energy.md) | [jvm]<br>fun [Player](index.md).[updateRunEnergy](../update-run-energy.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [updateStat](../update-stat.md) | [jvm]<br>fun [Player](index.md).[updateStat](../update-stat.md)(skill: [Skill](../../xlitekt.game.content.skill/-skill/index.md), level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), experience: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [varp](../varp.md) | [jvm]<br>inline fun [Player](index.md).[varp](../varp.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
