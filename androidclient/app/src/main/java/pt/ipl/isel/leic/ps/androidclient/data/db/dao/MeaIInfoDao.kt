package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbMealInfoRelation

private const val TABLE = DbMealInfoEntity.tableName
private const val PRIMARY_KEY = DbMealInfoEntity.primaryKeyName
private const val SOURCE_ORDINAL = DbMealInfoEntity.sourceOrdinalName

@Dao
abstract class MealInfoDao {

    @Transaction
    @Query("SELECT * FROM $TABLE WHERE $PRIMARY_KEY = :dbId AND $SOURCE_ORDINAL = :sourceOrdinal")
    abstract fun getByIdAndSource(
        dbId: Long,
        sourceOrdinal: Int
    ): LiveData<List<DbMealInfoRelation>>

    @Transaction
    @Query("SELECT * FROM $TABLE WHERE $SOURCE_ORDINAL = :sourceOrdinal")
    abstract fun getAllBySource(sourceOrdinal: Int): LiveData<List<DbMealInfoRelation>>

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :dbId")
    abstract fun get(dbId: Long): DbMealInfoRelation

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    abstract fun getAllByName(name: String): DbMealInfoRelation

    @Transaction
    @Insert
    fun insert(relation: DbMealInfoRelation) {
        insertMeal(relation.entity)
        insertComponentMeals(relation.componentMeals)
        insertComponentIngredients(relation.componentIngredients)
        insertCuisines(relation.cuisines)
        insertPortions(relation.portions)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertComponentMeals(componentMealEntities: Iterable<DbComponentMealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertComponentIngredients(componentIngredientEntities: Iterable<DbComponentIngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCuisines(cuisineEntities: Iterable<DbCuisineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPortions(portionEntities: Iterable<DbPortionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMeal(mealEntity: DbMealInfoEntity)

    @Update
    abstract fun updateComponentMeals(componentMealEntities: Iterable<DbComponentMealEntity>)

    @Update
    abstract fun updateComponentIngredients(componentIngredientEntities: Iterable<DbComponentIngredientEntity>)

    @Update
    abstract fun updateCuisines(cuisineEntities: Iterable<DbCuisineEntity>)

    @Update
    abstract fun updatePortions(portionEntities: Iterable<DbPortionEntity>)

    @Update
    abstract fun updateMeal(mealEntity: DbMealInfoEntity)

    @Transaction
    @Delete
    fun delete(relation: DbMealInfoRelation) {
        deleteMeal(relation.entity)
        deleteComponentMeals(relation.componentMeals)
        deleteComponentIngredients(relation.componentIngredients)
        deleteCuisines(relation.cuisines)
        deletePortions(relation.portions)
    }

    @Delete
    abstract fun deleteComponentMeals(componentMealEntities: Iterable<DbComponentMealEntity>)

    @Delete
    abstract fun deleteComponentIngredients(componentIngredientEntities: Iterable<DbComponentIngredientEntity>)

    @Delete
    abstract fun deleteCuisines(cuisineEntities: Iterable<DbCuisineEntity>)

    @Delete
    abstract fun deletePortions(portionEntities: List<DbPortionEntity>)

    @Delete
    abstract fun deleteMeal(mealEntity: DbMealInfoEntity)

    @Query("DELETE FROM $TABLE WHERE $PRIMARY_KEY = :dbMealId")
    abstract fun deleteById(dbMealId: Long)


}