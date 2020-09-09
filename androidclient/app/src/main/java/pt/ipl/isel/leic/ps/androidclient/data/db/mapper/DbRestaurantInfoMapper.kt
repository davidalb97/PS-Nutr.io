package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputFavoriteMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputVotesMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbRestaurantInfoRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.*

class DbRestaurantInfoMapper(
    private val cuisinesMapper: DbCuisineMapper,
    private val mealsMapper: DbMealItemMapper
) {

    fun mapToModel(relation: DbRestaurantInfoRelation): RestaurantInfo {
        val meals = mealsMapper.mapToListModel(relation.meals)
        return RestaurantInfo(
            dbId = relation.entity.primaryKey,
            id = relation.entity.id,
            name = relation.entity.name,
            latitude = relation.entity.latitude,
            longitude = relation.entity.longitude,
            creationDate = relation.entity.creationDate,
            votes = Votes(
                isVotable = relation.entity.isVotable,
                userHasVoted = VoteState.values()[relation.entity.userVoteOrdinal!!],
                positive = relation.entity.positiveVotes!!,
                negative = relation.entity.negativeVotes!!
            ),
            favorites = Favorites(
                relation.entity.isFavorable,
                relation.entity.isFavorite
            ),
            isReportable = relation.entity.isReportable,
            image = Uri.parse(relation.entity.imageUri),
            cuisines = cuisinesMapper.mapToListModel(relation.cuisines),
            meals = meals.filter { !it.isSuggested!! },
            suggestedMeals = meals.filter { it.isSuggested!! },
            ownerId = relation.entity.ownerId,
            source = Source.values()[relation.entity.sourceOrdinal]
        )
    }

    fun mapToRelation(model: RestaurantInfo) = DbRestaurantInfoRelation(
        entity = DbRestaurantInfoEntity(
            id = model.id,
            name = model.name,
            latitude = model.latitude,
            longitude = model.longitude,
            creationDate = model.creationDate,
            isFavorable = model.favorites.isFavorable,
            isFavorite = model.favorites.isFavorite,
            isVotable = model.votes.isVotable,
            isReportable = model.isReportable,
            imageUri = model.image?.toString(),
            positiveVotes = model.votes.positive,
            negativeVotes = model.votes.negative,
            userVoteOrdinal = model.votes.userHasVoted.ordinal,
            ownerId = null,
            sourceOrdinal = model.source.ordinal
        ).also { dto ->
            dto.primaryKey = model.dbId ?: DbRestaurantInfoEntity.DEFAULT_DB_ID
        },
        meals = mealsMapper.mapToListEntity(model.meals.plus(model.suggestedMeals)),
        cuisines = cuisinesMapper.mapToListEntity(model.cuisines)
    )

    fun mapToListModel(relations: List<DbRestaurantInfoRelation>) = relations.map(this::mapToModel)

    fun mapToListEntity(entities: List<RestaurantInfo>) = entities.map(this::mapToRelation)
}