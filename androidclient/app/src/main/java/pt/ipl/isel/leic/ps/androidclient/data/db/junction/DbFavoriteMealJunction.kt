package pt.ipl.isel.leic.ps.androidclient.data.db.junction

import androidx.room.Entity
import androidx.room.Junction
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity

@Entity(
    tableName = DbFavoriteMealJunction.tableName,
    primaryKeys = [DbFavoriteMealEntity.primaryKeyName, DbIngredientEntity.mealKeyName]
)
data class DbFavoriteMealJunction(
    val primaryKey: Long,
    val mealKey: Long
) {
    companion object {
        const val tableName = "FavoriteMealJunction"
    }
}