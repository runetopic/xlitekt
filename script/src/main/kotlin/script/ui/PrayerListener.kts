package script.ui

import xlitekt.cache.provider.config.enum.EnumEntryTypeProvider
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.shared.inject
import xlitekt.shared.packInterface
import xlitekt.shared.resource.prayer.Prayers

/**
 * @author Justin Kenney
 */
val enumProvider by inject<EnumEntryTypeProvider>()
val interfaceKeyEnum = 859
val prayerNameEnum = 860 // normal spellbook
val cachedPackedInterfaces = enumProvider.entryType(interfaceKeyEnum)?.params?.map { it.value to it.key }?.toMap()
val cachedPrayerNames = enumProvider.entryType(prayerNameEnum)?.params?.map { it.key to it.value.toString().lowercase().replace(" ", "_") }?.toMap()

onInterface<UserInterface.Prayer> {
    onOpen {
//        setEvent(interfaceId = 541, childId = 1, slots = 5..33, event = InterfaceEvent.CLICK_OPTION_1)
    }

    onClick { event ->

        val packedInterface = event.interfaceId.packInterface(event.childId)

        val foundKey = cachedPackedInterfaces?.get(packedInterface)
        val prayerName = cachedPrayerNames?.get(foundKey)
        val prayerInfo = Prayers.info(prayerName.toString()) ?: return@onClick

        this.prayer.switch(prayerInfo)
    }
}
