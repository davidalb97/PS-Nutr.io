package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Meal")
data class DbMeal(
    @PrimaryKey val mealName: String,
    val mealQuantity: Int,
    val glucoseAmount: Int,
    val carboAmount: Int
)