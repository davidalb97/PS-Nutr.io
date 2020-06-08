package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity

data class DbCustomMealRelation(
    @Embedded val entity: DbCustomMealEntity,
    @Relation(
        parentColumn = DbCustomMealEntity.primaryKeyName,
        entityColumn = DbIngredientEntity.mealKeyName
    )
    val ingredients: List<DbIngredientEntity>
): IDbMealRelation<DbCustomMealEntity> {

    override fun fetchIngredients() = ingredients

    override fun fetchMeal() = entity
}