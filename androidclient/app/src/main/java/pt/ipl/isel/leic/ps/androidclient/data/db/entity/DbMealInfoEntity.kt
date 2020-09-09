package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

@Entity(tableName = DbMealInfoEntity.tableName)
open class DbMealInfoEntity(
    val submissionId: Int?,
    val restaurantSubmissionId: String?,
    val name: String,
    val carbs: Float,
    val amount: Float,
    val unit: Int,
    val imageUri: String?,
    val isVotable: Boolean?,
    val positiveVotes: Int?,
    val negativeVotes: Int?,
    val userVoteOrdinal: Int?,
    val creationDate: TimestampWithTimeZone?,
    val sourceOrdinal: Int,
    val ownerId: Int?,
    val userPortion: Float?,
    val isFavorite: Boolean,
    val isFavorable: Boolean,
    val isReportable: Boolean?,
    val isSuggested: Boolean?,
    val isVerified: Boolean?
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "MealInfo"
        const val primaryKeyName = "primaryKey"
        const val sourceOrdinalName = "sourceOrdinal"
    }

    @PrimaryKey(autoGenerate = true)
    open var primaryKey: Long = DEFAULT_DB_ID

    var restaurantKey: Long = DEFAULT_DB_ID
}