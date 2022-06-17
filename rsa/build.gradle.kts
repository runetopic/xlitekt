import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec

/**
 * @author Jordan Abraham
 */
abstract class RSAGenTask : DefaultTask() {
    @TaskAction
    fun generate() {
        val factory = KeyFactory.getInstance("RSA")
        val pair = KeyPairGenerator.getInstance("RSA").also { it.initialize(1024) }.genKeyPair()
        val server = factory.getKeySpec(pair.private, RSAPrivateKeySpec::class.java)
        val client = factory.getKeySpec(pair.public, RSAPublicKeySpec::class.java)

        repeat(5) { println() }
        println("==================== Use the following for the application.conf ====================")
        println(
            """
                rsa {
                    exponent = "${server.privateExponent}"
                    modulus = "${server.modulus}"
                }
            """.trimIndent()
        )
        println()
        println("==================== Use following for the client ====================")
        println(
            """
                exponent = "${client.publicExponent}"
                modulus = "${client.modulus}"
            """.trimIndent()
        )
        repeat(5) { println() }
    }
}

tasks.register<RSAGenTask>("generateRSA")
