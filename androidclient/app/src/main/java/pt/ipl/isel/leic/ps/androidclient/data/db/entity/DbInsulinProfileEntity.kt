package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbInsulinProfileEntity.tableName)
data class DbInsulinProfileEntity(
    @PrimaryKey val profileName: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Int,
    val glucoseAmountPerInsulin: Int,
    val carbsAmountPerInsulin: Int
) {
    companion object {
        const val tableName = "InsulinProfile"
    }
}