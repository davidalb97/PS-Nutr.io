package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMeal

@Dao
interface FavoriteMealDao {

    @Query("SELECT * FROM Meal")
    fun getAll(): LiveData<List<DbFavoriteMeal>>

    @Update
    fun update(vararg dbFavoriteMeal: DbFavoriteMeal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbFavoriteMeal: DbFavoriteMeal)

    @Query("SELECT * FROM Meal where mealName =:mealName")
    fun get(mealName: String?): DbFavoriteMeal

    @Delete
    fun delete(dbFavoriteMeal: DbFavoriteMeal)
}