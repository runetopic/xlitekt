package script.skill.woodcutting

enum class AxeType(val requiredLevel: Int, val sequenceId: Int, vararg val id: Int) {
    Bronze(1, 879, 1351),
    Iron(1, 877, 1349),
    Steel(6, 875, 1353),
    Black(11, 873, 1361),
    Mithril(21, 871, 1355),
    Adamant(31, 869, 1357),
    Blessed(35, 10491),
    Rune(41, 1359),
    Gilded(41, 8303, 23279),
    Dragon(61, 2846, 6739),
    Infernal(61, 2117, 13241, 13242),
    ThirdAge(61, 7264, 20011),
    Crystal(71, 8324, 23673, 23675)
}

fun AxeType.getLowAndHigh(treeType: TreeType): Pair<Int, Int> {
    return when (this) {
        AxeType.Bronze -> treeType.getBronzeChance()
        AxeType.Iron -> treeType.getIronChance()
        AxeType.Steel -> treeType.getSteelChance()
        AxeType.Black -> treeType.getBlackChance()
        AxeType.Mithril -> treeType.getMithrilChance()
        AxeType.Adamant -> treeType.getAdamantChance()
        AxeType.Rune, AxeType.Gilded -> treeType.getRuneChance()
        AxeType.Dragon -> treeType.getDragonChance()
        AxeType.Crystal -> treeType.getCrystalChance()
        else -> 0 to 0
    }
}

// Normal Tree / jungle tree?
//    {{Skilling success chart|label=Normal tree cut chance|xlabel=Woodcutting level
//    |label1=Bronze|low1=64|high1=200|req1=1|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=96|high2=300|req2=1|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=128|high3=400|req3=6|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=144|high4=450|req4=11|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=160|high5=500|req5=21|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=192|high6=600|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=224|high7=700|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=240|high8=750|req8=61|color8=firebrick|image8=Dragon axe.png
//    |label9=Crystal|low9=250|high9=800|req=71|color9=cyan|image9=Crystal axe.png

// Dying tree

// ===Cut chance===
//    {{Skilling success chart|label=Dying tree cut chance
//    |label1=Bronze|low1=64|high1=200|req1=1|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=96|high2=300|req2=1|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=128|high3=400|req3=6|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=144|high4=450|req4=11|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=160|high5=500|req5=21|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=192|high6=600|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=224|high7=700|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=240|high8=750|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Dead tree

// ===Cut chance===
//    {{Skilling success chart|label=Dead tree cut chance
//    |label1=Bronze|low1=64|high1=200|req1=1|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=96|high2=300|req2=1|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=128|high3=400|req3=6|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=144|high4=450|req4=11|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=160|high5=500|req5=21|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=192|high6=600|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=224|high7=700|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=240|high8=750|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Burnt Tree seeems to be the same as dead tree
// ==Cut chance==
// {{Incomplete|May need confirmation of cut chances}}
// {{Skilling success chart|label=Dead tree cut chance
//    |label1=Bronze|low1=64|high1=200|req1=1|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=96|high2=300|req2=1|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=128|high3=400|req3=6|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=144|high4=450|req4=11|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=160|high5=500|req5=21|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=192|high6=600|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=224|high7=700|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=240|high8=750|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Evergreen
// ===Cut chance===
// {{Skilling success chart|label=Evergreen tree cut chance
//    |label1=Bronze|low1=64|high1=200|req1=1|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=96|high2=300|req2=1|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=128|high3=400|req3=6|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=144|high4=450|req4=11|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=160|high5=500|req5=21|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=192|high6=600|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=224|high7=700|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=240|high8=750|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Achey Tree
// ===Cut chance===
// {{Skilling success chart|label=Achey tree cut chance
//    |label1=Bronze|low1=64|high1=200|req1=1|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=96|high2=300|req2=1|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=128|high3=400|req3=6|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=144|high4=450|req4=11|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=160|high5=500|req5=21|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=192|high6=600|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=224|high7=700|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=240|high8=750|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Light jungle

