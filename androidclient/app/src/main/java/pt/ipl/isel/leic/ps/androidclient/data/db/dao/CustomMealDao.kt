package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbCustomMealDto

@Dao
interface CustomMealDao {

    @Query("SELECT * FROM CustomMeal")
    fun getAll(): LiveData<List<DbCustomMealDto>>

    @Update
    fun update(vararg dbCustomMeal: DbCustomMealDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbCustomMeal: DbCustomMealDto)

    @Query("SELECT * FROM CustomMeal where name =:name")
    fun get(name: String?): DbCustomMealDto

    @Delete
    fun delete(dbCustomMeal: DbCustomMealDto)
}