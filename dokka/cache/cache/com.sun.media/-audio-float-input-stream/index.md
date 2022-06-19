//[cache](../../../index.md)/[com.sun.media](../index.md)/[AudioFloatInputStream](index.md)

# AudioFloatInputStream

[jvm]\
abstract class [AudioFloatInputStream](index.md)

This class is used to create AudioFloatInputStream from AudioInputStream and byte buffers.

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [available](available.md) | [jvm]<br>abstract fun [available](available.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [close](close.md) | [jvm]<br>abstract fun [close](close.md)() |
| [getFormat](get-format.md) | [jvm]<br>abstract fun [getFormat](get-format.md)(): [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html) |
| [getFrameLength](get-frame-length.md) | [jvm]<br>abstract fun [getFrameLength](get-frame-length.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [getInputStream](get-input-stream.md) | [jvm]<br>open fun [getInputStream](get-input-stream.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)): [AudioFloatInputStream](index.md)<br>open fun [getInputStream](get-input-stream.md)(stream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)): [AudioFloatInputStream](index.md)<br>open fun [getInputStream](get-input-stream.md)(url: [URL](https://docs.oracle.com/javase/8/docs/api/java/net/URL.html)): [AudioFloatInputStream](index.md)<br>open fun [getInputStream](get-input-stream.md)(stream: [AudioInputStream](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioInputStream.html)): [AudioFloatInputStream](index.md)<br>open fun [getInputStream](get-input-stream.md)(format: [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html), buffer: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [AudioFloatInputStream](index.md) |
| [mark](mark.md) | [jvm]<br>abstract fun [mark](mark.md)(readlimit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [markSupported](mark-supported.md) | [jvm]<br>abstract fun [markSupported](mark-supported.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [read](read.md) | [jvm]<br>open fun [read](read.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>open fun [read](read.md)(b: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>abstract fun [read](read.md)(b: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)&gt;, off: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [reset](reset.md) | [jvm]<br>abstract fun [reset](reset.md)() |
| [skip](skip.md) | [jvm]<br>abstract fun [skip](skip.md)(len: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
