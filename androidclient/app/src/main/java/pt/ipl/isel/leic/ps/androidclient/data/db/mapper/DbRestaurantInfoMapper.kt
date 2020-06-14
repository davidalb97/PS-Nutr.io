package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCuisineEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantItemEntity
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
            votes = if(relation.entity.hasVote) Votes(
                userHasVoted = VoteState.values()[relation.entity.userVoteOrdinal!!],
                positive = relation.entity.positiveVotes!!,
                negative = relation.entity.negativeVotes!!
            ) else null,
            isFavorite = relation.entity.isFavorite,
            imageUri = Uri.parse(relation.entity.imageUri),
            cuisines = cuisinesMapper.mapToListModel(relation.cuisines),
            meals = meals.filter { !it.isSuggested },
            suggestedMeals = meals.filter { it.isSuggested }
        )
    }

    fun mapToRelation(model: RestaurantInfo) = DbRestaurantInfoRelation(
        entity = DbRestaurantInfoEntity (
            id = model.id,
            name = model.name,
            latitude = model.latitude,
            longitude = model.longitude,
            creationDate = model.creationDate,
            isFavorite = model.isFavorite,
            imageUri = model.imageUri?.toString(),
            positiveVotes = model.votes?.positive,
            negativeVotes = model.votes?.negative,
            userVoteOrdinal = model.votes?.userHasVoted?.ordinal,
            hasVote = model.votes != null
        ).also { dto ->
            dto.primaryKey = model.dbId
        },
        meals = mealsMapper.mapToListEntity(model.meals.plus(model.suggestedMeals)),
        cuisines = cuisinesMapper.mapToListEntity(model.cuisines)
    )

    fun mapToListModel(relations: List<DbRestaurantInfoRelation>) = relations.map(this::mapToModel)

    fun mapToListEntity(entities: List<RestaurantInfo>) = entities.map(this::mapToRelation)
}