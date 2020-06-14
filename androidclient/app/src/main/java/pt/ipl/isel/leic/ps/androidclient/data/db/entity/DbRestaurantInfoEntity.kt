package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone
import java.time.OffsetDateTime

@Entity(tableName = DbRestaurantInfoEntity.tableName)
data class DbRestaurantInfoEntity(
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val isFavorite: Boolean,
    val imageUri: String?,
    val hasVote: Boolean,
    val positiveVotes: Int?,
    val negativeVotes: Int?,
    val userVoteOrdinal: Int?,
    val creationDate: TimestampWithTimeZone?
) {

    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "RestaurantInfo"
        const val primaryKeyName = "primaryKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = DEFAULT_DB_ID
}