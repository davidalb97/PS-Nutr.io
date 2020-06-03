package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMeal

@Dao
interface CustomMealDao {

    @Query("SELECT * FROM Meal")
    fun getAll(): LiveData<List<DbCustomMeal>>

    @Update
    fun update(vararg dbCustomMeal: DbCustomMeal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbCustomMeal: DbCustomMeal)

    @Query("SELECT * FROM Meal where mealName =:mealName")
    fun get(mealName: String?): DbCustomMeal

    @Delete
    fun delete(dbCustomMeal: DbCustomMeal)
}