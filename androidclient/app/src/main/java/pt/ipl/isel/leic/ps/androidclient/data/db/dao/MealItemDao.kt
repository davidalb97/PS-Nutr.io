package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity

private const val TABLE = DbMealItemEntity.tableName
private const val PRIMARY_KEY = DbMealItemEntity.primaryKeyName
private const val SOURCE_ORDINAL = DbMealItemEntity.sourceOrdinalName

@Dao
abstract class MealItemDao {

    @Query("SELECT * FROM $TABLE WHERE $SOURCE_ORDINAL = :sourceOrdinal")
    abstract fun getAllBySource(sourceOrdinal: Int): LiveData<List<DbMealItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(mealEntity: DbMealItemEntity)

    @Delete
    abstract fun delete(mealEntity: DbMealItemEntity)

    @Query(
        "DELETE FROM ${DbMealItemEntity.tableName}" +
                " WHERE ${DbMealItemEntity.sourceOrdinalName} = :sourceOrdinal"
    )
    abstract fun deleteAllBySource(sourceOrdinal: Int)
}