package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCalculationHistoryEntity

@Dao
interface CalculationHistoryDao {

    @Query("SELECT * FROM CalculationHistory")
    fun getAll(): LiveData<List<DbCalculationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg calculation: DbCalculationHistoryEntity)

    @Delete
    fun delete(profile: DbCalculationHistoryEntity)
}