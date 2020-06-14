package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DbRestaurantItemMapper {

    fun mapToModel(entity: DbRestaurantItemEntity) = RestaurantItem(
        dbId = entity.primaryKey,
        id = entity.id,
        name = entity.name,
        latitude = entity.latitude,
        longitude = entity.longitude,
        votes = if(entity.hasVote) Votes(
            userHasVoted = VoteState.values()[entity.userVoteOrdinal!!],
            positive = entity.positiveVotes!!,
            negative = entity.negativeVotes!!
        ) else null,
        isFavorite = entity.isFavorite,
        imageUri = Uri.parse(entity.imageUri)
    )

    fun mapToEntity(model: RestaurantItem) = DbRestaurantItemEntity(
        id = model.id,
        name = model.name,
        latitude = model.latitude,
        longitude = model.longitude,
        isFavorite = model.isFavorite,
        imageUri = model.imageUri?.toString(),
        positiveVotes = model.votes?.positive,
        negativeVotes = model.votes?.negative,
        userVoteOrdinal = model.votes?.userHasVoted?.ordinal,
        hasVote = model.votes != null
    ).also { dto ->
        dto.primaryKey = model.dbId
    }

    fun mapToListModel(relations: List<DbRestaurantItemEntity>) = relations.map(this::mapToModel)

    fun mapToListEntity(entities: List<RestaurantItem>) = entities.map(this::mapToEntity)
}