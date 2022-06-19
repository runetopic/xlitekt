//[cache](../../../index.md)/[com.sun.media](../index.md)/[RIFFReader](index.md)

# RIFFReader

[jvm]\
open class [RIFFReader](index.md) : [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)

Resource Interchange File Format (RIFF) stream decoder.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [RIFFReader](-r-i-f-f-reader.md) | [jvm]<br>open fun [RIFFReader](-r-i-f-f-reader.md)(stream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)) |

## Functions

| Name | Summary |
|---|---|
| [available](available.md) | [jvm]<br>open fun [available](available.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [close](close.md) | [jvm]<br>open fun [close](close.md)() |
| [finish](finish.md) | [jvm]<br>open fun [finish](finish.md)() |
| [getFilePointer](get-file-pointer.md) | [jvm]<br>open fun [getFilePointer](get-file-pointer.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [getFormat](get-format.md) | [jvm]<br>open fun [getFormat](get-format.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getSize](get-size.md) | [jvm]<br>open fun [getSize](get-size.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [getType](get-type.md) | [jvm]<br>open fun [getType](get-type.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [hasNextChunk](has-next-chunk.md) | [jvm]<br>open fun [hasNextChunk](has-next-chunk.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [mark](index.md#666203525%2FFunctions%2F-82533025) | [jvm]<br>open fun [mark](index.md#666203525%2FFunctions%2F-82533025)(readlimit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [markSupported](index.md#1745627422%2FFunctions%2F-82533025) | [jvm]<br>open fun [markSupported](index.md#1745627422%2FFunctions%2F-82533025)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [nextChunk](next-chunk.md) | [jvm]<br>open fun [nextChunk](next-chunk.md)(): [RIFFReader](index.md) |
| [nullInputStream](index.md#1251694428%2FFunctions%2F-82533025) | [jvm]<br>open fun [nullInputStream](index.md#1251694428%2FFunctions%2F-82533025)(): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) |
| [read](read.md) | [jvm]<br>open fun [read](read.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>open fun [read](read.md)(b: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [readAllBytes](index.md#1606115803%2FFunctions%2F-82533025) | [jvm]<br>open fun [readAllBytes](index.md#1606115803%2FFunctions%2F-82533025)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt; |
| [readByte](read-byte.md) | [jvm]<br>open fun [readByte](read-byte.md)(): [Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html) |
| [readFully](read-fully.md) | [jvm]<br>fun [readFully](read-fully.md)(b: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;)<br>fun [readFully](read-fully.md)(b: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, off: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [readInt](read-int.md) | [jvm]<br>open fun [readInt](read-int.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [readLong](read-long.md) | [jvm]<br>open fun [readLong](read-long.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [readNBytes](index.md#-1344779765%2FFunctions%2F-82533025) | [jvm]<br>open fun [readNBytes](index.md#-1344779765%2FFunctions%2F-82533025)(len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;<br>open fun [readNBytes](index.md#-358870234%2FFunctions%2F-82533025)(b: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, off: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [readShort](read-short.md) | [jvm]<br>open fun [readShort](read-short.md)(): [Short](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html) |
| [readString](read-string.md) | [jvm]<br>open fun [readString](read-string.md)(len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [readUnsignedByte](read-unsigned-byte.md) | [jvm]<br>open fun [readUnsignedByte](read-unsigned-byte.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [readUnsignedInt](read-unsigned-int.md) | [jvm]<br>open fun [readUnsignedInt](read-unsigned-int.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [readUnsignedShort](read-unsigned-short.md) | [jvm]<br>open fun [readUnsignedShort](read-unsigned-short.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [reset](index.md#376003056%2FFunctions%2F-82533025) | [jvm]<br>open fun [reset](index.md#376003056%2FFunctions%2F-82533025)() |
| [skip](skip.md) | [jvm]<br>open fun [skip](skip.md)(n: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [skipBytes](skip-bytes.md) | [jvm]<br>fun [skipBytes](skip-bytes.md)(n: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [skipNBytes](index.md#-1630995485%2FFunctions%2F-82533025) | [jvm]<br>open fun [skipNBytes](index.md#-1630995485%2FFunctions%2F-82533025)(n: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [transferTo](index.md#-1649141576%2FFunctions%2F-82533025) | [jvm]<br>open fun [transferTo](index.md#-1649141576%2FFunctions%2F-82533025)(out: [OutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
