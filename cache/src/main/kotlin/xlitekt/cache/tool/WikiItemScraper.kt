package xlitekt.cache.tool

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import xlitekt.cache.cacheModule
import java.nio.file.Path
import kotlin.io.path.outputStream
import kotlin.system.exitProcess

/**
 * @author Jordan Abraham
 */
suspend fun main() {
    startKoin {
        loadKoinModules(cacheModule)
    }
    scrapeItems("/w/Category:Items")
}

private val wikiItems = mutableListOf<WikiItem>()

private suspend fun scrapeItems(baseUrl: String) {
    val baseDocument = Jsoup.connect("https://oldschool.runescape.wiki$baseUrl").get()
    val nextPage = baseDocument.getElementById("mw-pages")?.select("a")?.last()?.attr("href")

    generatePageLinks(baseDocument).drop(1).parallelStream().forEach { link ->
        val itemPage = Jsoup.connect("https://oldschool.runescape.wiki$link")
        println("https://oldschool.runescape.wiki$link")
        val document = itemPage.get()
        val wikiItem = WikiItem()
        val infoboxes = document.getElementsByClass("infobox")
        if (infoboxes.hasClass("infobox no-parenthesis-style infobox-item")) {
            val elements = infoboxes.flatMap { it.getElementsByClass("infobox no-parenthesis-style infobox-item") }.flatMap { it.select("tr") }
            wikiItem.name = generateNameFromElements(elements)
            wikiItem.equipable = generateEquipableFromElements(elements)
            wikiItem.destroyOption = generateDestroyOptionFromElements(elements)
            wikiItem.examine = generateExamineFromElements(elements)
            wikiItem.weight = generateWeightFromElements(elements)?.toFloatOrNull() ?: 0F
            wikiItem.itemId = generateItemIdFromElements(elements)?.toIntOrNull()
        }
        if (infoboxes.hasClass("infobox infobox-bonuses infobox")) {
            val elements = infoboxes.flatMap { it.getElementsByClass("infobox infobox-bonuses infobox") }.flatMap { it.select("tr") }
            val attackBonuses = generateAttackBonusesFromElement(elements[3])
            val defenceBonuses = generateDefenceBonusesFromElement(elements[8])
            val otherBonuses = generateOtherBonusesFromElement(elements[13])
            val equipment = ItemEquipment(
                attackStab = attackBonuses[0] ?: 0,
                attackSlash = attackBonuses[1] ?: 0,
                attackCrush = attackBonuses[2] ?: 0,
                attackMagic = attackBonuses[3] ?: 0,
                attackRanged = attackBonuses[4] ?: 0,
                defenceStab = defenceBonuses[0] ?: 0,
                defenceSlash = defenceBonuses[1] ?: 0,
                defenceCrush = defenceBonuses[2] ?: 0,
                defenceMagic = defenceBonuses[3] ?: 0,
                defenceRanged = defenceBonuses[4] ?: 0,
                strengthBonus = otherBonuses[0] ?: 0,
                rangedStrength = otherBonuses[1] ?: 0,
                magicDamage = otherBonuses[2] ?: 0,
                prayer = otherBonuses[3] ?: 0,
                equipmentSlot = generateEquipmentSlotFromElement(elements[13]),
                attackSpeed = generateSpeedFromElement(elements.getOrNull(17))?.toIntOrNull() ?: 0,
                attackRange = generateAttackRangeFromElement(elements.getOrNull(17))?.toIntOrNull() ?: 0
            )
            wikiItem.equipment = equipment
        }

        if (wikiItem.valid()) {
            wikiItems.add(wikiItem)
        }
    }
    // If the bot is on the last page.
    if (baseUrl == "/w/Category:Items?pagefrom=Zamorak+full+helm#mw-pages") {
        val json = Json { prettyPrint = true }
        json.encodeToStream(wikiItems.sortBy { it.itemId }, Path.of("./cache/data/dump/wiki/items.json").outputStream())
        exitProcess(0)
    }
    scrapeItems(nextPage!!)
    delay(1)
}

