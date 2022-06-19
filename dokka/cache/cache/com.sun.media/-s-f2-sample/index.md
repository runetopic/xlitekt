//[cache](../../../index.md)/[com.sun.media](../index.md)/[SF2Sample](index.md)

# SF2Sample

[jvm]\
open class [SF2Sample](index.md) : [SoundbankResource](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/SoundbankResource.html)

Soundfont sample storage.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [SF2Sample](-s-f2-sample.md) | [jvm]<br>open fun [SF2Sample](-s-f2-sample.md)(soundBank: [Soundbank](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Soundbank.html)) |
| [SF2Sample](-s-f2-sample.md) | [jvm]<br>open fun [SF2Sample](-s-f2-sample.md)() |

## Functions

| Name | Summary |
|---|---|
| [getData](index.md#1311705738%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getData](index.md#1311705738%2FFunctions%2F-82533025)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [getData24Buffer](get-data24-buffer.md) | [jvm]<br>open fun [getData24Buffer](get-data24-buffer.md)(): [ModelByteBuffer](../-model-byte-buffer/index.md) |
| [getDataBuffer](get-data-buffer.md) | [jvm]<br>open fun [getDataBuffer](get-data-buffer.md)(): [ModelByteBuffer](../-model-byte-buffer/index.md) |
| [getDataClass](index.md#1847946936%2FFunctions%2F-82533025) | [jvm]<br>open fun [getDataClass](index.md#1847946936%2FFunctions%2F-82533025)(): [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; |
| [getFormat](get-format.md) | [jvm]<br>open fun [getFormat](get-format.md)(): [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html) |
| [getName](index.md#1635503817%2FFunctions%2F-82533025) | [jvm]<br>open fun [getName](index.md#1635503817%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getSoundbank](index.md#-929831557%2FFunctions%2F-82533025) | [jvm]<br>open fun [getSoundbank](index.md#-929831557%2FFunctions%2F-82533025)(): [Soundbank](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Soundbank.html) |
| [toString](to-string.md) | [jvm]<br>open fun [toString](to-string.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |

## Properties

| Name | Summary |
|---|---|
| [data](data.md) | [jvm]<br>protected open var [data](data.md): [ModelByteBuffer](../-model-byte-buffer/index.md) |
| [data24](data24.md) | [jvm]<br>protected open var [data24](data24.md): [ModelByteBuffer](../-model-byte-buffer/index.md) |
| [endLoop](end-loop.md) | [jvm]<br>protected open var [endLoop](end-loop.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [name](name.md) | [jvm]<br>protected open var [name](name.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [originalPitch](original-pitch.md) | [jvm]<br>protected open var [originalPitch](original-pitch.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [pitchCorrection](pitch-correction.md) | [jvm]<br>protected open var [pitchCorrection](pitch-correction.md): [Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html) |
| [sampleLink](sample-link.md) | [jvm]<br>protected open var [sampleLink](sample-link.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [sampleRate](sample-rate.md) | [jvm]<br>protected open var [sampleRate](sample-rate.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [sampleType](sample-type.md) | [jvm]<br>protected open var [sampleType](sample-type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [startLoop](start-loop.md) | [jvm]<br>protected open var [startLoop](start-loop.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
