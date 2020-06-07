package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbCustomMealRelation
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal

class DbCustomMealMapper(private val dbIngredientMapper: DbIngredientMapper) {

    fun mapToModel(relation: DbCustomMealRelation) = CustomMeal(
        dbId = relation.entity.primaryKey,
        name = relation.entity.name,
        carbs = relation.entity.carbs,
        amount = relation.entity.amount,
        unit = relation.entity.unit,
        imageUrl = relation.entity.imageUrl,
        ingredients = dbIngredientMapper.mapToListModel(relation.ingredients)
    )

    fun mapToRelation(model: CustomMeal) = DbCustomMealRelation(
        entity = DbCustomMealEntity(
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

    fun mapToListModel(relations: List<DbCustomMealRelation>) = relations.map(this::mapToModel)
}