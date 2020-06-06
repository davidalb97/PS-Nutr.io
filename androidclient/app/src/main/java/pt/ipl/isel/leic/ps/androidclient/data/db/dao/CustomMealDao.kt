package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbApiMealRelation
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbCustomMealRelation

private const val TABLE = DbCustomMealEntity.tableName
private const val PRIMARY_KEY = DbCustomMealEntity.primaryKeyName

@Dao
interface CustomMealDao{

    @Transaction
    @Query("SELECT * FROM $TABLE")
    fun getAll(): LiveData<List<DbCustomMealRelation>>

    @Transaction
    @Update
    fun update(dbCustomMeal: DbCustomMealEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbCustomMealRelation: DbCustomMealEntity)

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :key")
    fun get(key: Long): DbCustomMealEntity

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    fun getAllByName(name: String): LiveData<List<DbCustomMealRelation>>

    @Transaction
    @Delete
    fun delete(dbCustomMealRelation: DbCustomMealEntity)
}