package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.InputRestaurantDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class InputRestaurantMapper(private val inputMealMapper: InputMealMapper) {

    // TODO - remove nullables when supported by the http server
    fun mapToModel(dto: InputRestaurantDto) = Restaurant(
            id = dto.id,
            name = dto.name,
            latitude = dto.latitude,
            longitude = dto.longitude,
            votes = dto.votes?.let { Votes(dto.votes.positive, dto.votes.negative) },
            cuisines = dto.cuisines,
            meals = dto.inputMealDtos?.let { inputMealMapper.mapToListModel(it) }
    )

    fun mapToListModel(dtos: Iterable<InputRestaurantDto>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<InputRestaurantDto>) = dtos.map(::mapToModel)
}