// ===Cut chance===
// {{Skilling success chart|label=Light Jungle Cut Chance|showbefore=no
//    |label1=Plain Machete|low1=49|high1=169|req1=10|color1=dimgray|image1=Machete.png
//    |label2=Opal Machete|low2=54|high2=174|req2=10|color2=lightgray|image2=Opal machete.png
//    |label3=Jade Machete|low3=70|high3=190|req3=10|color3=lightgreen|image3=Jade machete.png
//    |label4=Red Topaz Machete|low4=85|high4=205|req4=10|color4=firebrick|image4=Red topaz machete.png
// }}

// Oak

// ===Cut chance===
// {{Skilling success chart|label=Oak tree cut chance|xlabel=Woodcutting level
//    |label1=Bronze|low1=32|high1=100|req1=15|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=48|high2=150|req2=15|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=64|high3=200|req3=15|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=72|high4=225|req4=15|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=80|high5=250|req5=21|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=96|high6=300|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=112|high7=350|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=120|high8=375|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Medium jungle
// ===Cut chance===
// {{Skilling success chart|label=Medium Jungle Cut Chance|showbefore=no
//    |label1=Plain Machete|low1=34|high1=85|req1=20|color1=dimgray|image1=Machete.png
//    |label2=Opal Machete|low2=39|high2=90|req2=20|color2=lightgray|image2=Opal machete.png
//    |label3=Jade Machete|low3=54|high3=105|req3=20|color3=lightgreen|image3=Jade machete.png
//    |label4=Red Topaz Machete|low4=70|high4=121|req4=20|color4=firebrick|image4=Red topaz machete.png
// }}

// Willow
// ===Cut chance===
// {{Skilling success chart|label=Willow tree cut chance|xlabel=Woodcutting level|showbefore=no
//    |label1=Bronze|low1=16|high1=50|req1=30|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=24|high2=75|req2=30|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=32|high3=100|req3=30|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=36|high4=112|req4=30|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=40|high5=125|req5=30|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=48|high6=150|req6=31|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=56|high7=175|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=60|high8=187|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Teak
// ===Cut chance===
// {{Skilling success chart|label=Teak tree cut chance|xlabel=Woodcutting level|showbefore=no
//    |label1=Bronze|low1=15|high1=46|req1=35|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=23|high2=70|req2=35|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=31|high3=93|req3=35|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=35|high4=102|req4=35|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=39|high5=117|req5=35|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=47|high6=140|req6=35|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=55|high7=164|req7=41|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=60|high8=190|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Dense jungle
// ===Cut chance===
// {{Skilling success chart|label=Dense Jungle Cut Chance|showbefore=no
//    |label1=Plain Machete|low1=16|high1=44|req1=35|color1=dimgray|image1=Machete.png
//    |label2=Opal Machete|low2=21|high2=49|req2=35|color2=lightgray|image2=Opal machete.png
//    |label3=Jade Machete|low3=36|high3=64|req3=35|color3=lightgreen|image3=Jade machete.png
//    |label4=Red Topaz Machete|low4=52|high4=80|req4=35|color4=firebrick|image4=Red topaz machete.png
// }}

// Mature juniper tree
// Missing?

// Maple
// ===Cut chance===
// {{Skilling success chart|label=Maple tree cut chance|showbefore=no
//    |label1=Bronze|low1=8|high1=25|req1=45|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=12|high2=37|req2=45|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=16|high3=50|req3=45|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=18|high4=56|req4=45|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=20|high5=62|req5=45|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=24|high6=75|req6=45|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=28|high7=87|req7=45|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=30|high8=93|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Hollow

