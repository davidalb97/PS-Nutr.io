package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DbMealItemMapper(
    private val componentIngredientMapper: DbComponentIngredientMapper,
    private val componentMealMapper: DbComponentMealMapper,
    private val cuisinesMapper: DbCuisineMapper,
    private val portionMapper: DbPortionMapper
) {

    fun mapToModel(entity: DbMealItemEntity) = MealItem(
        dbId = entity.primaryKey,
        dbRestaurantId = entity.restaurantKey,
        submissionId = entity.submissionId,
        name = entity.name,
        votes = if(entity.hasVote) Votes(
            userHasVoted = VoteState.values()[entity.userVoteOrdinal!!],
            positive = entity.positiveVotes!!,
            negative = entity.negativeVotes!!
        ) else null,
        isFavorite = entity.isFavorite,
        imageUri = entity.imageUri?.let { Uri.parse(it) },
        isSuggested = entity.isSuggested
    )

    fun mapToEntity(model: MealItem) = DbMealItemEntity(
        submissionId = model.submissionId,
        name = model.name,
        isFavorite = model.isFavorite,
        imageUri = model.imageUri?.toString(),
        positiveVotes = model.votes?.positive,
        negativeVotes = model.votes?.negative,
        userVoteOrdinal = model.votes?.userHasVoted?.ordinal,
        hasVote = model.votes != null,
        isSuggested = model.isSuggested
    ).also { dto ->
        dto.primaryKey = model.dbId
        dto.restaurantKey = model.dbRestaurantId
    }

    fun mapToListModel(entities: List<DbMealItemEntity>) = entities.map(this::mapToModel)

    fun mapToListEntity(model: List<MealItem>) = model.map(this::mapToEntity)
}