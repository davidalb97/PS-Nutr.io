package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.model.food.Ingredient
import pt.isel.ps.g06.httpserver.model.food.Meal
import java.util.stream.Stream

data class MealComposition(
        //TODO Add base carbs for given ingredient - ingredient carbs should always be base line
        val meals: Stream<Meal>,
        val ingredients: Stream<Ingredient>
)