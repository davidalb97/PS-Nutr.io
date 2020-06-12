package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbApiMealRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class DbApiMealMapper(private val dbIngredientMapper: DbIngredientMapper) {

    fun mapToModel(relation: DbApiMealRelation) = Meal(
        dbId = relation.entity.primaryKey,
        submissionId = relation.entity.submissionId,
        name = relation.entity.name,
        isFavorite = relation.entity.isFavorite,
        imageUrl = relation.entity.imageUrl
    )

    fun mapToRelation(model: Meal) = DbApiMealRelation(
        entity = DbApiMealEntity(
            submissionId = model.submissionId,
            isFavorite = model.isFavorite!!,
            imageUrl = model.imageUrl
        ),
    ).also { dto ->
        dto.entity.primaryKey = model.dbId
    }

    fun mapToListModel(relations: List<DbApiMealRelation>) = relations.map(this::mapToModel)
}