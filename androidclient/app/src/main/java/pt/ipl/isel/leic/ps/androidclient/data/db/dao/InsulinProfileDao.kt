package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.InsulinProfileEntity

@Dao
interface InsulinProfileDao {

    @Query("SELECT * FROM InsulinProfile")
    fun getAll(): LiveData<List<InsulinProfileEntity>>

    @Update
    fun update(vararg profile: InsulinProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg profile: InsulinProfileEntity)

    @Query("SELECT * FROM InsulinProfile where profileName =:name")
    fun get(name: String?): LiveData<InsulinProfileEntity>

    @Delete
    fun delete(profile: InsulinProfileEntity)

}