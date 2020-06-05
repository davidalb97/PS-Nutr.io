package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbApiMealRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.ApiMeal
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DbApiMealMapper(private val dbIngredientMapper: DbIngredientMapper) {

    fun mapToModel(relation: DbApiMealRelation) = ApiMeal(
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

    fun mapToRelation(model: ApiMeal) = DbApiMealRelation(
        entity = DbApiMealEntity(
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
        dto.entity.primaryKey = model.dbId
    }

    fun mapToListModel(relations: List<DbApiMealRelation>) = relations.map(this::mapToModel)
}