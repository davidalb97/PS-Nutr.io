package pt.isel.ps.g06.httpserver.dataAccess.api.food


enum class FoodApiType {
    Spoonacular;

    companion object {
        fun getOrDefault(type: String?, default: FoodApiType = Spoonacular): FoodApiType {
            return values().find { it.toString().equals(type, true) } ?: default
        }

        fun getOrNull(type: String?): FoodApiType? {
            return values().find { it.toString().equals(type, true) }
        }
    }
}