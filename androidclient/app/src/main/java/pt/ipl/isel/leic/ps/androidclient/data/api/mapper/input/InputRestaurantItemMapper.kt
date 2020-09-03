package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurant.RestaurantItemContainerInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurant.RestaurantItemInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source

class InputRestaurantItemMapper(
    private val votesInputMapper: InputVotesMapper,
    private val favoritesInputMapper: InputFavoriteMapper
) {

    fun mapToModel(dto: RestaurantItemInput) = RestaurantItem(
        dbId = DbRestaurantItemEntity.DEFAULT_DB_ID,
        id = dto.identifier,
        name = dto.name,
        latitude = dto.latitude,
        longitude = dto.longitude,
        favorites = favoritesInputMapper.mapToModel(dto.favorites),
        votes = votesInputMapper.mapToModel(dto.votes),
        isReportable = dto.isReportable,
        image = dto.image,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<RestaurantItemInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<RestaurantItemInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dto: RestaurantItemContainerInput) =
        dto.restaurants.map(::mapToModel)
}
