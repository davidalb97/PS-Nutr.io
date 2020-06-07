package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.IMealRelation

@Dao
abstract class BaseMealDao<MealEntity> {

    @Transaction
    @Insert
    fun insert(relation: IMealRelation<MealEntity>) {
        insertMeal(relation.fetchMeal())
        insertIngredients(relation.fetchIngredients())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertIngredients(ingredients: Iterable<DbIngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMeal(mealEntity: MealEntity)

    @Update
    abstract fun updateIngredients(ingredients: Iterable<DbIngredientEntity>)

    @Update
    abstract fun updateMeal(mealEntity: MealEntity)

    @Transaction
    @Delete
    fun delete(relation: IMealRelation<MealEntity>) {
        deleteMeal(relation.fetchMeal())
        deleteIngredients(relation.fetchIngredients())
    }

    @Delete
    abstract fun deleteIngredients(ingredients: Iterable<DbIngredientEntity>)

    @Delete
    abstract fun deleteMeal(mealEntity: MealEntity)
}