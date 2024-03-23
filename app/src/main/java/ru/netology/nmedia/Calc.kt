package ru.netology.nmedia

import android.icu.text.DecimalFormat

object Calc {
    fun converter(value: Int): String {
        val convertedValue = when(value) {
            in 0 .. 999 -> "$value"
            in 1000 .. 1099 -> "${(value/1000)}K"
            in 1100 .. 9999 -> "${String.format("%.1f", value.toDouble()/1000)}K"
            in 10000 .. 999999 -> "${(value/1000)}K"
            else -> if (value > 0) "${String.format("%.1f", value.toDouble()/1_000_000)}М" else "Negative"
        }
        return convertedValue
    }
}