//[game](../../index.md)/[xlitekt.game.content.ui](index.md)

# Package xlitekt.game.content.ui

## Types

| Name | Summary |
|---|---|
| [InterfaceEvent](-interface-event/index.md) | [jvm]<br>sealed class [InterfaceEvent](-interface-event/index.md) |
| [InterfaceLayout](-interface-layout/index.md) | [jvm]<br>enum [InterfaceLayout](-interface-layout/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[InterfaceLayout](-interface-layout/index.md)&gt; |
| [InterfaceListener](-interface-listener/index.md) | [jvm]<br>class [InterfaceListener](-interface-listener/index.md) : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;, [UserInterfaceListener](-user-interface-listener/index.md).([Player](../xlitekt.game.actor.player/-player/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; |
| [Interfaces](-interfaces/index.md) | [jvm]<br>class [Interfaces](-interfaces/index.md)(player: [Player](../xlitekt.game.actor.player/-player/index.md), interfaces: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[UserInterface](-user-interface/index.md)&gt; = mutableListOf()) : [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[UserInterface](-user-interface/index.md)&gt; |
| [OnButtonClickEvent](index.md#-308394779%2FClasslikes%2F440369633) | [jvm]<br>typealias [OnButtonClickEvent](index.md#-308394779%2FClasslikes%2F440369633) = [Player](../xlitekt.game.actor.player/-player/index.md).([UserInterfaceEvent.ButtonClickEvent](-user-interface-event/-button-click-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnCloseEvent](index.md#1821806567%2FClasslikes%2F440369633) | [jvm]<br>typealias [OnCloseEvent](index.md#1821806567%2FClasslikes%2F440369633) = [Player](../xlitekt.game.actor.player/-player/index.md).([UserInterfaceEvent.CloseEvent](-user-interface-event/-close-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnCreateEvent](index.md#-1244066565%2FClasslikes%2F440369633) | [jvm]<br>typealias [OnCreateEvent](index.md#-1244066565%2FClasslikes%2F440369633) = [Player](../xlitekt.game.actor.player/-player/index.md).([UserInterfaceEvent.CreateEvent](-user-interface-event/-create-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnOpenEvent](index.md#1692743625%2FClasslikes%2F440369633) | [jvm]<br>typealias [OnOpenEvent](index.md#1692743625%2FClasslikes%2F440369633) = [Player](../xlitekt.game.actor.player/-player/index.md).([UserInterfaceEvent.OpenEvent](-user-interface-event/-open-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnOpHeldEvent](index.md#259597109%2FClasslikes%2F440369633) | [jvm]<br>typealias [OnOpHeldEvent](index.md#259597109%2FClasslikes%2F440369633) = [Player](../xlitekt.game.actor.player/-player/index.md).([UserInterfaceEvent.OpHeldEvent](-user-interface-event/-op-held-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [UserInterface](-user-interface/index.md) | [jvm]<br>sealed class [UserInterface](-user-interface/index.md) |
| [UserInterfaceEvent](-user-interface-event/index.md) | [jvm]<br>sealed class [UserInterfaceEvent](-user-interface-event/index.md) |
| [UserInterfaceListener](-user-interface-listener/index.md) | [jvm]<br>class [UserInterfaceListener](-user-interface-listener/index.md)(player: [Player](../xlitekt.game.actor.player/-player/index.md), val userInterface: [UserInterface](-user-interface/index.md)) |
