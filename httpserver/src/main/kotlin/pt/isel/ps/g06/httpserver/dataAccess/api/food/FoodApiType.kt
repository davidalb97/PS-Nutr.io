package pt.isel.ps.g06.httpserver.dataAccess.api.food

enum class FoodApiType {
    Spoonacular;

    companion object {
        fun getOrDefault(type: String?, default: FoodApiType = Spoonacular): FoodApiType {
            return try {
                return if (type == null) default
                else valueOf(type)
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}