// ===Cut chance===
// {{Skilling success chart|label=Hollow tree cut chance|showbefore=no
//    |label1=Bronze|low1=18|high1=26|req1=45|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=28|high2=40|req2=45|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=36|high3=54|req3=45|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=42|high4=57|req4=45|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=46|high5=68|req5=45|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=59|high6=81|req6=45|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=64|high7=94|req7=45|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=67|high8=101|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Mahogany
// ===Cut chance===
// {{Skilling success chart|label=Mahogany tree cut chance|showbefore=no
//    |label1=Bronze|low1=8|high1=25|req1=50|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=12|high2=38|req2=50|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=16|high3=50|req3=50|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=18|high4=54|req4=50|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=20|high5=63|req5=50|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=25|high6=75|req6=50|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=29|high7=88|req7=50|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=34|high8=94|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Artic pine

// ===Cut chance===
// {{Skilling success chart|label=Arctic Pine tree cut chance|showbefore=no
//    |label1=Bronze|low1=6|high1=30|req1=54|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=8|high2=44|req2=54|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=11|high3=60|req3=54|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=13|high4=67|req4=54|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=14|high5=74|req5=54|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=17|high6=90|req6=54|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=20|high7=104|req7=54|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=21|high8=112|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Yew
//
// ===Cut chance===
// {{Skilling success chart|label=Yew tree cut chance|showbefore=no
//    |label1=Bronze|low1=4|high1=12|req1=60|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=6|high2=19|req2=60|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=8|high3=25|req3=60|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=9|high4=28|req4=60|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=10|high5=31|req5=60|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=12|high6=37|req6=60|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=14|high7=44|req7=60|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=15|high8=47|req8=61|color8=firebrick|image8=Dragon axe.png
// }}

// Blisterwood Tree
// Missing?
// Sulliuscep
// Missing?

// Magic
// ===Cut chance===
// {{Skilling success chart|label=Magic tree cut chance|showbefore=no
// |label1=Bronze|low1=2|high1=6|req1=75|color1=saddlebrown|image1=Bronze axe.png
// |label2=Iron|low2=3|high2=9|req2=75|color2=dimgray|image2=Iron axe.png
// |label3=Steel|low3=4|high3=12|req3=75|color3=lightgray|image3=Steel axe.png
// |label4=Black|low4=5|high4=13|req4=75|color4=black|image4=Black axe.png
// |label5=Mithril|low5=5|high5=15|req5=75|color5=slateblue|image5=Mithril axe.png
// |label6=Adamant|low6=6|high6=18|req6=75|color6=seagreen|image6=Adamant axe.png
// |label7=Rune|low7=7|high7=21|req7=75|color7=steelblue|image7=Rune axe.png
// |label8=Dragon|low8=7|high8=22|req8=75|color8=firebrick|image8=Dragon axe.png
// }}

// Redwood

// ===Cut chance===
// {{^|Note: These are based on an Ash tweet where he gave whole number percentages, so they may be slightly off. May be worth revisiting at some point.}}
// {{Skilling success chart|label=Redwood tree cut chance|showbefore=no
//    |label1=Bronze|low1=2|high1=6|req1=90|color1=saddlebrown|image1=Bronze axe.png
//    |label2=Iron|low2=3|high2=9|req2=90|color2=dimgray|image2=Iron axe.png
//    |label3=Steel|low3=4|high3=12|req3=90|color3=lightgray|image3=Steel axe.png
//    |label4=Black|low4=4|high4=14|req4=90|color4=black|image4=Black axe.png
//    |label5=Mithril|low5=5|high5=15|req5=90|color5=slateblue|image5=Mithril axe.png
//    |label6=Adamant|low6=6|high6=18|req6=90|color6=seagreen|image6=Adamant axe.png
//    |label7=Rune|low7=7|high7=21|req7=90|color7=steelblue|image7=Rune axe.png
//    |label8=Dragon|low8=7|high8=30|req8=90|color8=firebrick|image8=Dragon axe.png
//    |label9=Crystal|low9=8|high9=35|req9=90|color9=cyan|image9=Crystal axe.png
// }}
