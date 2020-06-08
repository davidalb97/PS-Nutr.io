package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity

data class DbApiMealRelation(
    @Embedded val entity: DbApiMealEntity,
    @Relation(
        parentColumn = DbApiMealEntity.primaryKeyName,
        entityColumn = DbIngredientEntity.mealKeyName
    )
    val ingredients: List<DbIngredientEntity>
): IDbMealRelation<DbApiMealEntity> {

    override fun fetchIngredients() = ingredients

    override fun fetchMeal() = entity
}