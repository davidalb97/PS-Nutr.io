package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfileEntity

@Dao
interface InsulinProfileDao {

    @Query("SELECT * FROM InsulinProfile")
    fun getAll(): LiveData<List<DbInsulinProfileEntity>>

    @Update
    fun update(vararg profile: DbInsulinProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg profile: DbInsulinProfileEntity)

    @Query("SELECT * FROM InsulinProfile where profileName =:name")
    fun get(name: String?): LiveData<DbInsulinProfileEntity>

    @Delete
    fun delete(profile: DbInsulinProfileEntity)

}