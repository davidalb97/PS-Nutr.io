package pt.ipl.isel.leic.ps.androidclient.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal

@Dao
interface MealDao {

    @Query("SELECT * FROM Meal")
    fun getAll(): LiveData<List<Meal>>

    @Update
    fun update(vararg meal: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg meal: Meal)

    @Query("SELECT * FROM Meal where identifier =:name")
    fun get(name: String?): Meal

    @Delete
    fun delete(meal: Meal)
}