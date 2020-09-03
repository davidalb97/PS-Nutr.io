package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.*

class DbRestaurantItemMapper {

    fun mapToModel(entity: DbRestaurantItemEntity) = RestaurantItem(
        dbId = entity.primaryKey,
        id = entity.id,
        name = entity.name,
        latitude = entity.latitude,
        longitude = entity.longitude,
        votes = Votes(
            isVotable = entity.isVotable,
            userHasVoted = VoteState.values()[entity.userVoteOrdinal!!],
            positive = entity.positiveVotes!!,
            negative = entity.negativeVotes!!
        ),
        favorites = Favorites(
            entity.isFavorable,
            entity.isFavorite
        ),
        isReportable = entity.isReportable,
        image = Uri.parse(entity.image),
        source = Source.values()[entity.sourceOrdinal]
    )

    fun mapToEntity(model: RestaurantItem) = DbRestaurantItemEntity(
        id = model.id,
        name = model.name,
        latitude = model.latitude,
        longitude = model.longitude,
        isFavorable = model.favorites.isFavorable,
        isFavorite = model.favorites.isFavorite,
        isVotable = model.votes.isVotable,
        isReportable = model.isReportable,
        image = model.image?.toString(),
        positiveVotes = model.votes?.positive,
        negativeVotes = model.votes?.negative,
        userVoteOrdinal = model.votes?.userHasVoted?.ordinal,
        hasVote = model.votes != null,
        sourceOrdinal = model.source.ordinal
    ).also { dto ->
        dto.primaryKey = model.dbId ?: DbRestaurantItemEntity.DEFAULT_DB_ID
    }

    fun mapToListModel(relations: List<DbRestaurantItemEntity>) = relations.map(this::mapToModel)

    fun mapToListEntity(entities: List<RestaurantItem>) = entities.map(this::mapToEntity)
}