package xlitekt.cache.provider.sprite.titlescreen

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import java.nio.ByteBuffer

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
            ByteBuffer.wrap(group.data).loadEntryType(TitleScreenEntryType(group.id, name = glossary[index]))
        }.associateBy(TitleScreenEntryType::id)
    }

    override fun ByteBuffer.loadEntryType(type: TitleScreenEntryType): TitleScreenEntryType {
        discard(remaining())
        assertEmptyAndRelease()
        return type
    }
}
