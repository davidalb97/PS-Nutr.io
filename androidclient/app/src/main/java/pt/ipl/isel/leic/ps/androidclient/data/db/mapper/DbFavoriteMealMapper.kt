package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbFavoriteMealRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.FavoriteMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DbFavoriteMealMapper(private val dbIngredientMapper: DbIngredientMapper) {

    fun mapToModel(relation: DbFavoriteMealRelation) = FavoriteMeal(
        dbId = relation.entity.primaryKey,
        submissionId = relation.entity.submissionId,
        name = relation.entity.name,
        carbs = relation.entity.carbs,
        amount = relation.entity.amount,
        unit = relation.entity.unit,
        votes = Votes(
            relation.entity.positiveVotes,
            relation.entity.negativeVotes
        ),
        imageUrl = relation.entity.imageUrl,
        ingredients = dbIngredientMapper.mapToListModel(relation.ingredients)
    )

    fun mapToRelation(model: FavoriteMeal) = DbFavoriteMealRelation(
        entity = DbFavoriteMealEntity(
            submissionId = model.submissionId,
            positiveVotes = model.votes.positive,
            negativeVotes = model.votes.negative,
            name = model.name,
            carbs = model.carbs,
            amount = model.amount,
            unit = model.unit,
            imageUrl = model.imageUrl
        ),
        ingredients = dbIngredientMapper.mapToListDto(model.ingredients)
    ).also { dto ->
        //Set auto incremented serial
        dto.entity.primaryKey = model.dbId
    }

    fun mapToListModel(dtos: Iterable<DbFavoriteMealRelation>) = dtos.map(this::mapToModel)
}