package pt.ipl.isel.leic.ps.androidclient.data.db.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CustomMeal")
data class CustomMealDto(
    @PrimaryKey val name: String,
    val mealQuantity: Int,
    val glucoseAmount: Int,
    val carboAmount: Int
)