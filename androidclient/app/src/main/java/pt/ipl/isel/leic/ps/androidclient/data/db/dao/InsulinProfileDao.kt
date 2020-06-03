package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile

@Dao
interface InsulinProfileDao {

    @Query("SELECT * FROM InsulinProfile")
    fun getAll(): LiveData<List<DbInsulinProfile>>

    @Update
    fun update(vararg profileDb: DbInsulinProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg profileDb: DbInsulinProfile)

    @Query("SELECT * FROM InsulinProfile where profileName =:name")
    fun get(name: String?): DbInsulinProfile

    @Delete
    fun delete(profileDb: DbInsulinProfile)

}