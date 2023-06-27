package de.joshua.dnd.dices

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import de.joshua.DiscordBot
import de.joshua.dnd.dices.buttons.*
import net.dv8tion.jda.api.interactions.components.ItemComponent
import java.awt.Color
import java.io.StringReader
import java.net.URL
import kotlin.random.Random


class Dice {
    companion object {
        fun rollDice(max: Int = 100): Int {
            return (Math.random() * max + 1).toInt()
        }


        fun getColor(per: Double): Color {
            val percent = per / 100
            val best = Color.decode("#066e32")
            val worst = Color.decode("#ff0000")

            val red = ((1 - percent) * best.red + percent * worst.red).toInt()
            val blue = ((1 - percent) * best.blue + percent * worst.blue).toInt()
            val green = ((1 - percent) * best.green + percent * worst.green).toInt()

            return Color(red, green, blue)
        }

        fun isCrit(number: Int): Boolean {
            val list = number.toDigits()
            if (list.size < 2) return false
            return list.size == 3 || list[0] == list[1]
        }

        fun getCrit(location: Int = rollDice(), intensity: Int = rollDice()): CritEntry {

            var file: URL? = null

            if (location <= 9) {
                file = DiscordBot::class.java.getResource("/crit/head.json")
            } else if (location <= 24) {
                file = DiscordBot::class.java.getResource("/crit/arm.json")
            } else if (location <= 44) {
                file = DiscordBot::class.java.getResource("/crit/arm.json")
            } else if (location <= 79) {
                file = DiscordBot::class.java.getResource("/crit/body.json")
            } else if (location <= 89) {
                file = DiscordBot::class.java.getResource("/crit/leg.json")
            } else if (location <= 100) {
                file = DiscordBot::class.java.getResource("/crit/leg.json")
            }

            if (file == null) return CritEntry(0, 0, "Error", "", "")


            val klaxon = Klaxon()
            val critTableEntries = arrayListOf<CritTableEntry>()
            JsonReader(StringReader(file.readText())).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        /*val person = klaxon.parse<TableEntry>(reader)
                        if (person != null) {
                            result.add(person)
                        }*/
                        klaxon.parse<CritTableEntry>(reader)?.let { critTableEntries.add(it) }
                    }
                }
            }

            val critEntre: CritTableEntry =
                critTableEntries.find { it.max > intensity } ?: critTableEntries.maxByOrNull { it.max }
                ?: CritTableEntry(0, "", "Error", "out of bounds")

            return CritEntry(location, intensity, critEntre.description, critEntre.wounds, critEntre.additionalEffects)
        }

        fun getBodyPart(number: Int): String {
            if (number <= 9) {
                return "Head"
            } else if (number <= 24) {
                return "Left Arm"
            } else if (number <= 44) {
                return "Right Arm"
            } else if (number <= 79) {
                return "Body"
            } else if (number <= 89) {
                return "Left Leg"
            } else if (number <= 100) {
                return "Right Leg"
            }
            return "Error"
        }

        fun getActionRow(): List<ItemComponent> {
            val list = mutableListOf<ItemComponent>()
            list.add(DiceButton().getButton())
            list.add(d6Button().getButton())
            list.add(d10Button().getButton())
            list.add(BodyPartButton().getButton())
            list.add(CritButton().getButton())
            return list
        }

        fun rollDice(input: String): Int {
            val parts = input.split("d", "+", "-")
            val numDice = parts[0].toInt()
            val diceSides = parts[1].toInt()

            var sum = 0
            repeat(numDice) {
                sum += Random.nextInt(1, diceSides)
            }

            if (input.contains("+")) {
                val modifier = parts[2].toInt()
                sum += modifier
            } else if (input.contains("-")) {
                val modifier = parts[2].toInt()
                sum -= modifier
            }

            return sum
        }

        fun rollDiceString(input: String): String {
            val parts = input.split("d", "+", "-")
            val numDice = parts[0].toInt()
            val diceSides = parts[1].toInt()

            val rolls = mutableListOf<Int>()
            repeat(numDice) {
                val roll = Random.nextInt(1, diceSides + 1)
                rolls.add(roll)
            }

            val result = StringBuilder()
            for (i in rolls.indices) {
                result.append(rolls[i])
                if (i != rolls.lastIndex) {
                    result.append(", ")
                }
            }

            if (input.contains("+")) {
                val modifier = parts[2].toInt()
                result.append(" + $modifier")
            } else if (input.contains("-")) {
                val modifier = parts[2].toInt()
                result.append(" - $modifier")
            }

            return result.toString()
        }
    }
}

fun Int.toDigits(base: Int = 10): List<Int> = sequence {
    var n = this@toDigits
    require(n >= 0)
    while (n != 0) {
        yield(n % base)
        n /= base
    }
}.toList()

fun getBodyPartNumber(bodyPart: String): Int {
    when (bodyPart) {
        "head" -> return 0
        "arm" -> return 10
        "body" -> return 45
        "leg" -> return 80
    }
    return 0
}


data class CritTableEntry(
    val max: Int,
    val description: String,
    val wounds: String,
    val additionalEffects: String,
)

data class CritEntry(
    val location: Int,
    val intensity: Int,
    val description: String,
    val wounds: String,
    val additionalEffects: String,
)