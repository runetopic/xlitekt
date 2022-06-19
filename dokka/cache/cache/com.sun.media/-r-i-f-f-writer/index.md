//[cache](../../../index.md)/[com.sun.media](../index.md)/[RIFFWriter](index.md)

# RIFFWriter

[jvm]\
open class [RIFFWriter](index.md) : [OutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html)

Resource Interchange File Format (RIFF) stream encoder.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [RIFFWriter](-r-i-f-f-writer.md) | [jvm]<br>open fun [RIFFWriter](-r-i-f-f-writer.md)(name: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), format: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |
| [RIFFWriter](-r-i-f-f-writer.md) | [jvm]<br>open fun [RIFFWriter](-r-i-f-f-writer.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), format: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |
| [RIFFWriter](-r-i-f-f-writer.md) | [jvm]<br>open fun [RIFFWriter](-r-i-f-f-writer.md)(stream: [OutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html), format: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |

## Functions

| Name | Summary |
|---|---|
| [close](close.md) | [jvm]<br>open fun [close](close.md)() |
| [flush](index.md#1139125994%2FFunctions%2F-82533025) | [jvm]<br>open fun [flush](index.md#1139125994%2FFunctions%2F-82533025)() |
| [getFilePointer](get-file-pointer.md) | [jvm]<br>open fun [getFilePointer](get-file-pointer.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [getWriteOverride](get-write-override.md) | [jvm]<br>open fun [getWriteOverride](get-write-override.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [nullOutputStream](index.md#-935828636%2FFunctions%2F-82533025) | [jvm]<br>open fun [nullOutputStream](index.md#-935828636%2FFunctions%2F-82533025)(): [OutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html) |
| [seek](seek.md) | [jvm]<br>open fun [seek](seek.md)(pos: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [setWriteOverride](set-write-override.md) | [jvm]<br>open fun [setWriteOverride](set-write-override.md)(writeoverride: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [write](write.md) | [jvm]<br>open fun [write](write.md)(b: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>open fun [write](write.md)(b: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, off: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [writeByte](write-byte.md) | [jvm]<br>open fun [writeByte](write-byte.md)(b: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [writeChunk](write-chunk.md) | [jvm]<br>open fun [writeChunk](write-chunk.md)(format: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [RIFFWriter](index.md) |
| [writeInt](write-int.md) | [jvm]<br>open fun [writeInt](write-int.md)(b: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [writeList](write-list.md) | [jvm]<br>open fun [writeList](write-list.md)(format: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [RIFFWriter](index.md) |
| [writeLong](write-long.md) | [jvm]<br>open fun [writeLong](write-long.md)(b: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [writeShort](write-short.md) | [jvm]<br>open fun [writeShort](write-short.md)(b: [Short](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html)) |
| [writeString](write-string.md) | [jvm]<br>open fun [writeString](write-string.md)(string: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html))<br>open fun [writeString](write-string.md)(string: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [writeUnsignedByte](write-unsigned-byte.md) | [jvm]<br>open fun [writeUnsignedByte](write-unsigned-byte.md)(b: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [writeUnsignedInt](write-unsigned-int.md) | [jvm]<br>open fun [writeUnsignedInt](write-unsigned-int.md)(b: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [writeUnsignedShort](write-unsigned-short.md) | [jvm]<br>open fun [writeUnsignedShort](write-unsigned-short.md)(b: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
