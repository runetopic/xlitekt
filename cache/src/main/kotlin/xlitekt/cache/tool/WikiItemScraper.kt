package xlitekt.cache.tool

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
fun main() {
    startKoin {
        loadKoinModules(cacheModule)
    }
    scrapeItems("/w/Category:Items")
}

private val wikiItems = mutableListOf<WikiItem>()

private fun scrapeItems(baseUrl: String) {
    val baseDocument = Jsoup.connect("https://oldschool.runescape.wiki$baseUrl").get()
    val nextPage = baseDocument.getElementById("mw-pages")?.select("a")?.last()?.attr("href")

    generatePageLinks(baseDocument).drop(1).forEach { link ->
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
            wikiItem.weight = generateWeightFromElements(elements)?.toFloatOrNull()
            wikiItem.itemId = generateItemIdFromElements(elements)?.toIntOrNull()
        }
        if (infoboxes.hasClass("infobox infobox-bonuses infobox")) {
            val elements = infoboxes.flatMap { it.getElementsByClass("infobox infobox-bonuses infobox") }.flatMap { it.select("tr") }
            val attackBonuses = generateAttackBonusesFromElement(elements[3])
            wikiItem.attackStab = attackBonuses[0]
            wikiItem.attackSlash = attackBonuses[1]
            wikiItem.attackCrush = attackBonuses[2]
            wikiItem.attackMagic = attackBonuses[3]
            wikiItem.attackRanged = attackBonuses[4]
            val defenceBonuses = generateDefenceBonusesFromElement(elements[8])
            wikiItem.defenceStab = defenceBonuses[0]
            wikiItem.defenceSlash = defenceBonuses[1]
            wikiItem.defenceCrush = defenceBonuses[2]
            wikiItem.defenceMagic = defenceBonuses[3]
            wikiItem.defenceRanged = defenceBonuses[4]
            val otherBonuses = generateOtherBonusesFromElement(elements[13])
            wikiItem.strengthBonus = otherBonuses[0]
            wikiItem.rangedStrength = otherBonuses[1]
            wikiItem.magicDamage = otherBonuses[2]
            wikiItem.prayer = otherBonuses[3]
            wikiItem.equipmentSlot = generateEquipmentSlotFromElement(elements[13])
            wikiItem.attackSpeed = generateSpeedFromElement(elements.getOrNull(17))?.toIntOrNull()
            wikiItem.attackRange = generateAttackRangeFromElement(elements.getOrNull(17))?.toIntOrNull()
        }

        if (wikiItem.valid()) {
            wikiItems.add(wikiItem)
        }
    }
    // If the bot is on the last page.
    if (baseUrl == "/w/Category:Items?pagefrom=Zamorak+full+helm#mw-pages") {
        val json = Json { prettyPrint = true }
        json.encodeToStream(wikiItems, Path.of("./cache/data/dump/wiki/items.json").outputStream())
        exitProcess(0)
    }
    scrapeItems(nextPage!!)
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
data class WikiItem(
    var itemId: Int? = null,
    var name: String? = null,
    var equipable: Boolean = false,
    var option: String? = null,
    var destroyOption: String? = null,
    var examine: String? = null,
    var weight: Float? = null,
    var attackStab: Int? = null,
    var attackSlash: Int? = null,
    var attackCrush: Int? = null,
    var attackMagic: Int? = null,
    var attackRanged: Int? = null,
    var defenceStab: Int? = null,
    var defenceSlash: Int? = null,
    var defenceCrush: Int? = null,
    var defenceMagic: Int? = null,
    var defenceRanged: Int? = null,
    var strengthBonus: Int? = null,
    var rangedStrength: Int? = null,
    var magicDamage: Int? = null,
    var prayer: Int? = null,
    var equipmentSlot: String? = null,
    var attackSpeed: Int? = null,
    var attackRange: Int? = null
) {
    fun valid() = itemId != null && name != null
}