private fun generatePageLinks(document: Element): List<String> {
    val links = mutableListOf<String>()
    document.getElementById("mw-pages")?.getElementsByClass("mw-category-group")?.forEach {
        it.getElementsByAttribute("href").forEach { link ->
            links.add(link.attr("href"))
        }
    }
    return links
}

private fun generateNameFromElements(elements: List<Element>) = elements
    .flatMap { it.getElementsByClass("infobox-header") }
    .first()
    ?.text()

private fun generateEquipableFromElements(elements: List<Element>) = elements
    .firstOrNull { it.select("th").text() == "Equipable" }
    ?.text()
    ?.drop("Equipable ".length) == "Yes"

private fun generateDestroyOptionFromElements(elements: List<Element>) = elements
    .firstOrNull { it.select("th").text() == "Destroy" }
    ?.text()
    ?.drop("Destroy ".length)

private fun generateExamineFromElements(elements: List<Element>) = elements
    .firstOrNull { it.select("th").text() == "Examine" }
    ?.text()
    ?.drop("Examine ".length)

private fun generateWeightFromElements(elements: List<Element>) = elements
    .firstOrNull { it.select("th").text() == "Weight" }
    ?.text()
    ?.drop("Weight ".length)
    ?.dropLast(" kg".length)

private fun generateItemIdFromElements(elements: List<Element>) = elements
    .firstOrNull { it.select("th").text() == "Item ID" }
    ?.text()
    ?.drop("Item ID ".length)
    ?.filter(Char::isDigit)

private fun generateAttackBonusesFromElement(element: Element?) = buildList {
    val select = element?.select("td")
    add(select?.get(0)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // stab
    add(select?.get(1)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // slash
    add(select?.get(2)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // crush
    add(select?.get(3)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // magic
    add(select?.get(4)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // ranged
}

private fun generateDefenceBonusesFromElement(element: Element?) = buildList {
    val select = element?.select("td")
    add(select?.get(0)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // stab
    add(select?.get(1)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // slash
    add(select?.get(2)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // crush
    add(select?.get(3)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // magic
    add(select?.get(4)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // ranged
}

private fun generateOtherBonusesFromElement(element: Element?) = buildList {
    val select = element?.select("td")
    add(select?.get(0)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // strengthBonus
    add(select?.get(1)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // rangedStrength
    add(select?.get(2)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // magicDamage
    add(select?.get(3)?.text()?.let { if (it.first() == '+') it.filter(Char::isDigit).toIntOrNull() else it.filter(Char::isDigit).toIntOrNull()?.inv()?.plus(1) }) // prayer
}

private fun generateEquipmentSlotFromElement(element: Element?) = element
    ?.select("a")
    ?.attr("title")
    ?.dropLast(" slot".length)
    ?.lowercase()

private fun generateSpeedFromElement(element: Element?) = element
    ?.select("td")
    ?.first()
    ?.selectFirst("img")
    ?.attr("alt")
    ?.takeLast(5)
    ?.dropLast(4)

private fun generateAttackRangeFromElement(element: Element?) = element
    ?.select("td")
    ?.last()
    ?.text()

@Serializable
data class ItemEquipment(
    var attackStab: Int,
    var attackSlash: Int,
    var attackCrush: Int,
    var attackMagic: Int,
    var attackRanged: Int,
    var defenceStab: Int,
    var defenceSlash: Int,
    var defenceCrush: Int,
    var defenceMagic: Int,
    var defenceRanged: Int,
    var strengthBonus: Int,
    var rangedStrength: Int,
    var magicDamage: Int,
    var prayer: Int,
    var equipmentSlot: String? = null,
    var attackSpeed: Int,
    var attackRange: Int
)

@Serializable
data class WikiItem(
    var itemId: Int? = null,
    var name: String? = null,
    var equipable: Boolean = false,
    var option: String? = null,
    var destroyOption: String? = null,
    var examine: String? = null,
    var weight: Float = 0F,
    var equipment: ItemEquipment? = null
) {
    fun valid() = itemId != null && name != null
}
