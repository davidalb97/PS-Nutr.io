package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto

@Dao
interface CustomMealDao {

    @Query("SELECT * FROM CustomMeal")
    fun getAll(): LiveData<List<CustomMealDto>>

    @Update
    fun update(vararg customMeal: CustomMealDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg customMeal: CustomMealDto)

    @Query("SELECT * FROM CustomMeal where name =:name")
    fun get(name: String?): CustomMealDto

    @Delete
    fun delete(customMeal: CustomMealDto)
}