package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.player.friends.Friend
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.item.Item
import com.runetopic.xlitekt.game.map.Viewport
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.packet.CamResetPacket
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.IfSetColorPacket
import com.runetopic.xlitekt.network.packet.IfSetTextPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.MidiSongPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.network.packet.SetMapFlagPacket
import com.runetopic.xlitekt.network.packet.SetPlayerOpPacket
import com.runetopic.xlitekt.network.packet.SoundEffectPacket
import com.runetopic.xlitekt.network.packet.UpdateContainerFullPacket
import com.runetopic.xlitekt.network.packet.UpdateContainerPartialPacket
import com.runetopic.xlitekt.network.packet.UpdateFriendListPacket
import com.runetopic.xlitekt.network.packet.UpdateRebootTimerPacket
import com.runetopic.xlitekt.network.packet.UpdateStatPacket
import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class Player(
    val client: Client,
    var displayName: String
) : Actor(Tile(3222, 3222)) {
    var rights = 2
    var viewport = Viewport(this)

    suspend fun login() {
        this.previousTile = this.tile
        client.writePacket(RebuildNormalPacket(viewport, tile, true))
        client.writePacket(IfOpenTopPacket(161))
        renderer.appearance(Render.Appearance.Gender.MALE, -1, -1, false)
        client.writePacket(MessageGamePacket(0, "Testing messages"))
        client.writePacket(SetMapFlagPacket(255, 255))
        client.writePacket(UpdateStatPacket(200.0, 2, 2))
        client.writePacket(MidiSongPacket(0))
        client.writePacket(IfSetColorPacket(22, 99))
        client.writePacket(UpdateContainerFullPacket(149 shl 16 or 65536, 93, listOf(Item(4151, 1), Item(995, Int.MAX_VALUE))))
        client.writePacket(SetPlayerOpPacket(false, "Trade", 1))
        client.writePacket(IfSetTextPacket(22, "Hello"))
        client.writePacket(CamResetPacket())

        client.writePacket(IfOpenSubPacket(593, 161 shl 16 or 75, true)) // Combat Options
        client.writePacket(IfOpenSubPacket(320, 161 shl 16 or 76, true)) // Skills
        client.writePacket(IfOpenSubPacket(629, 161 shl 16 or 77, true)) // Quests/Etc List
        client.writePacket(IfOpenSubPacket(149, 161 shl 16 or 78, true)) // Inventory
        client.writePacket(IfOpenSubPacket(387, 161 shl 16 or 79, true)) // Worn Equipment
        client.writePacket(IfOpenSubPacket(541, 161 shl 16 or 80, true)) // Prayer
        client.writePacket(IfOpenSubPacket(218, 161 shl 16 or 81, true)) // Magic
        client.writePacket(IfOpenSubPacket(7, 161 shl 16 or 82, true)) // Chat-channel
        client.writePacket(IfOpenSubPacket(429, 161 shl 16 or 84, true)) // Friends
        client.writePacket(IfOpenSubPacket(671, 161 shl 16 or 83, true)) // Account Management
        client.writePacket(IfOpenSubPacket(182, 161 shl 16 or 85, true)) // Logout
        client.writePacket(IfOpenSubPacket(116, 161 shl 16 or 86, true)) // Options
        client.writePacket(IfOpenSubPacket(216, 161 shl 16 or 87, true)) // Emotes
        client.writePacket(IfOpenSubPacket(239, 161 shl 16 or 88, true)) // Music Player

        client.writePacket(UpdateFriendListPacket(listOf(Friend("_jordan", true))))

        client.writePacket(SoundEffectPacket(20, 2, 0))
        client.writePacket(VarpLargePacket(10, Byte.MAX_VALUE + 1))
        client.writePacket(VarpSmallPacket(10, 1))
        client.writePacket(UpdateRebootTimerPacket(10_000))
        client.writePacket(UpdateContainerPartialPacket(149 shl 16 or 65536, 93, listOf(Item(4151, 1), Item(995, 1)), listOf(1)))
        // TODO Just for now loop it here.
        val service = Executors.newScheduledThreadPool(1)
        service.scheduleAtFixedRate({
            runBlocking {
                client.writePacket(PlayerInfoPacket(this@Player))
                renderer.clearUpdates()
            }
        }, 0, 600, TimeUnit.MILLISECONDS)
    }
}
