package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType

data class Ingredient(val name: String, val apiId: String, val apiType: FoodApiType)