package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto

@Dao
interface InsulinProfileDao {

    @Query("SELECT * FROM InsulinProfile")
    fun getAll(): LiveData<List<InsulinProfileDto>>

    @Update
    fun update(vararg profile: InsulinProfileDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg profile: InsulinProfileDto)

    @Query("SELECT * FROM InsulinProfile where profile_name =:name")
    fun get(name: String?): InsulinProfileDto

    @Delete
    fun delete(profile: InsulinProfileDto)

}