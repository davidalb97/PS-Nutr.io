package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbApiMealRelation

private const val TABLE = DbApiMealEntity.tableName
private const val PRIMARY_KEY = DbApiMealEntity.primaryKeyName

@Dao
abstract class ApiMealDao: BaseMealDao<DbApiMealEntity>() {

    @Transaction
    @Query("SELECT * FROM $TABLE")
    abstract fun getAll(): LiveData<List<DbApiMealRelation>>

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :submissionId")
    abstract fun get(submissionId: String?): DbApiMealRelation

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    abstract fun getAllByName(name: String): DbApiMealRelation
}