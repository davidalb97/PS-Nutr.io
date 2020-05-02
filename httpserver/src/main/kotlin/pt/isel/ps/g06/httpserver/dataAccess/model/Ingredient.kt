package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.dataAccess.api.food.model.FoodApiType

data class Ingredient(val name: String, val apiId: Int, val apiType: FoodApiType)