package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbCustomMealRelation

private const val TABLE = DbCustomMealEntity.tableName
private const val PRIMARY_KEY = DbCustomMealEntity.primaryKeyName

@Dao
abstract class CustomMealDao: BaseMealDao<DbCustomMealEntity>() {

    @Transaction
    @Query("SELECT * FROM $TABLE")
    abstract fun getAll(): LiveData<List<DbCustomMealRelation>>

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :key")
    abstract fun get(key: Long): DbCustomMealRelation

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    abstract fun getAllByName(name: String): LiveData<List<DbCustomMealRelation>>
}