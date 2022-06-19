//[cache](../../../index.md)/[com.sun.media](../index.md)/[SF2Soundbank](index.md)

# SF2Soundbank

[jvm]\
open class [SF2Soundbank](index.md) : [Soundbank](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Soundbank.html)

A SoundFont 2.04 soundbank reader. Based on SoundFont 2.04 specification from: 

 http://developer.creative.com  http://www.soundfont.com/ ;

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [SF2Soundbank](-s-f2-soundbank.md) | [jvm]<br>open fun [SF2Soundbank](-s-f2-soundbank.md)() |
| [SF2Soundbank](-s-f2-soundbank.md) | [jvm]<br>open fun [SF2Soundbank](-s-f2-soundbank.md)(url: [URL](https://docs.oracle.com/javase/8/docs/api/java/net/URL.html)) |
| [SF2Soundbank](-s-f2-soundbank.md) | [jvm]<br>open fun [SF2Soundbank](-s-f2-soundbank.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) |
| [SF2Soundbank](-s-f2-soundbank.md) | [jvm]<br>open fun [SF2Soundbank](-s-f2-soundbank.md)(inputstream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)) |

## Functions

| Name | Summary |
|---|---|
| [addInstrument](add-instrument.md) | [jvm]<br>open fun [addInstrument](add-instrument.md)(resource: [SF2Instrument](../-s-f2-instrument/index.md)) |
| [addResource](add-resource.md) | [jvm]<br>open fun [addResource](add-resource.md)(resource: [SoundbankResource](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/SoundbankResource.html)) |
| [getDescription](get-description.md) | [jvm]<br>open fun [getDescription](get-description.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getInstrument](get-instrument.md) | [jvm]<br>open fun [getInstrument](get-instrument.md)(patch: [Patch](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Patch.html)): [Instrument](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Instrument.html) |
| [getInstruments](index.md#-183931252%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getInstruments](index.md#-183931252%2FFunctions%2F-82533025)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Instrument](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/Instrument.html)&gt; |
| [getName](index.md#-1658739401%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getName](index.md#-1658739401%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getResources](get-resources.md) | [jvm]<br>open fun [getResources](get-resources.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[SoundbankResource](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/SoundbankResource.html)&gt; |
| [getVendor](get-vendor.md) | [jvm]<br>open fun [getVendor](get-vendor.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getVersion](get-version.md) | [jvm]<br>open fun [getVersion](get-version.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [removeInstrument](remove-instrument.md) | [jvm]<br>open fun [removeInstrument](remove-instrument.md)(resource: [SF2Instrument](../-s-f2-instrument/index.md)) |
| [removeResource](remove-resource.md) | [jvm]<br>open fun [removeResource](remove-resource.md)(resource: [SoundbankResource](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/SoundbankResource.html)) |
| [save](save.md) | [jvm]<br>open fun [save](save.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html))<br>open fun [save](save.md)(out: [OutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html))<br>open fun [save](save.md)(name: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |
| [setDescription](set-description.md) | [jvm]<br>open fun [setDescription](set-description.md)(s: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |
| [setVendor](set-vendor.md) | [jvm]<br>open fun [setVendor](set-vendor.md)(s: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |

## Properties

| Name | Summary |
|---|---|
| [copyright](copyright.md) | [jvm]<br>protected open var [copyright](copyright.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [creationDate](creation-date.md) | [jvm]<br>protected open var [creationDate](creation-date.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [name](name.md) | [jvm]<br>protected open var [name](name.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [product](product.md) | [jvm]<br>protected open var [product](product.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [romName](rom-name.md) | [jvm]<br>protected open var [romName](rom-name.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [romVersionMajor](rom-version-major.md) | [jvm]<br>protected open var [romVersionMajor](rom-version-major.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [romVersionMinor](rom-version-minor.md) | [jvm]<br>protected open var [romVersionMinor](rom-version-minor.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [targetEngine](target-engine.md) | [jvm]<br>protected open var [targetEngine](target-engine.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [tools](tools.md) | [jvm]<br>protected open var [tools](tools.md): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
