package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurant.RestaurantInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

class InputRestaurantInfoMapper(
    private val mealInputMapper: InputMealItemMapper,
    private val cuisineInputMapper: InputCuisineMapper,
    private val votesInputMapper: InputVotesMapper,
    private val inputFavoriteMapper: InputFavoriteMapper
) {

    fun mapToModel(dto: RestaurantInfoInput): RestaurantInfo = RestaurantInfo(
        dbId = DbRestaurantInfoEntity.DEFAULT_DB_ID,
        id = dto.identifier,
        name = dto.name,
        latitude = dto.latitude,
        longitude = dto.longitude,
        votes = votesInputMapper.mapToModel(dto.votes),
        creationDate = TimestampWithTimeZone.parse(dto.creationDate),
        favorites = inputFavoriteMapper.mapToModel(dto.favorites),
        isReportable = dto.isReportable,
        cuisines = cuisineInputMapper.mapToListModel(dto.cuisines),
        meals = mealInputMapper.mapToListModel(dto.meals, dto.identifier),
        suggestedMeals = mealInputMapper.mapToListModel(dto.suggestedMeals, dto.identifier),
        image = dto.image,
        ownerId = dto.createdBy.id,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<RestaurantInfoInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<RestaurantInfoInput>) = dtos.map(::mapToModel)
}