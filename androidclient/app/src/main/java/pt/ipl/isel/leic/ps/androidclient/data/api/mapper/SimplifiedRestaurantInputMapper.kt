package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInputDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class SimplifiedRestaurantInputMapper(private val inputMealMapper: DetailedMealInputMapper) {

    fun mapToModel(dto: SimplifiedRestaurantInputDto) = Restaurant(
            id = dto.id,
            name = dto.name,
            latitude = dto.latitude,
            longitude = dto.longitude,
            votes = Votes(dto.votes!!.userHasVoted, dto.votes.positive, dto.votes.negative),
            isFavorite = dto.isFavorite
    )

    fun mapToListModel(dtos: Iterable<SimplifiedRestaurantInputDto>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedRestaurantInputDto>) = dtos.map(::mapToModel)
}
