package xlitekt.cache.provider.sprite.titlescreen

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class TitleScreenEntryTypeProvider : EntryTypeProvider<TitleScreenEntryType>() {

    override fun load(): Map<Int, TitleScreenEntryType> {
        val index = store.index(SPRITE_INDEX)
        val groups = listOf(
            index.group("logo"),
            index.group("logo_deadman_mode"),
            index.group("logo_seasonal_mode"),
            index.group("titlebox"),
            index.group("titlebutton"),
            index.group("titlebutton_large"),
            index.group("play_now_text"),
            index.group("titlebutton_wide42,1"),
            index.group("runes"),
            index.group("title_mute"),
            index.group("options_radio_buttons,0"),
            index.group("options_radio_buttons,4"),
            index.group("options_radio_buttons,2"),
            index.group("options_radio_buttons,6"),
            index.group("sl_button"),
            index.group("sl_back"),
            index.group("sl_flags"),
            index.group("sl_arrows"),
            index.group("sl_stars"),
            index.group("leftarrow"),
            index.group("rightarrow")
        )
        return groups.map { ByteReadPacket(it.data).loadEntryType(TitleScreenEntryType(it.id)) }.associateBy(TitleScreenEntryType::id)
    }

    override fun ByteReadPacket.loadEntryType(type: TitleScreenEntryType): TitleScreenEntryType {
        discard(remaining)
        assertEmptyAndRelease()
        return type
    }
}
