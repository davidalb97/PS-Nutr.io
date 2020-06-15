package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info.DetailedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone

class InputRestaurantInfoMapper(
    private val mealInputMapper: InputMealItemMapper,
    private val cuisineInputMapper: InputCuisineMapper,
    private val votesInputMapper: InputVotesMapper
) {

    fun mapToModel(dto: DetailedRestaurantInput): RestaurantInfo = RestaurantInfo(
        dbId = DbRestaurantInfoEntity.DEFAULT_DB_ID,
        id = dto.id,
        name = dto.name,
        latitude = dto.latitude,
        longitude = dto.longitude,
        votes = votesInputMapper.mapToModel(dto.votes),
        creationDate = TimestampWithTimeZone.parse(dto.creationDate),
        isFavorite = dto.isFavorite,
        cuisines = cuisineInputMapper.mapToListModel(dto.cuisines),
        meals = mealInputMapper.mapToListModel(dto.meals, dto.id),
        suggestedMeals = mealInputMapper.mapToListModel(dto.suggestedMeals, dto.id),
        imageUri = dto.imageUri,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<DetailedRestaurantInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<DetailedRestaurantInput>) = dtos.map(::mapToModel)
}