package pt.ipl.isel.leic.ps.androidclient.ui.util.units

import android.text.SpannableStringBuilder
import java.math.RoundingMode

private const val CONVERSION_CONST = 18

enum class GlucoseUnits(val string: String) {
    MILLI_GRAM_PER_DL("mg / dL"),
    MILLI_MOL_PER_L("mmol / L");

    fun convert(targetUnit: GlucoseUnits, value: Double): Double {
        if (this == targetUnit) {
            return value
        }
        val builder = when (targetUnit) {
            MILLI_GRAM_PER_DL -> value / CONVERSION_CONST
            MILLI_MOL_PER_L -> value * CONVERSION_CONST
        }
        return SpannableStringBuilder(
            builder
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_UP)
                .toFloat()
                .toString()
        ).toString().toDouble()
    }

    override fun toString(): String = string
}