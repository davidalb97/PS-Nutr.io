package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = DbFavoriteMealEntity.tableName,
    indices = [Index(value = [DbFavoriteMealEntity.unique], unique = true)]
)
data class DbFavoriteMealEntity(
    val submissionId: Int,
    val positiveVotes: Int,
    val negativeVotes: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?
) {
    companion object {
        const val tableName = "FavoriteMeal"
        const val primaryKeyName = "primaryKey"
        const val unique = "submissionId"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = 0
}