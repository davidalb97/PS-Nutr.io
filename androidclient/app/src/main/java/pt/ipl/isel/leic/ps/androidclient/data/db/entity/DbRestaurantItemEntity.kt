package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.data.util.readList
import pt.ipl.isel.leic.ps.androidclient.data.util.readOffsetDateTime
import pt.ipl.isel.leic.ps.androidclient.data.util.writeOffsetDateTime
import java.time.OffsetDateTime

@Entity(tableName = DbRestaurantItemEntity.tableName)
data class DbRestaurantItemEntity(
    val id: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val isFavorite: Boolean,
    val imageUri: String?,
    val hasVote: Boolean,
    val positiveVotes: Int?,
    val negativeVotes: Int?,
    val userVoteOrdinal: Int?
) {

    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "RestaurantItem"
        const val primaryKeyName = "primaryKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = DEFAULT_DB_ID
}