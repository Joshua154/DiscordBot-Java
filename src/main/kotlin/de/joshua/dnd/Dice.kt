package de.joshua.dnd

import java.awt.Color

class Dice {
    companion object {
        fun rollDice(max: Int): Int {
            return (Math.random() * max).toInt()
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

class TableEntry(
    val description: String,
    val wounds: Int,
    val additionalEffects: String
)
