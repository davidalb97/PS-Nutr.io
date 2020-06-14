package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = DbCuisineEntity.tableName)
open class DbCuisineEntity(
    val name: String
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "Cuisine"
        const val primaryKeyName = "primaryKey"
        const val mealKeyName = "mealKey"
        const val restaurantKeyName = "restaurantKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = DEFAULT_DB_ID

    var mealKey: Long = DEFAULT_DB_ID

    var restaurantKey: Long = DEFAULT_DB_ID
}