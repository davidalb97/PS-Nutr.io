package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = DbMealInfoEntity.tableName)
open class DbMealInfoEntity(
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val isFavorite: Boolean,
    val imageUri: String?,
    val hasVote: Boolean,
    val positiveVotes: Int?,
    val negativeVotes: Int?,
    val userVoteOrdinal: Int?,
    val creationDate: OffsetDateTime?,
    val isSuggested: Boolean
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "MealInfo"
        const val primaryKeyName = "primaryKey"
    }

    @PrimaryKey(autoGenerate = true)
    open var primaryKey: Long = DEFAULT_DB_ID
}