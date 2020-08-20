package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

@Entity(tableName = DbCalculationHistoryEntity.tableName)
open class DbCalculationHistoryEntity(
    val time: TimestampWithTimeZone,
    val mealName: String,
    val result: Float
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "CalculationHistory"
        const val primaryKeyName = "primaryKey"
    }

    @PrimaryKey(autoGenerate = true)
    open var primaryKey: Long = DEFAULT_DB_ID
}