package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone

@Entity(tableName = InsulinProfileEntity.tableName)
data class InsulinProfileEntity(
    @PrimaryKey val profileName: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Int,
    val glucoseAmountPerInsulin: Int,
    val carbsAmountPerInsulin: Int,
    val modificationDate: TimestampWithTimeZone?
) {
    companion object {
        const val tableName = "InsulinProfile"
    }
}