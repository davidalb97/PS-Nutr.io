package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbApiMealRelation

private const val TABLE = DbApiMealEntity.tableName
private const val PRIMARY_KEY = DbApiMealEntity.primaryKeyName

@Dao
interface ApiMealDao {

    @Transaction
    @Query("SELECT * FROM $TABLE")
    fun getAll(): LiveData<List<DbApiMealRelation>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbCustomMealRelation: DbApiMealRelation)

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :submissionId")
    fun get(submissionId: String?): DbApiMealRelation

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    fun getAllByName(name: String): DbApiMealRelation

    @Transaction
    @Delete
    fun delete(dbCustomMealRelation: DbApiMealRelation)
}