//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelInstrument](index.md)

# ModelInstrument

[jvm]\
abstract class [ModelInstrument](index.md) : [Instrument](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Instrument.html)

The model instrument class. 

The main methods to override are: getPerformer, getDirector, getChannelMixer. 

Performers are used to define what voices which will playback when using the instrument. ChannelMixer is used to add channel-wide processing on voices output or to define non-voice oriented instruments. Director is used to change how the synthesizer chooses what performers to play on midi events.

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [getChannelMixer](get-channel-mixer.md) | [jvm]<br>open fun [getChannelMixer](get-channel-mixer.md)(channel: [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html), format: [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html)): [ModelChannelMixer](../-model-channel-mixer/index.md) |
| [getChannels](get-channels.md) | [jvm]<br>open fun [getChannels](get-channels.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [getData](../-s-f2-sample/index.md#1311705738%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getData](../-s-f2-sample/index.md#1311705738%2FFunctions%2F-82533025)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [getDataClass](../-s-f2-sample/index.md#1847946936%2FFunctions%2F-82533025) | [jvm]<br>open fun [getDataClass](../-s-f2-sample/index.md#1847946936%2FFunctions%2F-82533025)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
| [getDirector](get-director.md) | [jvm]<br>open fun [getDirector](get-director.md)(performers: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ModelPerformer](../-model-performer/index.md)&gt;, channel: [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html), player: [ModelDirectedPlayer](../-model-directed-player/index.md)): [ModelDirector](../-model-director/index.md) |
| [getKeys](get-keys.md) | [jvm]<br>open fun [getKeys](get-keys.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt; |
| [getName](../-s-f2-sample/index.md#1635503817%2FFunctions%2F-82533025) | [jvm]<br>open fun [getName](../-s-f2-sample/index.md#1635503817%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getPatch](../-model-mapped-instrument/index.md#404430618%2FFunctions%2F-82533025) | [jvm]<br>open fun [getPatch](../-model-mapped-instrument/index.md#404430618%2FFunctions%2F-82533025)(): [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html) |
| [getPatchAlias](get-patch-alias.md) | [jvm]<br>open fun [getPatchAlias](get-patch-alias.md)(): [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html) |
| [getPerformers](get-performers.md) | [jvm]<br>open fun [getPerformers](get-performers.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ModelPerformer](../-model-performer/index.md)&gt; |
| [getSoundbank](../-s-f2-sample/index.md#-929831557%2FFunctions%2F-82533025) | [jvm]<br>open fun [getSoundbank](../-s-f2-sample/index.md#-929831557%2FFunctions%2F-82533025)(): [Soundbank](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Soundbank.html) |

## Inheritors

| Name |
|---|
| [SF2Instrument](../-s-f2-instrument/index.md) |
| [ModelMappedInstrument](../-model-mapped-instrument/index.md) |
