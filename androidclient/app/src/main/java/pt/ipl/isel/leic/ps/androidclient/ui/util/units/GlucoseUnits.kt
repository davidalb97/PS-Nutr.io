package pt.ipl.isel.leic.ps.androidclient.ui.util.units

import android.text.SpannableStringBuilder
import java.math.RoundingMode

private const val CONVERTION_CONST = 18

enum class GlucoseUnits(val str: String) {
    MILLIGRAM_PER_DL("mg / dL"),
    MILLIMOL_PER_L("mmol / L");

    fun convert(targetUnit: GlucoseUnits, value: Double): Double {
        if (this == targetUnit) {
            return value
        }
        val builder = when (targetUnit) {
            MILLIGRAM_PER_DL -> value / CONVERTION_CONST
            MILLIMOL_PER_L -> value * CONVERTION_CONST
        }
        return SpannableStringBuilder(
            builder
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_UP)
                .toFloat()
                .toString()
        ).toString().toDouble()
    }

    override fun toString(): String = str
}