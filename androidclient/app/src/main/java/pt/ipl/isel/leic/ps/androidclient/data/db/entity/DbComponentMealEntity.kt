package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbComponentMealEntity.tableName)
class DbComponentMealEntity(
    val submissionId: Int?,
    val name: String,
    val carbs: Float,
    val amount: Float,
    val unit: Int,
    val isFavorite: Boolean,
    val isFavorable: Boolean,
    val isReportable: Boolean?,
    val imageUri: String?,
    val sourceOrdinal: Int
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "ComponentMeal"
        const val primaryKeyName = "primaryKey"
        const val mealKeyName = "mealKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = 0

    var mealKey: Long = 0
}