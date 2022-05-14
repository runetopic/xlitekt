package xlitekt.cache.provider.sprite.titlescreen

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class TitleScreenEntryTypeProvider : EntryTypeProvider<TitleScreenEntryType>() {

    override fun load(): Map<Int, TitleScreenEntryType> {
        val glossary = listOf(
            "logo",
            "logo_deadman_mode",
            "logo_seasonal_mode",
            "titlebox",
            "titlebutton",
            "titlebutton_large",
            "play_now_text",
            "titlebutton_wide42,1",
            "runes",
            "title_mute",
            "options_radio_buttons,0",
            "options_radio_buttons,4",
            "options_radio_buttons,2",
            "options_radio_buttons,6",
            "sl_button",
            "sl_back",
            "sl_flags",
            "sl_arrows",
            "sl_stars",
            "leftarrow",
            "rightarrow"
        )
        val groups = glossary.map(store.index(SPRITE_INDEX)::group)
        return groups.mapIndexed { index, group ->
            ByteReadPacket(group.data).loadEntryType(TitleScreenEntryType(group.id, name = glossary[index]))
        }.associateBy(TitleScreenEntryType::id)
    }

    override fun ByteReadPacket.loadEntryType(type: TitleScreenEntryType): TitleScreenEntryType {
        discard(remaining)
        assertEmptyAndRelease()
        return type
    }
}
