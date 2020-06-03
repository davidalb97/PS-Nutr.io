package pt.ipl.isel.leic.ps.androidclient.data.db

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile
import java.math.RoundingMode

class InsulinCalculator {

    fun calculateMealInsulin(
        dbInsulinProfile: DbInsulinProfile,
        currentGlucose: Int,
        mealCarbs: Int
    ): Float {
        //The correction factor based on current glucose, glucose objective and user's ISF
        val correctionFactor: Float =
            (currentGlucose - dbInsulinProfile.glucoseObjective) /
                    dbInsulinProfile.glucoseAmountPerInsulin.toFloat()

        //Insulin based on carb ratio and ingested carbs during this meal
        val carbInsulin: Float =
            mealCarbs / dbInsulinProfile.carbsAmountPerInsulin.toFloat()

        val total = correctionFactor + carbInsulin

        return total
            .toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
            .toFloat()
    }
}