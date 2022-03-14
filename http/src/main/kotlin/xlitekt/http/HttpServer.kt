package xlitekt.http

import com.github.michaelbull.logging.InlineLogger
import io.ktor.application.call
import io.ktor.response.respondBytes
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import xlitekt.shared.config.JavaConfig
import xlitekt.shared.inject

class HttpServer {
    private val logger = InlineLogger()
    private val javaConfig by inject<JavaConfig>()

    fun start() {
        logger.debug { "Setting up HTTP Server." }

        embeddedServer(Netty, port = 8080) {
            routing {
                get("/jav_config.ws") {
                    call.respondBytes { javaConfig.fileAsBytes }
                }
                get("/gamepack.jar") {
                    call.respondBytes { javaConfig.gamePackAsBytes }
                }
                get("/browsercontrol_0.jar") {
                    call.respondBytes { javaConfig.browserControl0 }
                }
                get("/browsercontrol_1.jar") {
                    call.respondBytes { javaConfig.browserControl1 }
                }
            }
        }.start(wait = true)
    }
}
