package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbComponentIngredientEntity.tableName)
class DbComponentIngredientEntity(
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val isFavorite: Boolean,
    val imageUri: String?
) {
    companion object {
        const val DEFAULT_DB_ID: Long = 0
        const val tableName = "ComponentIngredient"
        const val primaryKeyName = "primaryKey"
        const val mealKeyName = "mealKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = 0

    var mealKey: Long = 0
}