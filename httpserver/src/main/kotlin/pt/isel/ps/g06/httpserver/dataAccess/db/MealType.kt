package pt.isel.ps.g06.httpserver.dataAccess.db

const val MEAL_TYPE_SUGGESTED_MEAL = "MS"
const val MEAL_TYPE_SUGGESTED_INGREDIENT = "I"
const val MEAL_TYPE_CUSTOM = "MC"

enum class MealType(private val type: String) {
    SUGGESTED_MEAL(MEAL_TYPE_SUGGESTED_MEAL),
    SUGGESTED_INGREDIENT(MEAL_TYPE_SUGGESTED_INGREDIENT),
    CUSTOM(MEAL_TYPE_CUSTOM);

    override fun toString(): String = this.type

    companion object {
        /**
         * Gets the enum value from it's type.
         * @param str The type name.
         * @throws [NoSuchElementException] If no such element is found.
         */
        fun fromValue(str: String) = values().first { it.type == str }
    }
}
