package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class DbMealItemMapper {

    fun mapToModel(entity: DbMealItemEntity) = MealItem(
        dbId = entity.primaryKey,
        dbRestaurantId = entity.restaurantKey,
        submissionId = entity.submissionId,
        restaurantSubmissionId = entity.restaurantSubmissionId,
        name = entity.name,
        carbs = entity.carbs,
        amount = entity.amount,
        unit = WeightUnits.values()[entity.unit],
        votes = if (entity.hasVote) Votes(
            isVotable = entity.isVotable ?: false,
            userHasVoted = VoteState.values()[entity.userVoteOrdinal!!],
            positive = entity.positiveVotes!!,
            negative = entity.negativeVotes!!
        ) else null,
        favorites = Favorites(
            false,
            entity.isFavorite
        ),
        isReportable = false,
        isVerified = false,
        imageUri = entity.imageUri?.let { Uri.parse(it) },
        isSuggested = entity.isSuggested,
        source = Source.values()[entity.sourceOrdinal]
    )

    fun mapToEntity(model: MealItem) = DbMealItemEntity(
        submissionId = model.submissionId,
        restaurantSubmissionId = model.restaurantSubmissionId,
        name = model.name,
        carbs = model.carbs,
        amount = model.amount,
        unit = model.unit.ordinal,
        isFavorite = model.favorites.isFavorite,
        isVotable = model.votes?.isVotable ?: false,
        imageUri = model.imageUri?.toString(),
        positiveVotes = model.votes?.positive,
        negativeVotes = model.votes?.negative,
        userVoteOrdinal = model.votes?.userHasVoted?.ordinal,
        hasVote = model.votes != null,
        isSuggested = model.isSuggested,
        sourceOrdinal = model.source.ordinal
    ).also { dto ->
        dto.primaryKey = model.dbId ?: DbMealItemEntity.DEFAULT_DB_ID
        dto.restaurantKey = model.dbRestaurantId ?: DbMealItemEntity.DEFAULT_DB_ID
    }

    fun mapToListModel(entities: List<DbMealItemEntity>) = entities.map(this::mapToModel)

    fun mapToListEntity(model: List<MealItem>) = model.map(this::mapToEntity)
}