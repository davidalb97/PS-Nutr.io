package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantMealInputDto
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantMeal

class SimplifiedRestaurantMealInputMapper {

    fun mapToModel(dto: SimplifiedRestaurantMealInputDto): RestaurantMeal =
        RestaurantMeal()

    fun mapToListModel(dtos: Iterable<SimplifiedRestaurantMealInputDto>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedRestaurantMealInputDto>) = dtos.map(::mapToModel)
}