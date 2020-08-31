package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbMealInfoRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class DbMealInfoMapper(
    private val componentIngredientMapper: DbComponentIngredientMapper,
    private val componentMealMapper: DbComponentMealMapper,
    private val cuisinesMapper: DbCuisineMapper,
    private val portionMapper: DbPortionMapper
) {

    fun mapToModel(relation: DbMealInfoRelation) = MealInfo(
        dbId = relation.entity.primaryKey,
        dbRestaurantId = relation.entity.restaurantKey,
        submissionId = relation.entity.submissionId,
        restaurantSubmissionId = relation.entity.restaurantSubmissionId,
        name = relation.entity.name,
        carbs = relation.entity.carbs,
        amount = relation.entity.amount,
        unit = WeightUnits.values()[relation.entity.unit],
        votes = if (relation.entity.hasVote) Votes(
            userHasVoted = VoteState.values()[relation.entity.userVoteOrdinal!!],
            positive = relation.entity.positiveVotes!!,
            negative = relation.entity.negativeVotes!!
        ) else null,
        isFavorite = relation.entity.isFavorite,
        isVotable = relation.entity.isVotable,
        imageUri = relation.entity.imageUri?.let { Uri.parse(it) },
        creationDate = relation.entity.creationDate,
        mealComponents = componentMealMapper.mapToListModel(relation.componentMeals),
        ingredientComponents = componentIngredientMapper.mapToListModel(relation.componentIngredients),
        cuisines = cuisinesMapper.mapToListModel(relation.cuisines),
        portions = portionMapper.mapToListModel(relation.portions),
        isSuggested = relation.entity.isSuggested,
        source = Source.values()[relation.entity.sourceOrdinal],
        submissionOwner = relation.entity.ownerId?.let { ownerId ->
            relation.entity.ownerName?.let { ownerName ->
                SubmissionOwner(
                    username = ownerName,
                    id = ownerId
                )
            }
        }
    )

    fun mapToRelation(model: MealInfo) = DbMealInfoRelation(
        entity = DbMealInfoEntity(
            submissionId = model.submissionId,
            restaurantSubmissionId = model.restaurantSubmissionId,
            name = model.name,
            carbs = model.carbs,
            amount = model.amount,
            unit = model.unit.ordinal,
            isFavorite = model.isFavorite,
            isVotable = model.isVotable,
            imageUri = model.imageUri?.toString(),
            positiveVotes = model.votes?.positive,
            negativeVotes = model.votes?.negative,
            userVoteOrdinal = model.votes?.userHasVoted?.ordinal,
            hasVote = model.votes != null,
            creationDate = model.creationDate,
            isSuggested = model.isSuggested,
            sourceOrdinal = model.source.ordinal,
            ownerId = model.submissionOwner?.id,
            ownerName = model.submissionOwner?.username,
        ),
        componentMeals = componentMealMapper.mapToListEntity(model.mealComponents),
        componentIngredients = componentIngredientMapper.mapToListEntity(model.ingredientComponents),
        cuisines = cuisinesMapper.mapToListEntity(model.cuisines),
        portions = portionMapper.mapToListEntity(model.portions)
    ).also { dto ->
        dto.entity.primaryKey = model.dbId ?: DbMealInfoEntity.DEFAULT_DB_ID
        dto.entity.restaurantKey = model.dbRestaurantId ?: DbMealInfoEntity.DEFAULT_DB_ID
    }

    fun mapToListModel(relations: List<DbMealInfoRelation>) = relations.map(this::mapToModel)
}