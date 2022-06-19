//[cache](../../../index.md)/[com.sun.media](../index.md)/[SF2Instrument](index.md)

# SF2Instrument

[jvm]\
open class [SF2Instrument](index.md) : [ModelInstrument](../-model-instrument/index.md)

Soundfont instrument.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [SF2Instrument](-s-f2-instrument.md) | [jvm]<br>open fun [SF2Instrument](-s-f2-instrument.md)() |
| [SF2Instrument](-s-f2-instrument.md) | [jvm]<br>open fun [SF2Instrument](-s-f2-instrument.md)(soundbank: [SF2Soundbank](../-s-f2-soundbank/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [getChannelMixer](../-model-instrument/get-channel-mixer.md) | [jvm]<br>open fun [getChannelMixer](../-model-instrument/get-channel-mixer.md)(channel: [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html), format: [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html)): [ModelChannelMixer](../-model-channel-mixer/index.md) |
| [getChannels](../-model-instrument/get-channels.md) | [jvm]<br>open fun [getChannels](../-model-instrument/get-channels.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [getData](get-data.md) | [jvm]<br>open fun [getData](get-data.md)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [getDataClass](../-s-f2-sample/index.md#1847946936%2FFunctions%2F-82533025) | [jvm]<br>open fun [getDataClass](../-s-f2-sample/index.md#1847946936%2FFunctions%2F-82533025)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
| [getDirector](../-model-instrument/get-director.md) | [jvm]<br>open fun [getDirector](../-model-instrument/get-director.md)(performers: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ModelPerformer](../-model-performer/index.md)&gt;, channel: [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html), player: [ModelDirectedPlayer](../-model-directed-player/index.md)): [ModelDirector](../-model-director/index.md) |
| [getGlobalRegion](get-global-region.md) | [jvm]<br>open fun [getGlobalRegion](get-global-region.md)(): [SF2GlobalRegion](../-s-f2-global-region/index.md) |
| [getKeys](../-model-instrument/get-keys.md) | [jvm]<br>open fun [getKeys](../-model-instrument/get-keys.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt; |
| [getName](../-s-f2-sample/index.md#1635503817%2FFunctions%2F-82533025) | [jvm]<br>open fun [getName](../-s-f2-sample/index.md#1635503817%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getPatch](get-patch.md) | [jvm]<br>open fun [getPatch](get-patch.md)(): [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html) |
| [getPatchAlias](../-model-instrument/get-patch-alias.md) | [jvm]<br>open fun [getPatchAlias](../-model-instrument/get-patch-alias.md)(): [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html) |
| [getPerformers](get-performers.md) | [jvm]<br>open fun [getPerformers](get-performers.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ModelPerformer](../-model-performer/index.md)&gt; |
| [getSoundbank](../-s-f2-sample/index.md#-929831557%2FFunctions%2F-82533025) | [jvm]<br>open fun [getSoundbank](../-s-f2-sample/index.md#-929831557%2FFunctions%2F-82533025)(): [Soundbank](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Soundbank.html) |
| [setGlobalZone](set-global-zone.md) | [jvm]<br>open fun [setGlobalZone](set-global-zone.md)(zone: [SF2GlobalRegion](../-s-f2-global-region/index.md)) |
| [setPatch](set-patch.md) | [jvm]<br>open fun [setPatch](set-patch.md)(patch: [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html)) |
| [toString](to-string.md) | [jvm]<br>open fun [toString](to-string.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |

## Properties

| Name | Summary |
|---|---|
| [genre](genre.md) | [jvm]<br>protected open var [genre](genre.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [library](library.md) | [jvm]<br>protected open var [library](library.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [morphology](morphology.md) | [jvm]<br>protected open var [morphology](morphology.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [name](name.md) | [jvm]<br>protected open var [name](name.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [regions](regions.md) | [jvm]<br>protected open val [regions](regions.md): [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[SF2InstrumentRegion](../-s-f2-instrument-region/index.md)&gt; |
