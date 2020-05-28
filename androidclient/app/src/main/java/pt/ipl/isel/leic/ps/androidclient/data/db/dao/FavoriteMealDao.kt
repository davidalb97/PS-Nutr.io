package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.FavoriteMealDto

@Dao
interface FavoriteMealDao {

    @Query("SELECT * FROM FavoriteMeal")
    fun getAll(): LiveData<List<FavoriteMealDto>>

    @Update
    fun update(vararg favoriteMeal: FavoriteMealDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg favoriteMeal: FavoriteMealDto)

    @Query("SELECT * FROM FavoriteMeal where identifier =:identifier")
    fun get(identifier: String?): FavoriteMealDto

    @Delete
    fun delete(favoriteMeal: FavoriteMealDto)
}