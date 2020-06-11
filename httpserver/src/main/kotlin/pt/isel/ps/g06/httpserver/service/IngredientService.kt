package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository

@Service
class IngredientService(private val mealDbRepository: MealDbRepository) {
    fun getIngredients(skip: Int?, limit: Int?) {
        //TODO When David finishes output model and mapper
        mealDbRepository
                .getIngredients(skip, limit)
                .forEach { println(it.meal_name) }
    }
}