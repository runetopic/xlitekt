//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelMappedInstrument](index.md)

# ModelMappedInstrument

[jvm]\
open class [ModelMappedInstrument](index.md) : [ModelInstrument](../-model-instrument/index.md)

This class is used to map instrument to another patch.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [ModelMappedInstrument](-model-mapped-instrument.md) | [jvm]<br>open fun [ModelMappedInstrument](-model-mapped-instrument.md)(ins: [ModelInstrument](../-model-instrument/index.md), patch: [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html)) |

## Functions

| Name | Summary |
|---|---|
| [getChannelMixer](get-channel-mixer.md) | [jvm]<br>open fun [getChannelMixer](get-channel-mixer.md)(channel: [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html), format: [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html)): [ModelChannelMixer](../-model-channel-mixer/index.md) |
| [getChannels](../-model-instrument/get-channels.md) | [jvm]<br>open fun [getChannels](../-model-instrument/get-channels.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [getData](get-data.md) | [jvm]<br>open fun [getData](get-data.md)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [getDataClass](../-s-f2-sample/index.md#1847946936%2FFunctions%2F-82533025) | [jvm]<br>open fun [getDataClass](../-s-f2-sample/index.md#1847946936%2FFunctions%2F-82533025)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
| [getDirector](get-director.md) | [jvm]<br>open fun [getDirector](get-director.md)(performers: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ModelPerformer](../-model-performer/index.md)&gt;, channel: [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html), player: [ModelDirectedPlayer](../-model-directed-player/index.md)): [ModelDirector](../-model-director/index.md) |
| [getKeys](../-model-instrument/get-keys.md) | [jvm]<br>open fun [getKeys](../-model-instrument/get-keys.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt; |
| [getName](../-s-f2-sample/index.md#1635503817%2FFunctions%2F-82533025) | [jvm]<br>open fun [getName](../-s-f2-sample/index.md#1635503817%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getPatch](index.md#404430618%2FFunctions%2F-82533025) | [jvm]<br>open fun [getPatch](index.md#404430618%2FFunctions%2F-82533025)(): [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html) |
| [getPatchAlias](../-model-instrument/get-patch-alias.md) | [jvm]<br>open fun [getPatchAlias](../-model-instrument/get-patch-alias.md)(): [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html) |
| [getPerformers](get-performers.md) | [jvm]<br>open fun [getPerformers](get-performers.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ModelPerformer](../-model-performer/index.md)&gt; |
| [getSoundbank](../-s-f2-sample/index.md#-929831557%2FFunctions%2F-82533025) | [jvm]<br>open fun [getSoundbank](../-s-f2-sample/index.md#-929831557%2FFunctions%2F-82533025)(): [Soundbank](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Soundbank.html) |
