package pt.ipl.isel.leic.ps.androidclient.data.dao

import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile

@Dao
interface InsulinProfileDao {

    @Query("SELECT * FROM InsulinProfile")
    fun getAll(): List<InsulinProfile>

    @Update
    fun update(vararg profile: InsulinProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg profile: InsulinProfile)

    @Query("SELECT * FROM InsulinProfile where profile_name =:name")
    fun get(name: String?): InsulinProfile

    @Delete
    fun delete(profile: InsulinProfile)

}