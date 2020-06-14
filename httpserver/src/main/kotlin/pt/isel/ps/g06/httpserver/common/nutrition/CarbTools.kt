package pt.isel.ps.g06.httpserver.common.nutrition

/**
 * Performs a "regra 3 simples" to calculate carbs for any given meal/ingredient
 *
 * originalQuantity -- originalCarbs
 * newQuantity      --   RETURN
 */
fun calculateCarbsFromBase(originalQuantity: Int, originalCarbs: Int, newQuantity: Int): Float {
    return (newQuantity.times(originalCarbs).toFloat()).div(originalQuantity)
}