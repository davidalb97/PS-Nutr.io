package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredient")
data class DbIngredient(
    @PrimaryKey val ingredientId: String,
    val name: String,
    val ingredientMealName: String
)