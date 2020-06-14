package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = DbMealItemEntity.tableName)
open class DbMealItemEntity(
    val submissionId: Int,
    val name: String,
    val carbs: Int?,
    val amount: Int?,
    val unit: String?,
    val imageUri: String?,
    val hasVote: Boolean,
    val positiveVotes: Int?,
    val negativeVotes: Int?,
    val userVoteOrdinal: Int?,
    val isFavorite: Boolean,
    val isSuggested: Boolean
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "MealItem"
        const val primaryKeyName = "primaryKey"
        const val restaurantKeyName = "restaurantKey"
    }

    @PrimaryKey(autoGenerate = true)
    open var primaryKey: Long = DEFAULT_DB_ID

    open var restaurantKey: Long = DEFAULT_DB_ID
}