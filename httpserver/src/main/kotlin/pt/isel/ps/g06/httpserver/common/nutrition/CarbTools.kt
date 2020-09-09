package pt.isel.ps.g06.httpserver.common.nutrition

/**
 * Performs a "regra 3 simples" to calculate carbs for any given meal/ingredient
 *
 * originalQuantity -- originalCarbs
 * newQuantity      --   RETURN
 */
fun calculateCarbsFromBase(originalQuantity: Float, originalCarbs: Float, newQuantity: Float): Float {
    return (newQuantity.times(originalCarbs)).div(originalQuantity)
}