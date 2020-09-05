package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbRestaurantItemEntity.tableName)
data class DbRestaurantItemEntity(
    val id: String?,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val isFavorable: Boolean,
    val isFavorite: Boolean,
    val isVotable: Boolean,
    val isReportable: Boolean,
    val image: String?,
    val positiveVotes: Int?,
    val negativeVotes: Int?,
    val userVoteOrdinal: Int?,
    val sourceOrdinal: Int
) {

    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "RestaurantItem"
        const val primaryKeyName = "primaryKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = DEFAULT_DB_ID
}