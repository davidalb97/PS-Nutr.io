package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.relation.DbFavoriteMealRelation

private const val TABLE = DbFavoriteMealEntity.tableName
private const val PRIMARY_KEY = DbFavoriteMealEntity.primaryKeyName

@Dao
abstract class FavoriteMealDao: BaseMealDao<DbFavoriteMealEntity>() {

    @Transaction
    @Query("SELECT * FROM $TABLE")
    abstract fun getAll(): LiveData<List<DbFavoriteMealRelation>>

    @Transaction
    @Query("SELECT * FROM $TABLE where $PRIMARY_KEY = :key")
    abstract fun get(key: Long): DbFavoriteMealRelation

    @Transaction
    @Query("SELECT * FROM $TABLE where name = :name")
    abstract fun getAllByName(name: String): LiveData<List<DbFavoriteMealRelation>>
}