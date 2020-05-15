package pt.isel.ps.g06.httpserver.diabetes

/**
 * Displays a user's medical Insulin profile, which is used when calculating
 * the final/total insulin that is **suggested** to be administered for any given meal.
 *
 * @param glucoseObjective the glucose objective. Usually 100 in Portugal
 * @param insulinForGlucose how much glucose a unit of insulin reduces to the objective.
 * @param insulinForCarbs how much insulin units are administered for X carbAmount
 * @param carbAmount how many carbs are lowered by Y insulin
 */
data class InsulinProfile(
        //Insulin Sensibility Factor fields, or "Correction factor"
        val glucoseObjective: Int,
        val insulinForGlucose: Int,
        //Carb ratio fields
        val insulinForCarbs: Float,
        val carbAmount: Int
)