package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbInsulinProfileDto

@Dao
interface InsulinProfileDao {

    @Query("SELECT * FROM InsulinProfile")
    fun getAll(): LiveData<List<DbInsulinProfileDto>>

    @Update
    fun update(vararg profileDb: DbInsulinProfileDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg profileDb: DbInsulinProfileDto)

    @Query("SELECT * FROM InsulinProfile where profile_name =:name")
    fun get(name: String?): DbInsulinProfileDto

    @Delete
    fun delete(profileDb: DbInsulinProfileDto)

}