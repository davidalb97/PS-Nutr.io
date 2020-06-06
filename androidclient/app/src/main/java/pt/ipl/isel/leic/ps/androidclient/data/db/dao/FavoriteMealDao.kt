package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbFavoriteMealRelation

private const val TABLE = DbFavoriteMealEntity.tableName
private const val PRIMARY_KEY = DbFavoriteMealEntity.primaryKeyName

@Dao
interface FavoriteMealDao {

    @Transaction
    @Query("SELECT * FROM $TABLE")
    fun getAll(): LiveData<List<DbFavoriteMealRelation>>

    @Transaction
    @Update
    fun update(dbFavoriteMeal: DbFavoriteMealEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbFavoriteMeal: DbFavoriteMealEntity)

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :key")
    fun get(key: Long): DbFavoriteMealEntity

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    fun getAllByName(name: String): LiveData<List<DbFavoriteMealRelation>>

    @Transaction
    @Delete
    fun delete(dbFavoriteMeal: DbFavoriteMealEntity)
}