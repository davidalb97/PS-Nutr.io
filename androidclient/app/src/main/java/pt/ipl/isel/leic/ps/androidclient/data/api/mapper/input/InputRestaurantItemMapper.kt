package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedRestaurantInput
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.data.model.Source

class InputRestaurantItemMapper(
    private val votesInputMapper: InputVotesMapper
) {

    fun mapToModel(dto: SimplifiedRestaurantInput) = RestaurantItem(
        dbId = DbRestaurantItemEntity.DEFAULT_DB_ID,
        id = dto.id,
        name = dto.name,
        latitude = dto.latitude,
        longitude = dto.longitude,
        votes = votesInputMapper.mapToModel(dto.votes),
        isFavorite = dto.isFavorite,
        isVotable = dto.isVotable,
        imageUri = dto.imageUri,
        source = Source.API
    )

    fun mapToListModel(dtos: Iterable<SimplifiedRestaurantInput>) = dtos.map(::mapToModel)

    fun mapToListModel(dtos: Array<SimplifiedRestaurantInput>) = dtos.map(::mapToModel)
}
