package pt.ipl.isel.leic.ps.androidclient.data.db.junction

import androidx.room.Entity
import androidx.room.Junction
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity

@Entity(
    tableName = DbCustomMealJunction.tableName,
    primaryKeys = [DbCustomMealEntity.primaryKeyName, DbIngredientEntity.mealKeyName]
)
data class DbCustomMealJunction(
    val primaryKey: Long,
    val mealKey: Long
) {
    companion object {
        const val tableName = "CustomMealJunction"
    }
}