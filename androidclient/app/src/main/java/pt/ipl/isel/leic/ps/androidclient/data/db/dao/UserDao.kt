package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbUserEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.InsulinProfileEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM DbUser")
    fun getAll(): LiveData<List<DbUserEntity>>

    @Update
    fun update(vararg userProfile: DbUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg userProfile: DbUserEntity)

    @Query("SELECT * FROM DbUser where userId =:id")
    fun get(id: Int): LiveData<DbUserEntity>

    @Delete
    fun delete(profile: InsulinProfileEntity)
}