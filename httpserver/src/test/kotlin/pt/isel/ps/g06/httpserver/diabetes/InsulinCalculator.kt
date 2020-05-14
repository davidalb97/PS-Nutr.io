package pt.isel.ps.g06.httpserver.diabetes

import org.junit.jupiter.api.Test
import java.math.RoundingMode

/**
 * Calculates **suggested** insulin that should be administered as a result of current meal,
 * given an user's current [InsulinProfile] and ingested carbs for current meal.
 *
 * @param glucose user's glucose for this current meal.
 * @param carbs ingested carbs for current meal.
 *
 * @return the **suggested** insulin result that should be administered, rounded up
 * to 2 decimal values precision (e.g: 0.00units).
 *
 * This rounding is made for the sake of user readability, but also because insulin pumps only
 * allow 2 decimal values input.
 */
fun calculateMealInsulin(insulinProfile: InsulinProfile, glucose: Int, carbs: Int): Float {
    //The correction factor based on current glucose, glucose objective and user's ISF
    val correctionFactor: Float = (glucose - insulinProfile.glucoseObjective) / insulinProfile.insulinForGlucose.toFloat()

    //Insulin based on carb ratio and ingested carbs during this meal
    val carbInsulin: Float = (carbs / insulinProfile.carbAmount) * insulinProfile.insulinForCarbs
    val total = correctionFactor + carbInsulin

    val rounded = total
            .toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
            .toFloat()

    println("Correction factor: $correctionFactor")
    println("Carb insulin: $carbInsulin")
    println("Added up: $total")
    println("Rounded to 2 values, half up: $rounded")

    return rounded
}

class InsulinCalculator {
    @Test
    fun test() {
        val profile = InsulinProfile(100, 40, 1F, 12)
        calculateMealInsulin(profile, 200, 24)
    }
}
