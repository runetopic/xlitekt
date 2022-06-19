//[game](../../../index.md)/[xlitekt.game.packet](../index.md)/[UpdateContainerPartialPacket](index.md)

# UpdateContainerPartialPacket

[jvm]\
data class [UpdateContainerPartialPacket](index.md)(val packedInterface: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val containerKey: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val items: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Item](../../xlitekt.game.content.item/-item/index.md)?&gt;, val slots: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;) : [Packet](../-packet/index.md)

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [UpdateContainerPartialPacket](-update-container-partial-packet.md) | [jvm]<br>fun [UpdateContainerPartialPacket](-update-container-partial-packet.md)(packedInterface: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), containerKey: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), items: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Item](../../xlitekt.game.content.item/-item/index.md)?&gt;, slots: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [containerKey](container-key.md) | [jvm]<br>val [containerKey](container-key.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [items](items.md) | [jvm]<br>val [items](items.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Item](../../xlitekt.game.content.item/-item/index.md)?&gt; |
| [packedInterface](packed-interface.md) | [jvm]<br>val [packedInterface](packed-interface.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [slots](slots.md) | [jvm]<br>val [slots](slots.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt; |
