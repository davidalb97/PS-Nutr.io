package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbCustomMealRelation

private const val TABLE = DbIngredientEntity.tableName
private const val PRIMARY_KEY = DbIngredientEntity.primaryKeyName

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient")
    fun getAll(): LiveData<List<DbIngredientEntity>>

    @Update
    fun update(vararg dbIngredientEntity: DbIngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbIngredientEntity: DbIngredientEntity)

    @Query("SELECT * FROM Ingredient where $PRIMARY_KEY = :submissionId")
    fun get(submissionId: Int): DbIngredientEntity

    @Query("SELECT * FROM $TABLE where name = :name")
    fun getAllByName(name: String): DbIngredientEntity

    @Query("SELECT * FROM $TABLE where mealKey = :mealDbKey")
    fun getAllByMeal(mealDbKey: Long): DbIngredientEntity

    @Delete
    fun delete(dbIngredientEntity: DbIngredientEntity)

}