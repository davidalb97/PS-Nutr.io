package pt.ipl.isel.leic.ps.androidclient.ui.util.units

import android.text.SpannableStringBuilder
import java.math.RoundingMode

private const val GRAM_OUNCE_RATIO = 0.035274


enum class WeightUnits(private val str: String) {
    GRAMS("grams"),
    OUNCES("ounces");

    fun convert(targetUnit: WeightUnits, value: Float): Float {
        if (this == targetUnit) {
            return value
        }
        val builder = when (targetUnit) {
            GRAMS -> value / GRAM_OUNCE_RATIO
            OUNCES -> value * GRAM_OUNCE_RATIO
        }
        return SpannableStringBuilder(
            builder
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_UP)
                .toFloat()
                .toString()
        ).toString().toFloat()
    }

    override fun toString(): String = str

    companion object {
        @Override
        fun fromValue(description: String): WeightUnits {
            return values().first { it.toString() == description }
        }
    }
}