package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DetailedRestaurantInputMapper(
    private val simplifiedRestaurantMealInputMapper: SimplifiedRestaurantMealInputMapper
) {

    fun mapToModel(dto: DetailedRestaurantInput) =
        RestaurantInfo(
            id = dto.id,
            name = dto.name,
            latitude = dto.latitude,
            longitude = dto.longitude,
            votes = Votes(dto.votes!!.userHasVoted, dto.votes.positive, dto.votes.negative),
            isFavorite = dto.isFavorite,
            cuisines = dto.cuisines,
            creationDate = dto.creationDate,
            meals = simplifiedRestaurantMealInputMapper.mapToListModel(dto.meals),
            suggestedMeals = simplifiedRestaurantMealInputMapper.mapToListModel(dto.suggestedMeals)
        )

    fun mapToInput(model: RestaurantInfo) {

    }

    fun mapToListModel(dtos: Iterable<DetailedRestaurantInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<DetailedRestaurantInput>) = dtos.map(::mapToModel)
}