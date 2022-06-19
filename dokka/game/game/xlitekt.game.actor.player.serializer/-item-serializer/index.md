//[game](../../../index.md)/[xlitekt.game.actor.player.serializer](../index.md)/[ItemSerializer](index.md)

# ItemSerializer

[jvm]\
class [ItemSerializer](index.md) : KSerializer&lt;[Item](../../xlitekt.game.content.item/-item/index.md)?&gt; 

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [ItemSerializer](-item-serializer.md) | [jvm]<br>fun [ItemSerializer](-item-serializer.md)() |

## Functions

| Name | Summary |
|---|---|
| [deserialize](deserialize.md) | [jvm]<br>open override fun [deserialize](deserialize.md)(decoder: Decoder): [Item](../../xlitekt.game.content.item/-item/index.md)? |
| [serialize](serialize.md) | [jvm]<br>open override fun [serialize](serialize.md)(encoder: Encoder, value: [Item](../../xlitekt.game.content.item/-item/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [descriptor](descriptor.md) | [jvm]<br>open override val [descriptor](descriptor.md): SerialDescriptor |
