package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity

data class DbFavoriteMealRelation(
    @Embedded val entity: DbFavoriteMealEntity,
    @Relation(
        parentColumn = DbFavoriteMealEntity.primaryKeyName,
        entityColumn = DbIngredientEntity.mealKeyName
    )
    val ingredients: List<DbIngredientEntity>
): IDbMealRelation<DbFavoriteMealEntity> {

    override fun fetchIngredients() = ingredients

    override fun fetchMeal() = entity
}