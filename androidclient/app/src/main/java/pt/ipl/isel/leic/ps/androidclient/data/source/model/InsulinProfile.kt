package pt.ipl.isel.leic.ps.androidclient.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "InsulinProfile")
data class InsulinProfile (
    @PrimaryKey val profile_name: String,
    val start_time: TimeZone,
    val end_time: TimeZone,
    val fsi: Double,
    val hcr: Double
)