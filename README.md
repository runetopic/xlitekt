# xlitekt

[![wakatime](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81.svg)](https://wakatime.com/badge/user/00b793fe-9bcc-4e7a-88c2-7c1879c548ce/project/392eeeea-4500-4c18-904b-2c0d662dfb81)

# Introduction
XliteKt is a Kotlin based OSRS Emulator for educational purposes. Currently built around the game update: [#202](https://oldschool.runescape.wiki/w/5_January) (Nex)

The goal behind this project is to provide the community with a powerful, yet simple to use framework that is _heavily_ documented.
## Getting Started
Make sure to download the Jan 6th #202 cache version from one of the archives below. We don't push this to github for obvious reasons.

Download a cache from one of the archives:
- [archive.runestats.com](https://archive.runestats.com/osrs/)
- [archive.openrs2.org](https://archive.openrs2.org/)

Place the cache you downloaded into the ``./cache/`` folder inside of the project. This path is configurable in the [application.conf](/src/main/resources/application.conf)

```
ktor {
    development = true
    deployment {
        port = -1 # This is unused but it is still required.
        watch = [ classes, resources ]
    }
    application {
        modules = [ com.runetopic.xlitekt.ApplicationKt.main ]
    }
}
```

```json
cache {
    path = "./cache/"
}
```

```
network {
    port = 43594
    timeout = 10000
}
```
