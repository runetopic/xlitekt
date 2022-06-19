//[game](../../../index.md)/[xlitekt.game.content.ui](../index.md)/[UserInterfaceListener](index.md)

# UserInterfaceListener

[jvm]\
class [UserInterfaceListener](index.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md), val userInterface: [UserInterface](../-user-interface/index.md))

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [UserInterfaceListener](-user-interface-listener.md) | [jvm]<br>fun [UserInterfaceListener](-user-interface-listener.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md), userInterface: [UserInterface](../-user-interface/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [click](click.md) | [jvm]<br>fun [click](click.md)(buttonClickEvent: [UserInterfaceEvent.ButtonClickEvent](../-user-interface-event/-button-click-event/index.md)) |
| [close](close.md) | [jvm]<br>fun [close](close.md)(closeEvent: [UserInterfaceEvent.CloseEvent](../-user-interface-event/-close-event/index.md)) |
| [init](init.md) | [jvm]<br>fun [init](init.md)(createEvent: [UserInterfaceEvent.CreateEvent](../-user-interface-event/-create-event/index.md)) |
| [onClick](on-click.md) | [jvm]<br>fun [onClick](on-click.md)(onButtonClickEvent: [OnButtonClickEvent](../index.md#-308394779%2FClasslikes%2F440369633))<br>fun [onClick](on-click.md)(childId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onButtonClickEvent: [OnButtonClickEvent](../index.md#-308394779%2FClasslikes%2F440369633))<br>fun [onClick](on-click.md)(action: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), onButtonClickEvent: [OnButtonClickEvent](../index.md#-308394779%2FClasslikes%2F440369633)) |
| [onClose](on-close.md) | [jvm]<br>fun [onClose](on-close.md)(onCloseEvent: [OnCloseEvent](../index.md#1821806567%2FClasslikes%2F440369633)) |
| [onCreate](on-create.md) | [jvm]<br>fun [onCreate](on-create.md)(onCreateEvent: [OnCreateEvent](../index.md#-1244066565%2FClasslikes%2F440369633)) |
| [onOpen](on-open.md) | [jvm]<br>fun [onOpen](on-open.md)(onOpenEvent: [OnOpenEvent](../index.md#1692743625%2FClasslikes%2F440369633)) |
| [onOpHeld](on-op-held.md) | [jvm]<br>fun [onOpHeld](on-op-held.md)(event: [OnOpHeldEvent](../index.md#259597109%2FClasslikes%2F440369633)) |
| [open](open.md) | [jvm]<br>fun [open](open.md)(openEvent: [UserInterfaceEvent.OpenEvent](../-user-interface-event/-open-event/index.md)) |
| [opHeld](op-held.md) | [jvm]<br>fun [opHeld](op-held.md)(opHeldEvent: [UserInterfaceEvent.OpHeldEvent](../-user-interface-event/-op-held-event/index.md)) |
| [setEvent](set-event.md) | [jvm]<br>fun [setEvent](set-event.md)(childId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), slots: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html), vararg event: [InterfaceEvent](../-interface-event/index.md)) |
| [setItems](set-items.md) | [jvm]<br>fun [setItems](set-items.md)(containerKey: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), item: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Item](../../xlitekt.game.content.item/-item/index.md)?&gt;) |
| [setText](set-text.md) | [jvm]<br>fun [setText](set-text.md)(childId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [userInterface](user-interface.md) | [jvm]<br>val [userInterface](user-interface.md): [UserInterface](../-user-interface/index.md) |
