package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealInput
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantMeal

class SimplifiedRestaurantMealInputMapper {

    fun mapToModel(dto: SimplifiedRestaurantMealInput): RestaurantMeal =
        RestaurantMeal(
            id = dto.id
        )

    fun mapToListModel(dtos: Iterable<SimplifiedRestaurantMealInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedRestaurantMealInput>) = dtos.map(::mapToModel)
}