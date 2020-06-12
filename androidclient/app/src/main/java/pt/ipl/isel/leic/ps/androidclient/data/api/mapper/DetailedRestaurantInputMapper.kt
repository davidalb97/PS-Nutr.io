package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.DetailedRestaurantInputDto
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DetailedRestaurantInputMapper(
    private val simplifiedRestaurantMealInputMapper: SimplifiedRestaurantMealInputMapper
) {

    fun mapToModel(dto: DetailedRestaurantInputDto) =
        RestaurantInfo(
            id = dto.id,
            name = dto.name,
            latitude = dto.latitude,
            longitude = dto.longitude,
            votes = Votes(dto.votes!!.userHasVoted, dto.votes.positive, dto.votes.negative),
            isFavorite = dto.isFavorite,
            cuisines = dto.cuisines,
            creationDate = dto.creationDate,
            meals = dto.meals.map { simplifiedRestaurantMealInputMapper.mapToListModel(it) },
            suggestedMeals = dto.suggestedMeals
                .map { simplifiedRestaurantMealInputMapper.mapToListModel(it) }
        )

    fun mapToListModel(dtos: Iterable<DetailedRestaurantInputDto>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<DetailedRestaurantInputDto>) = dtos.map(::mapToModel)
}