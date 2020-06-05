package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbCustomMealEntity.tableName)
data class DbCustomMealEntity(
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val imageUrl: String?
) {
    companion object {
        const val tableName = "CustomMeal"
        const val primaryKeyName = "primaryKey"
    }

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Long = 0
}