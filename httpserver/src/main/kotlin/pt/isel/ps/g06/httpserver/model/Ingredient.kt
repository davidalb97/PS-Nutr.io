package pt.isel.ps.g06.httpserver.model

data class Ingredient(val name: String, val identifier: String, val carbs: Int)

data class MealIngredient(val ingredient: Ingredient, val quantity: Int)