package xlitekt.cache.provider.music

import io.ktor.util.copy
import io.ktor.util.moveToByteArray
import io.ktor.utils.io.core.writeInt
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.dynamicBuffer
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.buffer.readVarInt
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class MusicEntryTypeProvider : EntryTypeProvider<MusicEntryType>() {

    override fun load(): Map<Int, MusicEntryType> = store
        .index(MUSIC_INDEX)
        .groups()
        .onEach { require((glossary[it.nameHash]?.toNameHash() ?: it.nameHash) == it.nameHash) }
        .map { ByteBuffer.wrap(it.data).loadEntryType(MusicEntryType(it.id, name = glossary[it.nameHash] ?: it.nameHash.toString())) }
        .associateBy(MusicEntryType::id)

    override fun ByteBuffer.loadEntryType(type: MusicEntryType): MusicEntryType {
        val header = copy().header()
        val tracks = header.readUByte()
        val division = header.readUShort()
        var offset = 14 + tracks * 10

        var tempoOpcodes = 0
        var ctrlChangeOpcodes = 0
        var noteOnOpcodes = 0
        var noteOffOpcodes = 0
        var wheelChangeOpcodes = 0
        var chnnlAfterTchOpcodes = 0
        var keyAfterTchOpcodes = 0
        var progmChangeOpcodes = 0

        val body = copy().body()
        var controlChangeIndex: Int
        var opcode: Int
        repeat(tracks) {
            opcode = -1
            while (true) {
                controlChangeIndex = body.readUByte()
                if (controlChangeIndex != opcode) {
                    ++offset
                }
                opcode = controlChangeIndex and 15
                if (controlChangeIndex == 7) {
                    break
                }
                when {
                    controlChangeIndex == 23 -> ++tempoOpcodes
                    opcode == 0 -> ++noteOnOpcodes
                    opcode == 1 -> ++noteOffOpcodes
                    opcode == 2 -> ++ctrlChangeOpcodes
                    opcode == 3 -> ++wheelChangeOpcodes
                    opcode == 4 -> ++chnnlAfterTchOpcodes
                    opcode == 5 -> ++keyAfterTchOpcodes
                    opcode == 6 -> ++progmChangeOpcodes
                    else -> throw IllegalArgumentException("Control change index is not within the correct bounds. It was $controlChangeIndex.")
                }
            }
        }

        offset += tempoOpcodes * 5
        offset += (noteOnOpcodes + noteOffOpcodes + ctrlChangeOpcodes + wheelChangeOpcodes + keyAfterTchOpcodes) * 2
        offset += chnnlAfterTchOpcodes + progmChangeOpcodes
        opcode = tracks + tempoOpcodes + ctrlChangeOpcodes + noteOnOpcodes + noteOffOpcodes + wheelChangeOpcodes + chnnlAfterTchOpcodes + keyAfterTchOpcodes + progmChangeOpcodes

        val var13 = copy().offset(body.remaining())

        controlChangeIndex = 0
        while (controlChangeIndex < opcode) {
            body.readVarInt()
            ++controlChangeIndex
        }

        offset += copy().offset(body.remaining()) - var13
        controlChangeIndex = copy().offset(body.remaining())

        var modulationWheelSize = 0
        var modulationWheel2Size = 0
        var channelVolumeSize = 0
        var channelVolume2Size = 0
        var panSize = 0
        var pan2Size = 0
        var nonRegisteredMsbSize = 0
        var nonRegisteredLsbSize = 0
        var registeredNumberMsb = 0
        var registeredLsbSize = 0
        var commandsSize = 0
        var otherSize = 0
        var controllerNumber = 0

        repeat(ctrlChangeOpcodes) {
            controllerNumber = controllerNumber + body.readUByte() and 127
            when (controllerNumber) {
                0, 32 -> ++progmChangeOpcodes
                1 -> ++modulationWheelSize
                33 -> ++modulationWheel2Size
                7 -> ++channelVolumeSize
                39 -> ++channelVolume2Size
                10 -> ++panSize
                42 -> ++pan2Size
                99 -> ++nonRegisteredMsbSize
                98 -> ++nonRegisteredLsbSize
                101 -> ++registeredNumberMsb
                100 -> ++registeredLsbSize
                64, 65, 120, 121, 123 -> ++commandsSize
                else -> ++otherSize
            }
        }

        var commandsIndex = copy().offset(body.remaining())
        body.discard(commandsSize)

        var polyPressureIndex = copy().offset(body.remaining())
        body.discard(keyAfterTchOpcodes)

        var channelPressureIndex = copy().offset(body.remaining())
        body.discard(chnnlAfterTchOpcodes)

        var pitchWheelHighIndex = copy().offset(body.remaining())
        body.discard(wheelChangeOpcodes)

        var modulationWheelOffset = copy().offset(body.remaining())
        body.discard(modulationWheelSize)

        var channelVolumeOffset = copy().offset(body.remaining())
        body.discard(channelVolumeSize)

        var panOffset = copy().offset(body.remaining())
        body.discard(panSize)

        var notesIndex = copy().offset(body.remaining())
        body.discard(noteOnOpcodes + noteOffOpcodes + keyAfterTchOpcodes)

        var notesOnIndex = copy().offset(body.remaining())
        body.discard(noteOnOpcodes)

        var otherIndex = copy().offset(body.remaining())
        body.discard(otherSize)

        var notesOffIndex = copy().offset(body.remaining())
        body.discard(noteOffOpcodes)

        var modulationWheel2Offset = copy().offset(body.remaining())
        body.discard(modulationWheel2Size)

        var channelVolume2Offset = copy().offset(body.remaining())
        body.discard(channelVolume2Size)

        var pan2Offset = copy().offset(body.remaining())
        body.discard(pan2Size)

        var programChangeIndex = copy().offset(body.remaining())
        body.discard(progmChangeOpcodes)

        var pitchWheelLowIndex = copy().offset(body.remaining())
        body.discard(wheelChangeOpcodes)

        var nonRegisteredMsbIndex = copy().offset(body.remaining())
        body.discard(nonRegisteredMsbSize)

        var nonRegisteredLsbIndex = copy().offset(body.remaining())
        body.discard(nonRegisteredLsbSize)

        var registeredMsbIndex = copy().offset(body.remaining())
        body.discard(registeredNumberMsb)

        var registeredLsbIndex = copy().offset(body.remaining())
        body.discard(registeredLsbSize)

        var tempoOffset = copy().offset(body.remaining())
        body.discard(tempoOpcodes * 3)

        val midi = copy().midi(var13)
        val bytes = moveToByteArray()

        val buffer = dynamicBuffer {
            writeInt(1297377380)
            writeInt(6)
            writeShort(if (tracks > 1) 1 else 0)
            writeShort(tracks)
            writeShort(division)

            var channel = 0
            var note = 0
            var noteOn = 0
            var noteOff = 0
            var pitchWheel = 0
            var channelPressure = 0
            var polyPressure = 0
            val var59 = IntArray(128)
            controllerNumber = 0

            var index = 0

            loop@
            for (var60 in 0 until tracks) {
                writeInt(1297379947)
                writeInt(0) // Temporary length.

                val startSize = this.size
                var id = -1
                while (true) {
                    writeVarInt(midi::readVarInt)
                    val status = bytes[index++].toInt() and 0xff
                    val switch = status != id
                    id = status and 15
                    if (status == 7) {
                        // if (var65) writeByte { 255 } This is in the client, but it breaks in midi player.
                        writeByte(255) // This is the fix.
                        writeByte(47)
                        writeByte(0)
                        writeLengthInt { this.size - startSize } // Replace the length from above.
                        continue@loop
                    }
                    if (status == 23) {
                        // if (var65) writeByte { 255 } This is in the client, but it breaks in midi player.
                        writeByte(255) // This is the fix.
                        writeByte(81)
                        writeByte(3)
                        writeByte(bytes[tempoOffset++].toInt())
                        writeByte(bytes[tempoOffset++].toInt())
                        writeByte(bytes[tempoOffset++].toInt())
                    } else {
                        channel = channel xor (status shr 4)
                        when (id) {
                            0 -> {
                                if (switch) writeByte(channel + 144)
                                note += bytes[notesIndex++]
                                noteOn += bytes[notesOnIndex++]
                                writeByte(note and 127)
                                writeByte(noteOn and 127)
                            }
                            1 -> {
                                if (switch) writeByte(channel + 128)
                                note += bytes[notesIndex++]
                                noteOff += bytes[notesOffIndex++]
                                writeByte(note and 127)
                                writeByte(noteOff and 127)
                            }
                            2 -> {
                                if (switch) writeByte(channel + 176)
                                controllerNumber = controllerNumber + bytes[controlChangeIndex++] and 127
                                writeByte(controllerNumber)
                                val change = when (controllerNumber) {
                                    0, 32 -> bytes[programChangeIndex++]
                                    1 -> bytes[modulationWheelOffset++]
                                    33 -> bytes[modulationWheel2Offset++]
                                    7 -> bytes[channelVolumeOffset++]
                                    39 -> bytes[channelVolume2Offset++]
                                    10 -> bytes[panOffset++]
                                    42 -> bytes[pan2Offset++]
                                    99 -> bytes[nonRegisteredMsbIndex++]
                                    98 -> bytes[nonRegisteredLsbIndex++]
                                    101 -> bytes[registeredMsbIndex++]
                                    100 -> bytes[registeredLsbIndex++]
                                    64, 65, 120, 121, 123 -> bytes[commandsIndex++]
                                    else -> bytes[otherIndex++]
                                }
                                val controlChange = change + var59[controllerNumber]
                                var59[controllerNumber] = controlChange
                                writeByte(controlChange and 127)
                            }
                            3 -> {
                                if (switch) writeByte(channel + 224)
                                pitchWheel += bytes[pitchWheelLowIndex++]
                                pitchWheel += bytes[pitchWheelHighIndex++].toInt() shl 7
                                writeByte(pitchWheel and 127)
                                writeByte(pitchWheel shr 7 and 127)
                            }
                            4 -> {
                                if (switch) writeByte(channel + 208)
                                channelPressure += bytes[channelPressureIndex++]
                                writeByte(channelPressure and 127)
                            }
                            5 -> {
                                if (switch) writeByte(channel + 160)
                                note += bytes[notesIndex++]
                                polyPressure += bytes[polyPressureIndex++]
                                writeByte(note and 127)
                                writeByte(polyPressure and 127)
                            }
                            6 -> {
                                if (switch) writeByte(channel + 192)
                                writeByte(bytes[programChangeIndex++].toInt())
                            }
                            else -> throw IllegalArgumentException("Out of bounds. Was $id.")
                        }
                    }
                }
            }
        }
        type.bytes = buffer
        assertEmptyAndRelease()
        return type
    }

    private fun ByteBuffer.header(): ByteBuffer {
        discard(remaining() - 3)
        val bytes = ByteArray(remaining())
        get(bytes)
        return ByteBuffer.wrap(bytes)
    }

    private fun ByteBuffer.body(): ByteBuffer {
        val bytes = ByteArray(remaining())
        get(bytes)
        return ByteBuffer.wrap(bytes)
    }

    private fun ByteBuffer.midi(offset: Int): ByteBuffer {
        discard(offset)
        val bytes = ByteArray(remaining())
        get(bytes)
        return ByteBuffer.wrap(bytes)
    }

    private fun ByteBuffer.offset(amount: Int) = remaining() - amount

    private inline fun ByteBuffer.writeVarInt(value: () -> Int) = value.invoke().also {
        if (it and -128 != 0) {
            if (it and -16384 != 0) {
                if (it and -2097152 != 0) {
                    if (it and -268435456 != 0) {
                        writeByte(it ushr 28 or 128)
                    }
                    writeByte(it ushr 21 or 128)
                }
                writeByte(it ushr 14 or 128)
            }
            writeByte(it ushr 7 or 128)
        }
        writeByte(it and 127)
    }

    private inline fun ByteBuffer.writeLengthInt(value: () -> Int) = value.invoke().also {
        array()[position() - it - 4] = (it shr 24).toByte()
        array()[position() - it - 3] = (it shr 16).toByte()
        array()[position() - it - 2] = (it shr 8).toByte()
        array()[position() - it - 1] = it.toByte()
    }

    private companion object {
        val glossary = mapOf(
            1120933843 to "scape main",
            3225350 to "iban",
            828650857 to "autumn voyage",
            -1377700863 to "unknown land",
            1029455878 to "hells bells",
            -614076819 to "sad meadow",
            -1154441378 to "jollyr",
            530068296 to "overture",
            -1172405897 to "wildwood",
            -710537653 to "kingdom",
            104080482 to "moody",
            -1998869913 to "spooky2",
            -419218284 to "long way home",
            399048409 to "mage arena",
            -944748869 to "witching",
            35762567 to "workshop",
            627705588 to "scape main default",
            -1294172031 to "escape",
            1097468315 to "horizon",
            -1779111734 to "arabique",
            518814479 to "lullaby",
            1532279978 to "monarch waltz",
            1038911415 to "gnome king",
            1473393027 to "fe fi fo fum",
            -675357975 to "attack1",
            -675357974 to "attack2",
            -675357973 to "attack3",
            -675357972 to "attack4",
            -675357971 to "attack5",
            -675357970 to "attack6",
            437480876 to "voodoo cult",
            -1580150958 to "fight of the basilisk",
            -810515425 to "voyage",
            1846633612 to "gnome village",
            -782211141 to "wonder",
            -398925062 to "sea shanty2",
            -750127868 to "arabian",
            1234827707 to "deep wildy",
            -1066798491 to "trawler",
            345324413 to "church music 1",
            345324414 to "church music 2",
            122265833 to "expecting",
            819884324 to "wilderness2",
            819884325 to "wilderness3",
            981183822 to "right on track",
            -2098286081 to "venture2",
            124995564 to "harmony2",
            -1189743137 to "duel arena",
            -1890130256 to "morytania",
            -795140435 to "wander",
            1513246078 to "al kharid",
            -850506182 to "trawler minor",
            -905842564 to "serene",
            -925031874 to "royale",
            1121125956 to "scape soft",
            -751102526 to "high seas",
            1202794514 to "doorways",
            -1779684630 to "rune essence",
            105001967 to "nomad",
            -1349119470 to "cursed",
            -47057524 to "lasting",
            460367020 to "village",
            1749113330 to "newbie melody",
            1143353537 to "chain of command",
            289742397 to "book of spells",
            -672706748 to "miracle dance",
            -1106570438 to "legion",
            -839455633 to "close quarters",
            -1367483767 to "cavern",
            96463963 to "egypt",
            1306691868 to "upcoming",
            1274780903 to "chompy hunt",
            -1082154559 to "fanfare",
            -440204630 to "alls fairy in love n war",
            686705631 to "lightwalk",
            347955347 to "venture",
            696768774 to "harmony",
            284766976 to "splendour",
            -934797897 to "reggae",
            907815588 to "the desert",
            -324496873 to "soundscape",
            1640556978 to "wonderous",
            -213632750 to "waterfall",
            1802291895 to "big chords",
            788224888 to "dead quiet",
            -816227352 to "vision",
            -40521666 to "dimension x",
            -520702427 to "ice melody",
            1650323088 to "twilight",
            1086036315 to "reggae2",
            -1487348923 to "ambient jungle",
            -1228279453 to "riverside",
            -12868552 to "sea shanty",
            -995428255 to "parade",
            -1060046352 to "tribal2",
            582031337 to "intrepid",
            -1860080918 to "inspiration",
            -1220755677 to "hermit",
            -677662361 to "forever",
            -333224315 to "baroque",
            -1392319985 to "beyond",
            1411067174 to "gnome village2",
            92909147 to "alone",
            1377351472 to "oriental",
            549358875 to "camelot",
            -1037172987 to "tomorrow",
            -1309477156 to "expanse",
            1264132816 to "miles away",
            -2136059388 to "starlight",
            110327241 to "theme",
            1363656441 to "serenade",
            -988841056 to "still night",
            -103077377 to "gnomeball",
            686441581 to "lightness",
            -1254483584 to "jungly1",
            -1254483583 to "jungly2",
            1958759012 to "greatness",
            -1254483582 to "jungly3",
            -1282090556 to "faerie",
            -848436598 to "fishing",
            2061491048 to "shining",
            1503566841 to "forbidden",
            109407595 to "shine",
            -1779127378 to "arabian2",
            -1779127377 to "arabian3",
            -1253087691 to "garden",
            95997798 to "we are the fairies",
            1366257555 to "nightfall",
            -1237289460 to "grumpy",
            -170561624 to "spookyjungle",
            788399136 to "tree spirits",
            792536868 to "understanding",
            -1380919269 to "breeze",
            2122572442 to "the tower",
            -123912401 to "la mort",
            -429593707 to "jaws of the basilisk",
            -1526440800 to "lair of the basilisk",
            -493822008 to "jormungand defeated jingle",
            -1624274920 to "emperor",
            849596836 to "ballad of the basilisk",
            922007495 to "talking forest",
            -1960860275 to "barbarianism",
            -1644401602 to "complication",
            1976894499 to "down to earth",
            1120636327 to "scape cave",
            1258863383 to "yesteryear",
            -705938181 to "zealot",
            2092627105 to "silence",
            -1624760229 to "emotion",
            910299584 to "principality",
            949634504 to "mouse trap",
            109757538 to "start",
            736568812 to "ballad of enchantment",
            -485932799 to "expedition",
            -1951786153 to "bone dance",
            -969918857 to "neverland",
            1966766798 to "mausoleum",
            -900633031 to "medieval",
            107944162 to "quest",
            3165239 to "gaol",
            777534707 to "army of darkness",
            -2075972251 to "long ago",
            1033441676 to "tribal background",
            1398587265 to "flute salad",
            1216634785 to "landlubber",
            -865479038 to "tribal",
            812947089 to "fanfare2",
            812947090 to "fanfare3",
            -2075333010 to "lonesome",
            -528864109 to "crystal sword",
            1339486127 to "the shadow",
            3392903 to "null",
            1769177816 to "jungle island",
            -1320617626 to "dunjun",
            -51091830 to "desert voyage",
            -895939599 to "spirit",
            -275310687 to "undercurrent",
            -694094064 to "adventure",
            957931606 to "courage",
            795515487 to "underground",
            -353951458 to "attention",
            813726263 to "crystal cave",
            -1216167350 to "dangerous",
            1687654733 to "troubled",
            1320694328 to "magical journey",
            -378865792 to "magic dance",
            -734206983 to "arrival",
            -1063411723 to "tremble",
            694847251 to "in the manor",
            -606457701 to "wolf mountain",
            295831445 to "heart and mind",
            -552301350 to "knightly",
            -1059680853 to "trinity",
            -1077789440 to "mellow",
            789609582 to "brimstail's scales",
            -1349658313 to "delrith summoning",
            -1842191205 to "wally cutscene",
            -1829469821 to "lament of meiyerditch",
            -2134967800 to "dagannoth dawn",
            -31416935 to "jormungand fight",
            2142215577 to "the mollusc menace",
            429244831 to "slug a bug ball",
            -1236252722 to "prime time",
            1381363755 to "my arms journey",
            -1055503808 to "roc and roll",
            1006643748 to "high spirits",
            -1658514874 to "floating free",
            -943885542 to "scape hunter",
            -822106577 to "jungle island xmas",
            -919642451 to "jungle bells",
            -1980407601 to "sea shanty xmas",
            -356730043 to "pirates of penance",
            -1057729269 to "barbarian assault tutorial",
            -28982081 to "labyrinth",
            1691516951 to "undead dungeon",
            -1304031901 to "lotr_jingle_3",
            123560953 to "espionage",
            1318893900 to "have an ice day",
            1586342879 to "peng_bards_jingle",
            -1042007977 to "peng_plans_jingle",
            -1891851953 to "island of the trolls",
            2103661451 to "jester minute",
            796868952 to "major miner",
            -180851958 to "norse code",
            827249681 to "ogre the top",
            -95571520 to "volcanic vikings",
            -1061250740 to "fris_jingle_1",
            279431252 to "garden of autumn",
            790067275 to "garden of spring",
            794539501 to "garden of summer",
            898010371 to "garden of winter",
            -967559823 to "creature cruelty",
            -1197347961 to "magic magic magic",
            1030045177 to "mutant medley",
            -665666447 to "work work work",
            817472004 to "zombiism",
            1427539525 to "brain_barrelchest_battle",
            346263512 to "dorgeshun city",
            1306461568 to "stagnant",
            -2078908549 to "time out",
            -1350228392 to "stratosphere",
            1041911129 to "waterlogged",
            1728911401 to "natural",
            -1237461365 to "grotto",
            -1228392498 to "artistry",
            93330745 to "aztec",
            346288985 to "dorgeshun deep",
            -1793433037 to "surok_king_battle_music",
            -1268786147 to "forest",
            1814277765 to "elven mist",
            584643951 to "lost soul",
            -499867199 to "meridian",
            -56804840 to "woodland",
            529929957 to "overpass",
            -2032107216 to "sojourn",
            951530772 to "contest",
            306819362 to "crystal castle",
            -1081041422 to "insect queen",
            260940912 to "marzipan",
            1827366203 to "righteousness",
            1393517697 to "bandit camp",
            332368736 to "mad eadgar",
            -1307116191 to "superstition",
            1968917071 to "bone dry",
            -1857025509 to "sunburn",
            404357804 to "everywhere",
            -1095396929 to "competition",
            -1309055712 to "exposed",
            -901674570 to "well of voyage",
            -1526067851 to "alternative root",
            -468596910 to "easter jig",
            582140282 to "rising damp",
            1301622585 to "slice of station",
            -628963539 to "ham and seek",
            -2092714094 to "haunted mine",
            -2038936746 to "deep down",
            -276138668 to "ham attack",
            615365769 to "slice of grand opening",
            1994744000 to "slice of silent movie",
            738909086 to "chamber",
            378300078 to "everlasting",
            -74307138 to "miscellania",
            -1562452687 to "etcetera",
            -1019905269 to "shadowland",
            3314014 to "lair",
            -1567437308 to "deadlands",
            -544722449 to "rellekka",
            3522472 to "saga",
            1825640471 to "borderland",
            1787618597 to "stranded",
            -1106574323 to "legend",
            271319484 to "frostbite",
            1124565314 to "warrior",
            -1679325940 to "technology",
            -1564148198 to "etcetera theme",
            -1418827919 to "illusive",
            -1601127242 to "inadequacy",
            432605856 to "untouchable",
            -1665011705 to "down and out",
            1427043851 to "on the up",
            -1025233830 to "monkey madness",
            250959119 to "marooned",
            -327707013 to "anywhere",
            528722471 to "island life",
            -877351859 to "temple",
            133626717 to "suspicious",
            -1165315580 to "looking back",
            2023201035 to "dwarf theme",
            -338347745 to "showdown",
            740093634 to "find my way",
            212205923 to "goblin village",
            115411843 to "castlewars",
            -1010529595 to "impetuous_clue",
            -1479412376 to "the navigator",
            825974316 to "melodrama",
            286265996 to "ready for battle",
            497375231 to "stillness",
            -200388662 to "lighthouse",
            -645977478 to "scape scared",
            -454421102 to "out of the deep",
            111485446 to "upass",
            -1332194002 to "background",
            -1685231711 to "cave background",
            3075958 to "dark",
            95848451 to "dream",
            103666243 to "march",
            108392383 to "regal",
            621171714 to "cellar song",
            -1764950404 to "scape sad",
            1121239524 to "scape wild",
            -895763669 to "spooky",
            1523653533 to "pirates of peril",
            -1482676188 to "romancing the crone",
            908430134 to "dangerous road",
            -303898981 to "faithless",
            -873564465 to "tiptoe",
            563269755 to "the terrible tower",
            40246002 to "masquerade",
            1343200077 to "the slayer",
            -1618729246 to "body parts",
            -1858265682 to "monster melee",
            1999746381 to "fenkenstrain's refrain",
            1960215130 to "barking mad",
            -651951461 to "goblin game",
            -634763748 to "fruits de mer",
            79789174 to "narnode's theme",
            -492926285 to "impetuous",
            2124773424 to "dynasty",
            -908183966 to "scarab",
            -1032629963 to "shipwrecked",
            -418223472 to "phasmatys",
            -720253066 to "the other side",
            73828649 to "settlement",
            -2099722614 to "cave of beasts",
            1880989696 to "dragontooth island",
            1509400204 to "sarcophagus",
            -148552909 to "down below",
            1854274741 to "karamja jam",
            -1487589606 to "7th realm",
            1235442953 to "pathways",
            1837251043 to "melzars maze",
            1509070203 to "eagle peak",
            1447063382 to "barb wire",
            -1573228346 to "observatory telescope cutscene",
            -999707515 to "time to mine",
            -691855347 to "in between",
            1343649581 to "schools out",
            862821975 to "far away",
            -771284962 to "claustrophobia",
            1814357716 to "knightmare",
            1711341885 to "fight or flight",
            -728886272 to "temple of light",
            2110556093 to "the golem",
            1652745754 to "forgotten",
            -8976533 to "throne of the demon",
            -783693496 to "dance of the undead",
            -663428071 to "dangerous way",
            544229147 to "lore and order",
            -874529881 to "city of the dead",
            1134405764 to "hypnotized",
            1480073951 to "kr_betray_jingle",
            -957019274 to "too many cooks",
            -895977880 to "sphinx",
            -1073927447 to "mirage",
            -1773920521 to "cave of the goblins",
            -89244313 to "romper chomper",
            -650944128 to "strength of saradomin",
            -440187560 to "zogre dance",
            -587569902 to "path of peril",
            1131171307 to "wayward",
            46273615 to "tale of keldagrim",
            -649484675 to "land of the dwarves",
            221109227 to "tears of guthix",
            1267356434 to "the power of tears",
            -359173459 to "zamorak zoo",
            -43712789 to "scape original",
            -521895311 to "the adventurer",
            -1588113323 to "the rogues den",
            375695247 to "the far side",
            -1846853118 to "armageddon",
            155289058 to "giant dwarf meeting",
            1498787169 to "sailing journey2",
            1326424637 to "the lost melody",
            1119460311 to "bandos battalion",
            -1249495153 to "frogland",
            1921157304 to "lost tribe cutscene",
            1364992651 to "evil bobs island",
            848123561 to "into the abyss",
            196677638 to "the quizmaster",
            1808345541 to "armadyl alliance",
            -1642689926 to "athletes foot",
            -127408236 to "gnome_village_party",
            -1902858744 to "beneath_the_stronghold",
            -395250469 to "corporal punishment",
            -2048535896 to "pheasant peasant",
            465278529 to "the lost tribe",
            -826562194 to "troubled_waters",
            747848680 to "nether_realm",
            -1253085654 to "scorpia_dances",
            -78220817 to "devils_may_care",
            881850881 to "the chosen",
            -309570839 to "pick_and_shovel",
            1124498189 to "warpath",
            687938017 to "clanwars",
            -1658386264 to "shining_spirit",
            201526300 to "corporealbeast",
            -339706871 to "grimly_fiendish",
            3059343 to "coil",
            1936130561 to "thrall_of_the_serpent",
            -415134015 to "have a blast",
            -1913214770 to "wilderness",
            1717999087 to "forgettable melody",
            -140492390 to "bunny_sugar_rush",
            -2012159921 to "drunken dwarf",
            1966781751 to "maws_jaws_claws",
            -1028580907 to "that_sullen_hall",
            3530505 to "sire",
            781557721 to "dies_irae",
            1690742645 to "nox_irae",
            1740872686 to "soulfall",
            445640248 to "rugged_terrain",
            415928477 to "zeah_mining",
            1465443077 to "over to nardah",
            1250935993 to "the monsters below",
            135141185 to "zeah_combat",
            -1904094243 to "zeah_fishing",
            623451622 to "kourend_the_magnificent",
            939546513 to "forlorn_homestead",
            -860755690 to "jungle hunt",
            -282886672 to "home sweet home",
            1814287296 to "zeah_magic",
            -2133902017 to "zeah_farming",
            1389384362 to "monkey trouble",
            -1763090403 to "scape_ape",
            4820960 to "monkey sadness",
            -2130741313 to "joy of the hunt",
            1915718129 to "the desolate isle",
            1765722413 to "spirits of elid",
            1643875326 to "fire and brimstone",
            2110260221 to "the genie",
            2001751835 to "desert heat",
            -1623296531 to "ground scape",
            1581724013 to "monkey business",
            2097127567 to "monkey badness",
            -808772318 to "in the pits",
            466902883 to "strange place",
            -907669678 to "brew hoo hoo",
            -1124681475 to "darkly_altared",
            -858121616 to "tzhaar",
            -1256560486 to "last_man_standing",
            -2065077267 to "wild side",
            -1691854169 to "dead can dance",
            -683526139 to "wasteland",
            1705947058 to "the cellar dwellers",
            580384095 to "jungle troubles",
            693567775 to "mined out",
            -1021014225 to "catch me if you can",
            -271106892 to "rat a tat tat",
            -1751139908 to "wintertodt_boss",
            -1825588455 to "bobs on holiday",
            -200702983 to "the noble rodent",
            -1522984472 to "altar_ego",
            1868377358 to "lower_depths",
            1080306793 to "shayzien_march",
            1529837717 to "bubble and squeak",
            82917947 to "sarim's vermin",
            359174830 to "rat hunt",
            -1686202291 to "upper_depths",
            30362023 to "olm_battle",
            -1408684838 to "ascent",
            -1776024210 to "desolate_mage",
            1179379180 to "the trade parade",
            1160873524 to "aye car rum ba",
            -1947119982 to "blistering barnacles",
            -649601274 to "darkness_in_the_depths",
            1945133711 to "inferno",
            -564582358 to "distant land",
            -1453405761 to "mor-ul-rek",
            1801140808 to "fangs for the memory",
            284435223 to "pharoah's tomb",
            -1877545169 to "land down under",
            -1043985601 to "meddling kids",
            -2002535437 to "corridors of power",
            1609255038 to "slither and thither",
            685934899 to "in the clink",
            -84626226 to "mudskipper melody",
            72999866 to "subterranea",
            -1126455201 to "gargoyleboss",
            1116844876 to "incantation",
            344336468 to "grip of the talon",
            386188792 to "revenants",
            -511685441 to "on_the_shore",
            669250807 to "land of snow",
            -90350772 to "xenophobe",
            -956253112 to "title fight",
            -586830894 to "winter funfare",
            408585121 to "dragonkin ambience",
            -1455241861 to "victory is mine",
            1817249074 to "woe of the wyvern",
            685190118 to "in the brine",
            -1955797228 to "lucid nightmare",
            368271413 to "diango's little helpers",
            364185053 to "roll the bones",
            1533565119 to "mind over matter",
            -1789903512 to "golden touch",
            62757197 to "zombie dragon",
            -960709976 to "dogs of war",
            -1683079062 to "autumn in bridgelum",
            1310667448 to "lucid dream",
            -1055035908 to "oncoming foe",
            -926977577 to "the enchanter",
            -1110089645 to "lament",
            -993528987 to "making waves",
            2032696205 to "cabin fever",
            478781900 to "last stand",
            394756979 to "scape santa",
            -1741764817 to "poles apart",
            -222450192 to "dragonkin temple",
            1048381670 to "myths guild",
            -732051341 to "shayzien crypt",
            1344608745 to "roots_and_flutes",
            1209941997 to "bloodbath",
            -662489856 to "food for thought",
            -1081494434 to "malady",
            -1052794696 to "dance of death",
            -312706353 to "conspiracy",
            564620280 to "vanescula",
            3016106 to "bait",
            -1624762449 to "xarpus combat",
            547534551 to "wrath and ruin",
            -1388440987 to "verzik ambience",
            697382073 to "xarpus ambience",
            1092249049 to "storm brew",
            1683908988 to "maiden combat",
            2054833990 to "maiden ambience",
            -1220055670 to "pestilent bloat combat",
            965242971 to "verzik combat",
            -710515142 to "the mad mole",
            1531147948 to "fairy dragon cutscene",
            1213477442 to "chickened out",
            851641665 to "davy jones locker",
            -1918044851 to "mastermindless",
            -1221427244 to "pestilent bloat ambience",
            869816493 to "nylocas combat",
            1395994167 to "nylocas ambience",
            879650982 to "sotetseg combat",
            1145637049 to "verzik defeated",
            -1725263140 to "chef surprize",
            -2037963792 to "sotetseg ambience",
            2014802978 to "barren_land",
            1512143976 to "everlasting fire",
            744536246 to "null and void",
            1020264019 to "pest control",
            798909577 to "my2arm_love_theme_full",
            899275094 to "weiss_town",
            -1381531001 to "tomb raider",
            -1282174037 to "troll_shuffle",
            1761839009 to "my2arm_boss_battle",
            -2136649922 to "no way out",
            -1224099996 to "way of the wyrm",
            -946130271 to "hespori cave",
            656316714 to "newbie farming",
            2145539647 to "alchemical hydra",
            -778471612 to "hoe down",
            95734525 to "method of madness",
            812298380 to "hespori",
            -599680631 to "fear and loathing",
            -1665005042 to "funny bunnies",
            -1989106719 to "assault and battery",
            -742899599 to "battlefront",
            907740319 to "the depths",
            1396043573 to "stuck in the mire",
            1286329061 to "a farmers grind",
            647234089 to "distillery hilarity",
            -850395529 to "trouble brewing",
            214634021 to "head to head",
            -759203789 to "grow grow grow",
            1345432055 to "pinball wizard",
            783525419 to "beetle juice",
            -2122174648 to "back to life",
            201458339 to "getting down to business",
            1312917381 to "gill bill",
            1250412381 to "spy games",
            -1475251658 to "where eagles lair",
            2136164423 to "homescape",
            280241284 to "waking dream",
            986170990 to "dreamstate",
            896171846 to "forsaken tower",
            673424924 to "the lunar isle",
            941457503 to "way of the enchanter",
            -448773288 to "isle of everywhere",
            104077551 to "molch",
            -528815081 to "burning desire",
            619237947 to "the galleon",
            944208821 to "life's a beach!",
            740392969 to "little cave of horrors",
            1294629755 to "on the wing",
            -693313916 to "warriors guild",
            -953752155 to "ful to the brim",
            -2121935151 to "woodland walk",
            408985298 to "box_of_delights",
            -143163121 to "ham fisted",
            2001821724 to "forthos_dungeon",
            -1094248165 to "sigmunds showdown",
            -2129105081 to "sarachnis_lair",
            237156449 to "no pasaran",
            -43136286 to "the last shanty",
            -159400175 to "iowerths_lament",
            -787164765 to "warped_library",
            441740385 to "night of the vampiyre",
            -1522236626 to "city_guardians",
            919126885 to "the_tower_of_voices",
            -2124107058 to "gauntlet_minigame",
            -1178579224 to "ithell",
            -2084720053 to "the dark fragment",
            -1657478315 to "faith_of_the_hefin",
            1814452217 to "elven seed",
            -1413948561 to "amlodd",
            800455509 to "stand up and be counted",
            -1646255655 to "prif_slayer_dungeon",
            94944838 to "cryws",
            28034174 to "dance_of_the_meilyr",
            586023644 to "scape crystal",
            2001170767 to "zalcano_combat",
            1372894292 to "nightmare_combat",
            -182209093 to "slepe dungeon",
            1826933756 to "the terrible caverns",
            -96104580 to "well hallowed air",
            1949747579 to "arboretum",
            -1839593320 to "darkmeyer",
            1510693574 to "upir likhyi",
            -1870786963 to "hallowed sepulchre",
            300077565 to "the terrible tunnels",
            1615933233 to "domain of the vampyres",
            -1354286452 to "vanstrom",
            -677982497 to "rest_in_peace",
            -791905777 to "the enclave",
            1574724701 to "safety in numbers",
            -2091564975 to "eves epinette",
            658928897 to "sea_minor_shanty",
            -1931522920 to "soul wars",
            192519476 to "the waiting game",
            213399440 to "scrubfoots descent",
            1985000184 to "tempoross",
            1650813279 to "camdozaal_vault",
            3506511 to "rose",
            2042811758 to "confrontation"
        )
    }
}
