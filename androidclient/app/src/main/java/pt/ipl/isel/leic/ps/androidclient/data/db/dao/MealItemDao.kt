package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*

@Dao
abstract class MealItemDao {

    @Transaction
    @Delete
    fun deleteItem(mealItem: DbMealItemEntity) {
        deleteMeal(mealItem)
        deleteComponentIngredientsByMealId(mealItem.primaryKey)
        deleteComponentMealsByMealId(mealItem.primaryKey)
        deleteCuisinesByMealId(mealItem.primaryKey)
        deletePortionsByMealId(mealItem.primaryKey)
    }

    @Delete
    abstract fun deleteMeal(mealEntity: DbMealItemEntity)

    @Query("DELETE FROM ${DbComponentMealEntity.tableName}" +
            " WHERE ${DbComponentMealEntity.mealKeyName} = :mealId")
    abstract fun deleteComponentMealsByMealId(mealId: Long)

    @Query("DELETE FROM ${DbComponentIngredientEntity.tableName}" +
            " WHERE ${DbComponentIngredientEntity.mealKeyName} = :mealId")
    abstract fun deleteComponentIngredientsByMealId(mealId: Long)

    @Query("DELETE FROM ${DbCuisineEntity.tableName}" +
            " WHERE ${DbCuisineEntity.mealKeyName} = :mealId")
    abstract fun deleteCuisinesByMealId(mealId: Long)

    @Query("DELETE FROM ${DbPortionEntity.tableName}" +
            " WHERE ${DbPortionEntity.mealKeyName} = :mealId")
    abstract fun deletePortionsByMealId(mealId: Long)
}