# xlitekt
[![wakatime](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81.svg)](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81) ![](https://tokei.rs/b1/github/runetopic/xlitekt)

# Introduction
XliteKt is an open-source, and forever open-source Kotlin based OSRS Emulator for educational purposes. Currently built around the game
update: [#202](https://oldschool.runescape.wiki/w/5_January) (Nex)

The goal behind this project is to provide the community with a powerful, yet simple to use framework that is _heavily_
documented. 

Xlite is currently built by two developers:
 - [tyler27 (me)](https://github.com/tyler27)
 - [ultraviolet-jordan](https://github.com/ultraviolet-jordan)
# Getting Started
_Make sure to download the Jan 6th #202 cache version from one of the archives below. We don't push this to github for
obvious reasons._

Download a cache and xteas (JSON) from [archive.openrs2.org](https://archive.openrs2.org/).

Place the cache you downloaded into the ``./cache/`` folder inside of the project. This path is configurable in
the [application.conf](/src/main/resources/application.conf).

**Generate RSA tokens and update your application.conf file**

_This will eventually be a gradle task to generate new RSA keys. For now, you can reference the rune-server thread: [any-revision-enabling-rsa](https://www.rune-server.ee/runescape-development/rs2-server/tutorials/305532-any-revision-enabling-rsa.html)_

**Install the corresponding xteas inside of the [map](/src/main/resources/map) folder in resources.**

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
Configuration for game related properties. You will need to generate your RSA exponent and modulus, as well as the packet sizes.

We will be writing a tutorial in the future for dumping packets sizes and other information from the current OSRS client as well as the gradle task to generate RSA tokens.

**Please change the RSA tokens for a production based environment, these keys are just an example. Do not use this for anything other than local development.**
```shell
game {
    benchmarking = false
    build {
        major = 202
        minor = 1
    }
    packet {
        sizes = [ 3, 8, 16, -1, -1, 8, 2, 1, 8, 0, 8, 8, 0, 2, 16, 16, 8, 9, 11, 8, 8, 7, 2, 4, 7, 4, -1, 7, 15, 8, -1, 8, 9, 7, 15, 6, -1, 3, 11, 15, 4, 8, 8, 8, -1, -2, 3, 3, 5, -1, -1, -1, -1, 7, 7, 0, 7, -1, -1, 3, -2, 3, 8, 3, 8, 0, 8, 4, 7, 11, 3, 6, 8, 11, 3, 8, 7, 4, -1, 2, 15, 7, 8, 3, 14, 3, -1, 10, 8, 13, 3, -1, 3, -1, 7, -1, 0, -2, -1, -1, 2, -1, -1, -1, 3, -1 ]
    }
    cache {
        path = "./cache/"
    }
    rsa {
        exponent = "74319506585812759823290259020934858088666404737094871101540670473424793671202076033582991318775440709937362678616598621443723414224839661646087632908361014054642952231258678163322462341878133664959918342102621655539431162351843502897522597279543226584696172903586455624355061037387268986011976499046968675073"
        modulus = "101152132894052393265886644489429469067887733993499471826334750806131431774995232950094045980615261210482740859538462033841944288877997111341162261129657268035424385776764492943939466200272309679088830878857767599863397432612329236019641861788901097158810527108145428907942159175673330991981851896173021952237"
    }
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

# Maps
Maps are loaded using the xteas provided from Runestar, which follows this format:

```json
[
 {
    "archive": 5,
    "group": 5932,
    "name_hash": -1153353684,
    "name": "l44_81",
    "mapsquare": 11345,
    "key": [
      52989138,
      -933733387,
      916753309,
      1765351430
    ]
  }
]
```

## Documentation
https://runetopic.github.io/xlitekt/
