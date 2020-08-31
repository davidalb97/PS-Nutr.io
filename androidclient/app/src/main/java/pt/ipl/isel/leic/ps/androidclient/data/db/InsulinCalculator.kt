package pt.ipl.isel.leic.ps.androidclient.data.db

import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import java.math.RoundingMode

class InsulinCalculator {

    fun calculateMealInsulin(
        insulinProfile: InsulinProfile,
        currentGlucose: Float,
        mealCarbs: Int
    ): Float {
        //The correction factor based on current glucose, glucose objective and user's ISF
        val correctionFactor: Float =
            (currentGlucose - insulinProfile.glucoseObjective) /
                    insulinProfile.glucoseAmountPerInsulin

        //Insulin based on carb ratio and ingested carbs during this meal
        val carbInsulin: Float = mealCarbs / insulinProfile.carbsAmountPerInsulin

        val total = correctionFactor + carbInsulin

        return total
            .toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
            .toFloat()
    }
}