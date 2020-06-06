package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbApiMealRelation

private const val TABLE = DbApiMealEntity.tableName
private const val PRIMARY_KEY = DbApiMealEntity.primaryKeyName

@Dao
interface ApiMealDao<T> {

    @Transaction
    @Query("SELECT * FROM $TABLE")
    fun getAll(): LiveData<List<T>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbCustomMealRelation: T)

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :submissionId")
    fun get(submissionId: String?): T

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    fun getAllByName(name: String): T

    @Transaction
    @Delete
    fun delete(dbCustomMealRelation: T)
}