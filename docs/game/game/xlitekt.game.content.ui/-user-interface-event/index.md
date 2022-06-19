//[game](../../../index.md)/[xlitekt.game.content.ui](../index.md)/[UserInterfaceEvent](index.md)

# UserInterfaceEvent

[jvm]\
sealed class [UserInterfaceEvent](index.md)

## Types

| Name | Summary |
|---|---|
| [ButtonClickEvent](-button-click-event/index.md) | [jvm]<br>data class [ButtonClickEvent](-button-click-event/index.md)(val index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val interfaceId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val childId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val slotId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val itemId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val action: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [UserInterfaceEvent](index.md) |
| [CloseEvent](-close-event/index.md) | [jvm]<br>data class [CloseEvent](-close-event/index.md)(val interfaceId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [UserInterfaceEvent](index.md) |
| [ContainerUpdateFullEvent](-container-update-full-event/index.md) | [jvm]<br>data class [ContainerUpdateFullEvent](-container-update-full-event/index.md)(val interfaceId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val items: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Item](../../xlitekt.game.content.item/-item/index.md)?&gt;) : [UserInterfaceEvent](index.md) |
| [CreateEvent](-create-event/index.md) | [jvm]<br>data class [CreateEvent](-create-event/index.md)(val interfaceId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [UserInterfaceEvent](index.md) |
| [IfEvent](-if-event/index.md) | [jvm]<br>data class [IfEvent](-if-event/index.md)(val slots: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html), val event: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [UserInterfaceEvent](index.md) |
| [OpenEvent](-open-event/index.md) | [jvm]<br>data class [OpenEvent](-open-event/index.md)(val interfaceId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [UserInterfaceEvent](index.md) |
| [OpHeldEvent](-op-held-event/index.md) | [jvm]<br>data class [OpHeldEvent](-op-held-event/index.md)(val index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val fromInterfaceId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val fromChildId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val fromSlotId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val fromItemId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val toInterfaceId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val toChildId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val toSlotId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val toItemId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [UserInterfaceEvent](index.md) |

## Inheritors

| Name |
|---|
| [CreateEvent](-create-event/index.md) |
| [OpenEvent](-open-event/index.md) |
| [CloseEvent](-close-event/index.md) |
| [ButtonClickEvent](-button-click-event/index.md) |
| [IfEvent](-if-event/index.md) |
| [ContainerUpdateFullEvent](-container-update-full-event/index.md) |
| [OpHeldEvent](-op-held-event/index.md) |
