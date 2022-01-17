import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.startListeningOnPort
import java.nio.file.Path

const val PORT = 43594 // TODO move this into a properties file or some shit
const val IO_TIMEOUT = 10_000L // TODO move this into a properties file or some shit
val store = Js5Store(path = Path.of("${System.getProperty("user.home")}/202/"), parallel = true)
// TODO make store injectable with koin

fun main() {
    startListeningOnPort(PORT)
}
