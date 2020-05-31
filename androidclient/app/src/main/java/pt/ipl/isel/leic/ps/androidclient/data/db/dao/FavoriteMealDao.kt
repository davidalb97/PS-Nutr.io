package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbFavoriteMealDto

@Dao
interface FavoriteMealDao {

    @Query("SELECT * FROM FavoriteMeal")
    fun getAll(): LiveData<List<DbFavoriteMealDto>>

    @Update
    fun update(vararg dbFavoriteMeal: DbFavoriteMealDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbFavoriteMeal: DbFavoriteMealDto)

    @Query("SELECT * FROM FavoriteMeal where identifier =:identifier")
    fun get(identifier: String?): DbFavoriteMealDto

    @Delete
    fun delete(dbFavoriteMeal: DbFavoriteMealDto)
}