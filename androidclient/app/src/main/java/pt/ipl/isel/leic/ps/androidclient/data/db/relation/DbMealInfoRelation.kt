package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*

data class DbMealInfoRelation(
    @Embedded val entity: DbMealInfoEntity,
    @Relation(
        parentColumn = DbMealInfoEntity.primaryKeyName,
        entityColumn = DbComponentMealEntity.mealKeyName
    )
    val componentMeals: List<DbComponentMealEntity>,
    @Relation(
        parentColumn = DbMealInfoEntity.primaryKeyName,
        entityColumn = DbComponentIngredientEntity.mealKeyName
    )
    val componentIngredients: List<DbComponentIngredientEntity>,
    @Relation(
        parentColumn = DbMealInfoEntity.primaryKeyName,
        entityColumn = DbCuisineEntity.mealKeyName
    )
    val cuisines: List<DbCuisineEntity>,
    @Relation(
        parentColumn = DbMealInfoEntity.primaryKeyName,
        entityColumn = DbPortionEntity.mealKeyName
    )
    val portions: List<DbPortionEntity>
)