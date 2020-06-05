package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputRestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class InputRestaurantMapper(private val inputMealMapper: InputMealMapper) {

    fun mapToModel(dto: InputRestaurantDto) = Restaurant(
            id = dto.id,
            name = dto.name,
            latitude = dto.latitude,
            longitude = dto.longitude,
            votes = Votes(dto.votes.positive, dto.votes.negative),
            cuisines = dto.cuisines,
            meals = inputMealMapper.mapToListModel(dto.inputMealDtos)
        )

    fun mapToListModel(dtos: Iterable<InputRestaurantDto>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<InputRestaurantDto>) = dtos.map(::mapToModel)
}
