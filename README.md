<p align="center"><img src="https://github.com/runetopic/xlitekt/blob/main/assets/xlite%20logo.png"/></p>

[![Discord](https://img.shields.io/discord/212385463418355713?color=%237289DA&logo=Discord&logoColor=%237289DA)](https://discord.gg/3scgBkrfMG)
[![wakatime](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81.svg)](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81)
 ![](https://tokei.rs/b1/github/runetopic/xlitekt)

# Getting Started
1. We provide an application-example.conf by default. Rename this file to ``application.conf``. The configurations are set up to run Xlite out of the box. Change any of the configurations to fit your needs.
2. Download the supported revision cache (.dat2 format) and map keys (.json format) from [OpenRS2](https://archive.openrs2.org/caches).
3. Place the cache files you downloaded into the ``./cache/data/`` directory. This is configurable in the application.conf under the application module.
4. Place the map keys you downloaded into the ``./application/resources/map/`` directory. ``xteas202.json`` is an example of how the file should be named.
5. Generate the server and client RSA key pair using the ``generateRSA`` Gradle task. Follow the additional instructions from the console after the task is executed. ``./gradlew generateRSA``
6. Download the revision gamepack.jar from [Runestats](https://archive.runestats.com/osrs/) and place this in the ``./application/resources/client_config/`` directory. ``gamepack.jar`` is an example of how the file should be named.
7. Run the application.

# Application configuration

_Most everything will be setup and configured already for you around the build #202. So unless you're porting to a recent revision you'll really only need to worry about updating the RSA Keys in the application.conf file._

**Configuration for ktor can be found in the ```ktor``` block.**
```shell
ktor {
    development = false
    deployment {
        port = 43594
        watch = [ classes, resources ]
    }
    application {
        modules = [ com.runetopic.xlitekt.ApplicationKt.main ]
    }
}
```
**Configuration for the game can be found in the ``game`` block.**

```shell
game {
    # Set to true if you want to benchmark the server performance.
    benchmarking = false
    # Target number of cores for the server to use for synchronization.
    # The maximum number of cores allowed = the number of cores on the CPU.
    cores = 8
    # Supported client build information.
    build {
        major = 202
        minor = 1
    }
    # Client -> Server packet sizes in order by packet ID.
    packet {
        sizes = [ 3, 8, 16, -1, -1, 8, 2, 1, 8, 0, 8, 8, 0, 2, 16, 16, 8, 9, 11, 8, 8, 7, 2, 4, 7, 4, -1, 7, 15, 8, -1, 8, 9, 7, 15, 6, -1, 3, 11, 15, 4, 8, 8, 8, -1, -2, 3, 3, 5, -1, -1, -1, -1, 7, 7, 0, 7, -1, -1, 3, -2, 3, 8, 3, 8, 0, 8, 4, 7, 11, 3, 6, 8, 11, 3, 8, 7, 4, -1, 2, 15, 7, 8, 3, 14, 3, -1, 10, 8, 13, 3, -1, 3, -1, 7, -1, 0, -2, -1, -1, 2, -1, -1, -1, 3, -1 ]
    }
    # The server cache files directory.
    cache {
        path = "./cache/"
    }
    # The RSA exponent and modulus for the login block.
    rsa {
        exponent = "74319506585812759823290259020934858088666404737094871101540670473424793671202076033582991318775440709937362678616598621443723414224839661646087632908361014054642952231258678163322462341878133664959918342102621655539431162351843502897522597279543226584696172903586455624355061037387268986011976499046968675073"
        modulus = "101152132894052393265886644489429469067887733993499471826334750806131431774995232950094045980615261210482740859538462033841944288877997111341162261129657268035424385776764492943939466200272309679088830878857767599863397432612329236019641861788901097158810527108145428907942159175673330991981851896173021952237"
    }
    # Server data resources paths.
    resources {
        xteas = "/map/xteas202.json"
        interface_info = "/ui/interface_info.json"
        sequences = "/sequence/sequences.json"
        spot_animations = "/spot_animation/spot_animations.json"
        varps = "/var/varps.json"
        varbits = "/var/varbits.json"
    }
}
```

# Features
- Embedded HTTP server to deliver client configuration files.
- Cache data loading.
- Client cache files updating.
- Client game login.
- ISAAC and RSA.
- Full synchronization between connected clients and game server.
- Full clipping.
- Player updating.
- Npc updating.
- Basic UI support.
- Basic vars support.
- Basic commands support.
- And more!

# Developers
 - [tyler27](https://github.com/tyler27)
 - [ultraviolet-jordan](https://github.com/ultraviolet-jordan)
 - [justin51](https://github.com/justin51)

# Benchmarking & Performance
XliteKt aims to be a very fast server which can easily handle thousands of players simultaneously and scales very well with better hardware. 
Even though XliteKt can run on low end machines, we highly suggest using a machine with a computer processor that has atleast 4 cores.

```shell
game {
    benchmarking = true
}
```

If you wish to benchmark this server on your machine, you can set this field in the application.conf file.
Then use the ::benchmark command in-game.

#### Sample Benchmarking Results
```
- BenchmarkParallelActorSynchronizer
- AMD Ryzen 9 3900X 12-Core Processor               4.20 GHz
- Windows 11 Pro 21H2 22000.675 
============================================================
- Pathfinders took 15.417400ms for 2000 players. [TICK=1258]
- Pathfinders took 1.402100ms for 22472 npcs. [TICK=1258]
- Movement routing took 920.6us for all entities. [TICK=1258]
- Pre tick took 3.079500ms for 2000 players. [TICK=1258]
- Main tick took 24.448800ms for 2000 players. [TICK=1258]
============================================================
- Synchronization completed in 45.760100ms.
============================================================
```

## Documentation
https://runetopic.github.io/xlitekt/
