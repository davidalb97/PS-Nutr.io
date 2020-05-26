package pt.ipl.isel.leic.ps.androidclient.data.db.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InsulinProfile")
data class InsulinProfileDto(
    @PrimaryKey val profile_name: String,
    val start_time: String,
    val end_time: String,
    val glucose_objective: Int,
    val glucose_amount: Int,
    val carbohydrate_amount: Int
)