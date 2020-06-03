package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal

class CustomMealMapper {

    val ingredientMapper = IngredientMapper()

    fun mapToModel(dto: DbCustomMeal): CustomMeal =
        CustomMeal(
            dto.meal.mealName,
            dto.meal.mealQuantity,
            dto.meal.glucoseAmount,
            dto.meal.carboAmount,
            ingredientMapper
                .mapToListModel(dto.ingredients)
        )


    fun mapToListModel(dtos: List<DbCustomMeal>): List<CustomMeal> =
        dtos.map { mapToModel(it) }
}