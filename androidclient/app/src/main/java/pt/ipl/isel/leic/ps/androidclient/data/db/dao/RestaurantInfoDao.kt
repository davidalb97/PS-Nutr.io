package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCuisineEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbRestaurantInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbRestaurantInfoRelation

private const val TABLE = DbRestaurantInfoEntity.tableName
private const val PRIMARY_KEY = DbRestaurantInfoEntity.primaryKeyName

@Dao
abstract class RestaurantInfoDao {

    @Transaction
    @Query("SELECT * FROM $TABLE")
    abstract fun getAll(): LiveData<List<DbRestaurantInfoRelation>>

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :submissionId")
    abstract fun get(submissionId: String?): DbRestaurantInfoRelation

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    abstract fun getAllByName(name: String): DbRestaurantInfoRelation

    @Transaction
    @Insert
    fun insert(relation: DbRestaurantInfoRelation) {
        insertRestaurant(relation.entity)
        insertCuisines(relation.cuisines)
        insertMeals(relation.meals)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCuisines(cuisineEntities: Iterable<DbCuisineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMeals(mealEntities: Iterable<DbMealItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRestaurant(restaurantEntity: DbRestaurantInfoEntity)

    @Update
    abstract fun updateCuisines(cuisineEntities: Iterable<DbCuisineEntity>)

    @Update
    abstract fun updateMeals(mealEntities: Iterable<DbMealItemEntity>)

    @Update
    abstract fun updateRestaurant(restaurantEntity: DbRestaurantInfoEntity)

    @Transaction
    @Delete
    fun delete(relation: DbRestaurantInfoRelation) {
        deleteRestaurant(relation.entity)
        deleteCuisines(relation.cuisines)
        deleteMeals(relation.meals)
    }

    @Delete
    abstract fun deleteCuisines(cuisineEntities: Iterable<DbCuisineEntity>)

    @Delete
    abstract fun deleteMeals(mealEntities: Iterable<DbMealItemEntity>)

    @Delete
    abstract fun deleteRestaurant(restaurantEntity: DbRestaurantInfoEntity)
}