package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = DbPortionEntity.tableName)
open class DbPortionEntity(
    val amount: Int
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "Cuisine"
        const val primaryKeyName = "primaryKey"
        const val mealKeyName = "mealKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = DEFAULT_DB_ID

    var mealKey: Long = DEFAULT_DB_ID
}