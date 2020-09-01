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
        votes = relation.entity.isVotable?.let {
            Votes(
                isVotable = relation.entity.isVotable,
                userHasVoted = VoteState.values()[relation.entity.userVoteOrdinal!!],
                positive = relation.entity.positiveVotes!!,
                negative = relation.entity.negativeVotes!!
            )
        },
        favorites = Favorites(
            isFavorable = relation.entity.isFavorable,
            isFavorite = relation.entity.isFavorite
        ),
        imageUri = relation.entity.imageUri?.let { Uri.parse(it) },
        creationDate = relation.entity.creationDate,
        mealComponents = componentMealMapper.mapToListModel(relation.componentMeals),
        ingredientComponents = componentIngredientMapper.mapToListModel(relation.componentIngredients),
        cuisines = cuisinesMapper.mapToListModel(relation.cuisines),
        portions = portionMapper.mapToModel(
            relation.portions[0],
            relation.entity,
            relation.portions.map { it.portion }),
        isSuggested = relation.entity.isSuggested,
        isReportable = relation.entity.isReportable,
        isVerified = relation.entity.isVerified,
        source = Source.values()[relation.entity.sourceOrdinal],
        submissionOwner = relation.entity.ownerId?.let { ownerId ->
            SubmissionOwner(
                id = ownerId
            )
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
            isFavorite = model.favorites.isFavorite,
            imageUri = model.imageUri?.toString(),
            isVotable = model.votes?.isVotable,
            positiveVotes = model.votes?.positive,
            negativeVotes = model.votes?.negative,
            userVoteOrdinal = model.votes?.userHasVoted?.ordinal,
            creationDate = model.creationDate,
            isSuggested = model.isSuggested,
            sourceOrdinal = model.source.ordinal,
            ownerId = model.submissionOwner?.id,
            userPortion = model.portions?.userPortion,
            isFavorable = model.favorites.isFavorable,
            isReportable = model.isReportable,
            isVerified = model.isVerified
        ),
        componentMeals = componentMealMapper.mapToListEntity(model.mealComponents),
        componentIngredients = componentIngredientMapper.mapToListEntity(model.ingredientComponents),
        cuisines = cuisinesMapper.mapToListEntity(model.cuisines),
        portions = model.portions?.let { portionMapper.mapToListEntity(it) } ?: emptyList()
    ).also { dto ->
        dto.entity.primaryKey = model.dbId ?: DbMealInfoEntity.DEFAULT_DB_ID
        dto.entity.restaurantKey = model.dbRestaurantId ?: DbMealInfoEntity.DEFAULT_DB_ID
    }

    fun mapToListModel(relations: List<DbMealInfoRelation>) = relations.map(this::mapToModel)
}