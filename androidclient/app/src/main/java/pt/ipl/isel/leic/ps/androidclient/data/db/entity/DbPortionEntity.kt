package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbPortionEntity.tableName)
open class DbPortionEntity(
    val portion: Float
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "Portion"
        const val primaryKeyName = "primaryKey"
        const val mealKeyName = "mealKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = DEFAULT_DB_ID

    var mealKey: Long = DEFAULT_DB_ID
}