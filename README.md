# xlitekt

[![wakatime](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81.svg)](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81)

# Introduction

XliteKt is a Kotlin based OSRS Emulator for educational purposes. Currently built around the game
update: [#202](https://oldschool.runescape.wiki/w/5_January) (Nex)

The goal behind this project is to provide the community with a powerful, yet simple to use framework that is _heavily_
documented.

## Getting Started

Make sure to download the Jan 6th #202 cache version from one of the archives below. We don't push this to github for
obvious reasons.

Download a cache from one of the archives:

- [archive.runestats.com](https://archive.runestats.com/osrs/)
- [archive.openrs2.org](https://archive.openrs2.org/)

Place the cache you downloaded into the ``./cache/`` folder inside of the project. This path is configurable in
the [application.conf](/src/main/resources/application.conf).

## Application configuration:

Most everything will be setup and configured already for you, unless you're porting to a recent revision.

**Configuration for ktor can be found in the ```ktor``` block.**
```shell
ktor {
    development = true
    deployment {
        port = 43594
        watch = [ classes, resources ]
    }
    application {
        modules = [ com.runetopic.xlitekt.ApplicationKt.main ]
    }
}
```

**Configuration for cache related properties can be found in the ```cache``` block.**
```shell
cache {
  path = "./cache/"
}
```

Configuration for game related properties. You will need to generate your RSA exponent and modulus to provide here, as well as the packet sizes.
We will write a tutorial in the future for dumping these from the client as well as a gradle task to generate RSA tokens.
```shell
game {
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
        exponent = ""
        modulus = ""
    }
}
```

Configuration related to network.
```shell
network {
    timeout = 10000
}
```

### Loading Maps
Maps are loaded using the xteas provided from Runestar, which follows this format:

```json
[
  {
    "mapsquare": 4662,
    "key": [
      327206325,
      671317497,
      652416162,
      -982088476
    ]
  }
]
```


