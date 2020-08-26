package pt.isel.ps.g06.httpserver.dataAccess.db

const val MEAL_TYPE_SUGGESTED = "Suggested"
const val MEAL_TYPE_CUSTOM = "Custom"

enum class MealType(private val type: String) {
    SUGGESTED(MEAL_TYPE_SUGGESTED),
    CUSTOM(MEAL_TYPE_CUSTOM);

    override fun toString(): String = this.type
}