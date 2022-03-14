package xlitekt.shared.config

import java.io.BufferedReader
import java.io.InputStreamReader

@JvmInline
value class JavaConfig(
    private val properties: MutableMap<String, String> = HashMap()
) {
    init {
        BufferedReader(InputStreamReader(this::class.java.getResourceAsStream(JAV_CONFIG_FILE)!!)).useLines {
            it.iterator().forEach { line ->
                var value = line
                var index = value.indexOf('=')
                if (index == -1) return@forEach
                var key = value.substring(0, index)
                when (key) {
                    "param" -> {
                        value = value.substring(index + 1)
                        index = value.indexOf('=')
                        key = value.substring(0, index)
                        properties[key] = value.substring(index + 1)
                    }
                    else -> properties[key] = value.substring(index + 1)
                }
            }
        }
    }

    val fileAsBytes: ByteArray get() = this::class.java.getResourceAsStream(JAV_CONFIG_FILE)!!.readAllBytes()
    val gamePackAsBytes: ByteArray get() = this::class.java.getResourceAsStream(GAME_PACK)!!.readAllBytes()
    val browserControl0: ByteArray get() = this::class.java.getResourceAsStream(BROWSER_CONTROL_0)!!.readAllBytes()
    val browserControl1: ByteArray get() = this::class.java.getResourceAsStream(BROWSER_CONTROL_1)!!.readAllBytes()

    companion object {
        const val JAV_CONFIG_FILE: String = "/client_config/jav_config.ws"
        const val GAME_PACK: String = "/client_config/gamepack.jar"
        const val BROWSER_CONTROL_0: String = "/client_config/browsercontrol_0.jar"
        const val BROWSER_CONTROL_1: String = "/client_config/browsercontrol_1.jar"
    }
}
